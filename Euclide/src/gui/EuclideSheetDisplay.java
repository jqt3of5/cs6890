/* file : EuclideSheetDisplay.java
 * 
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 5 Apr. 2008
 *
 */

package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JPanel;

import math.geom2d.*;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.PointSet2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Rectangle2D;
import model.EuclideLayer;
import model.EuclideFigure;
import model.EuclideSheet;
import model.style.DrawStyle;
import model.style.Marker;

import org.apache.log4j.Logger;

import dynamic.DynamicShape2D;

/**
 * The basic class for displaying an EuclideSheet. Can be extended to manage
 * mouse listeners and reaction to user input, see EuclideSheetView for
 * example.
 * 
 * @author dlegland
 */
public class EuclideSheetDisplay extends JPanel {

	// ===================================================================
	// static variables

	/**
	 * version.
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	/** The different clipping possibilities. */
	public enum Clip {
		/**
		 * Draw figures only in the visible part of the sheet, defined by the
		 * view box.
		 */
		VIEWBOX,
		/**
		 * Draw figures only in the visible part of the page, possibly inside
		 * margin.
		 */
		PAGE,
		/** Draw figures on the whole window, possibly out of page. */
		WINDOW,
		/** Draw figures only inside margin of document. */
		MARGIN
	};

	// ===================================================================
	// class variables

	/** The sheet to display. Assume this can not be null. */
	protected EuclideSheet sheet;

	// ===================================================================
	// Display management

	/**
	 * The position of origin point (with user coordinate (0,0) ) on the page.
	 */
	protected Point2D origin = new Point2D();

	/**
	 * The number of pixel to represent one pixel unit. This is used to compute
	 * appropriate zoom.
	 */
	protected double dpu = 10;

	/**
	 * The position, in user coordinate, of the center of the view. Used when
	 * drawing an 'infinite' page.
	 */
	protected Point2D viewCenter = new Point2D(0, 0);

	protected AffineTransform2D user2Display = null;

	/** user to page transform */
	protected AffineTransform user2Page = new AffineTransform();

	/** page to display transform */
	protected AffineTransform page2Display = new AffineTransform();

	/**
	 * Direction of the y axis: up or bottom of the page. Up: necessary for
	 * geometric Down: 'text', 'image'
	 */
	protected boolean yToTop = true;

	/**
	 * Zoom level of the page (same for x and y). zoom=1 means same scale, zoom<1
	 * is zoom out, and zoom in is >1. Zoom equal to 0 means that zoom and
	 * position will be defined next time the paint method will be called.
	 */
	protected double zoom = 2;

	/**
	 * The position of the upper left corner of the page in the panel
	 * coordinate.
	 */
	protected Point2D pageOffset = new Point2D(10, 10);

	/** A flag determining if margin have to be drawn. */
	protected boolean drawMargin = false;

	/** The current clipping style. Defines the clipping rectangle */
	protected Clip clipping = Clip.VIEWBOX;

	/**
	 * The clipping box of shapes. It is equivalent to the 'viewbox' of SVG
	 * documents. Expressed in user coordinate.
	 */
	protected Box2D clippingBox = new Box2D(0, 100, 0, 100);

	// ===================================================================
	// Constructor

	/**
	 * Main constructor. Needs to specify a sheet to be valid.
	 */
	public EuclideSheetDisplay(EuclideSheet sheet) {
		super();
		this.sheet = sheet;

		this.setBackground(Color.WHITE);

		// set up position of origin point in the middle of the page
		 setOriginToPageCenter();

		// set up user coordinate of view center. Default: middle of view box.
		 setViewCenterToPageCenter();

		updateUser2DisplayTransform();
	}

	// ===================================================================
	// zoom management

	public double getDotsPerUnit() {
		return dpu;
	}

	/**
	 * Set up the resolution of the display. One user unit will be represented
	 * at zoom 1 by the number of pixels given by <code>dpu</code>.
	 * 
	 * @param dpu the number of pixels to represent one user unit.
	 */
	public void setDotsPerUnit(double dpu) {
		this.dpu = dpu;
	}

	/**
	 * Get the current zoom factor
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Change the zoom to a new value
	 * 
	 * @param z the new zoom factor
	 */
	public void setZoom(double z) {
		zoom = z;
		updateUser2DisplayTransform();
	}

	// ===================================================================
	// sheet management

	public EuclideSheet getSheet() {
		return sheet;
	}

	
	// ===================================================================
	// Drawing utilities
	
	/**
	* Set up position of origin point to the middle of the page
	*/
	public void setOriginToPageCenter() {
		Dimension dim = sheet.getDimension();
		EuclideSheet.Margin margin = sheet.getMargin();
		origin = new Point2D(
				((dim.width - margin.left - margin.right) / 2 + margin.left),
				((dim.height - margin.top - margin.bottom) / 2 + margin.bottom));
	}
	
	/**
	* Set up user coordinate of view center to the middle of view box.
	*/
	public void setViewCenterToPageCenter() {
		Box2D viewBox = sheet.getViewBox();
		viewCenter = new Point2D(
				(viewBox.getMinX() + viewBox.getMaxX()) / 2,
				(viewBox.getMinY() + viewBox.getMaxY()) / 2);
	}

	
	// ===================================================================
	// functions inherited from GUI class

	@Override
	public java.awt.Dimension getPreferredSize() {
		if (sheet == null)
			return new Dimension(100, 100);

		if (sheet.isViewPage()) {
			// Return size of the page plus some offset multiplied by current
			// zoom
			java.awt.Dimension dim = sheet.getDimension();
			double width = (dim.width + 2 * pageOffset.getX()) * zoom * dpu;
			double height = (dim.height + 2 * pageOffset.getY()) * zoom * dpu;
			return new Dimension((int) Math.ceil(width),
					(int) Math.ceil(height));
		} else {
			Box2D viewBox = sheet.getViewBox();
			double width = viewBox.getWidth() * zoom * dpu;
			double height = viewBox.getHeight() * zoom * dpu;
			return new Dimension((int) Math.ceil(width),
					(int) Math.ceil(height));
		}
	}

	// ===================================================================
	// paint functions

	/**
	 * Update coordinate transforms, and clipping box.
	 */
	protected void updateUser2DisplayTransform() {
		Dimension dim = sheet.getDimension();

		AffineTransform user2Display0 = new AffineTransform();

		// Update user to display transform
		if (this.sheet.isViewPage()) {
			// First update user 2 page transform
			user2Page.setToIdentity();
			if (yToTop) {
				user2Page.preConcatenate(AffineTransform
						.getScaleInstance(1, -1));
				user2Page.preConcatenate(AffineTransform.getTranslateInstance(
						origin.getX(), -origin.getY()));
				user2Page.preConcatenate(AffineTransform.getTranslateInstance(
						0, dim.height));
			} else {
				user2Page.preConcatenate(AffineTransform.getTranslateInstance(
						origin.getX(), origin.getY()));
			}

			// Apply page to display transform
			page2Display.setToIdentity();
			page2Display.preConcatenate(AffineTransform.getTranslateInstance(
					pageOffset.getX(), pageOffset.getY()));
			page2Display.preConcatenate(AffineTransform.getScaleInstance(zoom
					* dpu, zoom * dpu));

			// Concatenate to obtain user to display transform
			user2Display0.setToIdentity();
			user2Display0.preConcatenate(user2Page);
			user2Display0.preConcatenate(page2Display);
			
		} else {
			user2Display0.setToIdentity();
			user2Display0.preConcatenate(AffineTransform.getTranslateInstance(
					-viewCenter.getX(), -viewCenter.getY()));
			if (yToTop)
				user2Display0.preConcatenate(AffineTransform.getScaleInstance(
						1, -1));
			user2Display0.preConcatenate(AffineTransform.getScaleInstance(zoom
					*dpu, zoom*dpu));

			java.awt.Dimension panelSize = this.getSize();
			// System.out.println(panelSize);
			// java.awt.Dimension panelSize = new java.awt.Dimension(300, 300);
			double xc = panelSize.getWidth()/2.;
			double yc = panelSize.getHeight()/2.;
			user2Display0.preConcatenate(AffineTransform.getTranslateInstance(
					xc, yc));
		}

		double[] mat = new double[6];
		user2Display0.getMatrix(mat);
		user2Display = new AffineTransform2D(mat[0], mat[2], mat[4], mat[1],
				mat[3], mat[5]);

		// Also update the clipping box

		if (sheet.isViewPage()) {
			AffineTransform inverse = null;
			try {
				inverse = user2Page.createInverse();
			} catch (NoninvertibleTransformException ex) {
				System.err.println("non invertible user 2 page transform");
				return;
			}
			inverse.getMatrix(mat);
			AffineTransform2D page2User = new AffineTransform2D(mat[0], mat[2],
					mat[4], mat[1], mat[3], mat[5]);

			Point2D p1 = new Point2D(), p2 = new Point2D();
			switch (clipping) {
			case PAGE:
				p1 = new Point2D(0, 0);
				p2 = new Point2D(dim.getWidth(), dim.getHeight());
				break;
			case MARGIN:
				EuclideSheet.Margin margin = sheet.getMargin();
				p1 = new Point2D(margin.left, margin.bottom);
				p2 = new Point2D(dim.getWidth() - margin.left - margin.right,
						dim.getHeight() - margin.top - margin.bottom);
				break;
			}
			p1 = p1.transform(page2User);
			p2 = p2.transform(page2User);
			clippingBox = new Box2D(p1, p2);
		} else {
			switch (clipping) {
			case VIEWBOX:
				clippingBox = sheet.getViewBox();
				break;
			case WINDOW:
				dim = this.getSize();
				Point2D p1 = new Point2D(0, 0);
				Point2D p2 = new Point2D(dim.getWidth(), dim.getHeight());
				AffineTransform2D inv = user2Display.invert();
				p1 = p1.transform(inv);
				p2 = p2.transform(inv);
				clippingBox = new Box2D(p1, p2);
				break;
			default:
				clippingBox = sheet.getViewBox();
			}

		}
	}

	@Override
	public void validate() {
		// System.out.println("validate EuclideSheetDisplay");
		this.updateUser2DisplayTransform();
		super.validate();
	}

	public void paintGrid(Graphics2D g) {

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (sheet.isViewPage()) {
			// Get the current transform
			AffineTransform saveAT = g2.getTransform();

			// Page-To-Display Transform
			g2.transform(page2Display);

			// draw the visible page
			Dimension dim = sheet.getDimension();
			Rectangle2D rect = new Rectangle2D(0, 0, dim.width, dim.height);
			g2.setStroke(new BasicStroke((float) (1/zoom)));
			g2.setColor(Color.WHITE);
			rect.fill(g2);
			g2.setColor(Color.BLACK);
			rect.draw(g2);

			// draw the margin
			if (drawMargin) {
				EuclideSheet.Margin margin = sheet.getMargin();
				Rectangle2D marginRect = new Rectangle2D(margin.left,
						margin.bottom, dim.width-margin.right-margin.left,
						dim.height-margin.top-margin.bottom);
				g2.setColor(Color.gray);
				marginRect.draw(g2);
			}

			g2.setTransform(saveAT);
		}

		// Draw sheet axes and grid
		// ---

		// Draw the grid
		if (sheet.isGridVisible()) {
			g2.setColor(Color.LIGHT_GRAY);
			g2.setStroke(new BasicStroke(0.1f));
			Box2D box = sheet.getViewBox();
			for (LineSegment2D line : sheet.getGrid().getEdges(box)) {
				line.transform(user2Display).draw(g2);
			}
		}

		// Draw axes
		if (sheet.isAxesVisible()) {
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(0.1f, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND));
			StraightLine2D lineOx = new StraightLine2D(0, 0, 1, 0);
			lineOx.clip(clippingBox).transform(user2Display).draw(g2);
			StraightLine2D lineOy = new StraightLine2D(0, 0, 0, 1);
			lineOy.clip(clippingBox).transform(user2Display).draw(g2);
		}

		// Draw the bounding box
		Polygon2D boundingRect = sheet.getViewBox().getAsRectangle();
		g2.setColor(Color.GRAY);
		boundingRect.transform(user2Display).draw(g2);

		// process each layer of the sheet
		for (EuclideLayer layer : sheet.getLayers()) {
			if (!layer.isVisible())
				continue;

			// process each element of the layer, in natural order
			for (EuclideFigure shape : layer.getShapes())
				paintEuclideShape(g2, shape);
		}
	}

	public void paintEuclideShape(Graphics2D g2, EuclideFigure element) {
		// extract dynamic geometry of the element
		DynamicShape2D dynamic = element.getGeometry();
		if (!(dynamic.isDefined()))
			return;

		// extract the geometry (an instance of Shape2D)
		Shape2D geometry = dynamic.getShape();

		// extract the drawing style
		DrawStyle style = element.getDrawStyle();

		paintShape(g2, geometry, style);
	}
	
	@SuppressWarnings("unchecked")
	private void paintShape(Graphics2D g2, Shape2D shape, DrawStyle drawStyle) {
		// different processing depending on the geometry class
		if (shape instanceof Point2D) {
			Point2D point = (Point2D) shape;
			this.paintPoint(g2, point, drawStyle);
		} else if (shape instanceof PointSet2D) {
			PointSet2D pointSet = (PointSet2D) shape;

			// clip the visible points
			pointSet = pointSet.clip(clippingBox);

			for (Point2D point : pointSet) {
				this.paintPoint(g2, point, drawStyle);
			}
		} else if (shape instanceof Label2D) {
			Label2D label = (Label2D) shape;
			Point2D pos = label.getPosition();
			if (!clippingBox.contains(pos))
				return;

			pos = pos.transform(user2Display);
			String text = label.getText();
			g2.setColor(Color.BLACK);
			g2.drawString(text, (float) pos.getX()+3, (float) pos.getY()-3);
			
		} else if (shape instanceof Domain2D) {
			this.paintDomain(g2, (Domain2D) shape, drawStyle);
		} else if (shape instanceof Curve2D) {
			this.paintCurve(g2, (Curve2D) shape, drawStyle);
		} else if (shape instanceof ShapeArray2D) {
			// Unchecked class cast
			for(Shape2D sha : (ShapeArray2D<? extends Shape2D>) shape) {
				this.paintShape(g2, sha, drawStyle);
			}
		} else {
			if (shape==null)
				System.err
						.println("[EuclideSheetDisplay] Euclide element with no geometry");
			else
				System.err.println("[EuclideSheetDisplay] Unknown shape type: "
						+shape.getClass());
		}
	}

	private void paintPoint(Graphics2D g2, Point2D point,
			DrawStyle drawStyle) {
		
		if (!clippingBox.contains(point))
			return;

		point = point.transform(user2Display);

		double x = point.getX();
		double y = point.getY();
		double s = drawStyle.getMarkerSize();
		if (drawStyle.getMarkerSizeUnit()==DrawStyle.MarkerSizeUnit.USER)
			s = s*this.zoom;
		
		AffineTransform2D sca = AffineTransform2D.createScaling(s, s);
		AffineTransform2D tra = AffineTransform2D.createTranslation(x, y);
		AffineTransform2D trans = sca.preConcatenate(tra);

		Marker marker = drawStyle.getMarker();
		
		// fill up the point interior
		if(marker.isFillable()) {
			Boundary2D boundary = marker.getFillBoundary();
			g2.setPaint(drawStyle.getMarkerFillColor());
			boundary.transform(trans).fill(g2);
		}

		// draw point outline
		Shape2D shape = marker.getShape();
		g2.setStroke(new BasicStroke((float) drawStyle.getMarkerLineWidth()));
		g2.setColor(drawStyle.getMarkerColor());
		shape.transform(trans).draw(g2);
	}

	private void paintCurve(Graphics2D g2, Curve2D curve,
			DrawStyle drawStyle) {
		// first check if the curve need to be drawn
		if (!drawStyle.isLineVisible())
			return;
		
		// extract visible geometry of element
		curve = curve.clip(clippingBox);
		if (curve.isEmpty())
			return;
		
		// Transform clipped curve to page axis
		curve = curve.transform(user2Display);
		if (curve.isEmpty())
			return;

		// Setup line style
		g2.setStroke(makeStroke(drawStyle, this.zoom));
		g2.setColor(drawStyle.getLineColor());

		// Draw the curve
		curve.draw(g2);
	}
	
	private void paintDomain(Graphics2D g2, Domain2D domain,
			DrawStyle drawStyle) {
		
		boolean fill = drawStyle.getFillType() != DrawStyle.FillType.NONE;
		
		// in case of polygon, check it is not degenerate
		if (domain instanceof Polygon2D) {
			if (((Polygon2D) domain).getArea() < Shape2D.ACCURACY)
				fill = false;
		}
		
		// extract visible geometry of element
		Domain2D clippedDomain = domain.clip(clippingBox);

		if (!clippedDomain.isEmpty() && fill) {
			clippedDomain = clippedDomain.transform(user2Display);
			// Fill the interior of the shape
			Paint paint = makePaint(drawStyle);
			if (paint!=null) {
				g2.setPaint(paint);
				clippedDomain.fill(g2);
			}
		}
		
		// Process domain boundary
		if (drawStyle.isLineVisible()) {
	
			// extract the boundary
			Boundary2D boundary = domain.getBoundary();
	
			// extract visible geometry of the boundary
			Curve2D clippedBoundary = boundary.clip(clippingBox);

			if (!clippedBoundary.isEmpty()) {
				clippedBoundary = clippedBoundary.transform(user2Display);
				// Draw the contour of the shape
				g2.setStroke(makeStroke(drawStyle, this.zoom));
				g2.setColor(drawStyle.getLineColor());

				clippedBoundary.draw(g2);
			}
		}
	}
	
	/**
	 * Sets up the transparency of a color.
	 * 
	 * @param color the base color
	 * @param alpha the wished transparency, between 0 and 255
	 * @return a color with same r g and b as "color", and with specified alpha
	 */
	protected static Color makeAlphaColor(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
				alpha);
	}

	/**
	 * Sets up the transparency of a color.
	 * 
	 * @param color the base color
	 * @param alpha the wished transparency, between 0 and 1
	 * @return a color with same r g and b as "color", and with specified alpha
	 */
	protected static Color makeAlphaColor(Color color, double alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
				(int) Math.round(alpha*255));
	}

	/**
	 * Create a BasicStroke implementation from a DrawStyle object. The factor
	 * argument gives the ratio between 1 dash in DrawStyle, and a dash of the
	 * stroke.
	 */
	protected static Stroke makeStroke(DrawStyle style, double zoom) {
		// curve width
		float width;
		if (style.getLineWidth()!=0)
			width = (float) style.getLineWidth();
		else
			width = 1.0f;

		if (style.getLineWidthUnit()==DrawStyle.LineWidthUnit.USER)
			width = (float) (width*zoom);

		// behavior at the end of the curve
		int cap = style.getLineEndCap().getJavaEndCap();

		// behavior at junctions between inner curves
		int join = style.getLineJoin().getJavaLineJoin();

		// dash
		float[] dash = style.getLineDash();
		float dashPhase = (float) (style.getLineDashPhase()*zoom);

		// create the stroke
		Stroke stroke;
		if (dash.length==0)
			stroke = new BasicStroke(width, cap, join);
		else {
			// convert the dash lengths according to zoom factor and line width
			float[] dash2 = new float[dash.length];
			for (int i = 0; i<dash.length; i++)
				dash2[i] = dash[i]*width;

			// create a stroke with scaled dash
			stroke = new BasicStroke(width, cap, join, 10f, dash2, dashPhase);
		}
		return stroke;
	}

	protected static Paint makePaint(DrawStyle style) {
		if (style.getFillType()==DrawStyle.FillType.COLOR) {
			return makeAlphaColor(
					style.getFillColor(), 
					style.getFillTransparency());
		} else {
			logger.warn(String.format(
					"Unknown fill style (%s), use color fill",
					style.getFillType()));
		}
		return style.getFillColor();
	}
}
