/* file : EuclideSheetView.java
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
 * Created on 2 janv. 2006
 *
 */
package gui;


import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom2d.AffineTransform2D;
import math.geom2d.CountMeasure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.CircularShape2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.grid.Grid2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.point.PointSet2D;
import math.geom2d.point.PointShape2D;
import math.geom2d.polygon.Polygon2D;
import model.EuclideLayer;
import model.EuclideFigure;
import model.EuclideSheet;
import app.EuclideApp;
import dynamic.DynamicShape2D;
import dynamic.measures.Constant2D;
import dynamic.shapes.*;

/**
 * An extension of EuclideSheetDisplay, which allows management of
 * interactivity (mouse label, grid snap...).
 * @author dlegland
 */
public class EuclideSheetView extends EuclideSheetDisplay {


	// ===================================================================
	// static variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * The stroke used to draw selected shapes.
	 */
	protected final static Stroke selectionStroke = new BasicStroke(1f, 
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	// ===================================================================
	// class variables
	
	/**
	 * The distance, in pixels, for which the cursor is over a shape.
	 */
	private double pixelSnap = 3;
	
	/**
	 * The label to be displayed close to the mouse cursor
	 */
	String mouseString 	= new String("");
	
	/**
	 * The position of the mouse cursor, in pixels
	 */
	Point mousePosition = new Point(100, 100);
	Point2D mousePositionInUserUnit = new Point2D();
	
	/**
	 * The collection of selected shapes. If not empty, they can be drawn with
	 * special display.
	 * @param doc
	 */
	protected ArrayList<EuclideFigure> selection = new ArrayList<EuclideFigure>();
	
	protected ArrayList<EuclideFigure> previewShapes = new ArrayList<EuclideFigure>();
	
	
	// ===================================================================
	// Constructor
	
	/**
	 * main constructor, needs to specify a sheet to be valid.
	 */
	public EuclideSheetView(EuclideSheet sheet) {
		super(sheet);
	}


	public double pixelToUserUnit (double pixel) {
		return pixel / this.zoom / this.dpu;
	}
	
	public double userUnitToPixel (double userUnit) {
		return userUnit * this.zoom * this.dpu;
	}


	// ===================================================================
	// snap distance
	
	/**
	 * Returns the actual snap distance (in user unit) between a shape and
	 * a point, depending on the pixel snap number, the zoom, and the scale
	 * of the document.
	 */
	public double getSnapDistance(){
		return this.pixelSnap / this.zoom / this.dpu;
	}

	/**
	 * Returns the number of pixels needed to snap a shape.
	 * @return the number of pixels needed to snap a shape.
	 */
	public double getPixelSnap(){
		return this.pixelSnap;
	}
	
	/**
	 * Set up the number of pixels needed to snap a shape.
	 * @param dist the new number of pixels used for snapping shapes
	 */
	public void setPixelSnap(double dist){
		this.pixelSnap = dist;
	}

	/**
	 * Returns true if the given point is close enough to the given shape.
	 * @param point a point
	 * @param shape a shape
	 * @return true if the point is below the snapping limit
	 */
	public boolean isSnapped(Point2D point, Shape2D shape){
		double dist = shape.getDistance(point);
		return dist<getSnapDistance();
	}

	public void setMouseLabel(int x, int y, String text){
		mousePosition.setLocation(x, y);
		mouseString = text;
	}
	
	public void setMouseLabel(String text){
		mouseString = text;
	}
	
	public void setMousePosition(int x, int y){
		mousePosition.setLocation(x, y);
	}

	// ===================================================================
	// Selection management
	
	/**
	 * get selected elements in this view.
	 * @return the selected elements
	 */
	public Collection<EuclideFigure> getSelection(){
		return selection;
	}
	
	/**
	 * clear list of selected items. This has no effects on the items themselves.
	 */
	public void clearSelection(){
		selection.clear();
	}
	
	public void addToSelection(EuclideFigure elem){
		if (!selection.contains(elem))
			selection.add(elem);		
	}
	
	public void removeFromSelection(EuclideFigure elem){
		selection.remove(elem);
	}

	// ===================================================================
	// management of shape preview
	
	/**
	 * get selected elements in this view.
	 * @return the selected elements
	 */
	public Collection<EuclideFigure> getPreview(){
		return previewShapes;
	}
	
	/**
	 * clear list of selected items. This has no effects on the items themselves.
	 */
	public void clearPreview(){
		previewShapes.clear();
	}
	
	public void addToPreview(EuclideFigure elem){
		if (!previewShapes.contains(elem))
			previewShapes.add(elem);		
	}
	
	public void removeFromPreview(EuclideFigure elem){
		previewShapes.remove(elem);
	}

	
	// ===================================================================
	// general use functions
	
	/**
	 * Convert display coordinates (in pixels) in user coordinate (mm), and eventually
	 * toggle grid and/or item snap.
	 * @param x x position
	 * @param y y position
	 * @param toggleGridSnap toggle the snap mode of the view to the grid
	 * @param toggleGridSnap toggle the snap mode of the view to the items
	 * @return the position of the point in user space
	 */
	public Point2D getPosition(double x, double y, boolean snapToGrid,
			boolean snapToItem) {
		
		AffineTransform2D inv = user2Display.invert();
		Point2D res = new Point2D(x, y).transform(inv);
		
		// Snap to grid if needed
		if (snapToGrid){
			Grid2D grid = sheet.getSnapGrid();
			if (grid != null)
				res = grid.getClosestVertex(res);
		}
		return res;
	}
	
	public Point2D getPosition(double x, double y){
		return getPosition(x, y, false, false);
	}
	
	/**
	 * Same as getClosestElement, but return element only if less than default
	 * snap distance.
	 * @param point a location
	 * @param geometry a class which inherits Shape2D
	 * @return a set of EuclideShape whose shapes are instances of geometry
	 */
	public EuclideFigure getSnappedShape(Point2D point, Class<? extends Shape2D> shapeClass){
		// First try to find a point if possible
		if (shapeClass.isAssignableFrom(Point2D.class)) {
			EuclideFigure shape = this.getClosestShape(point, Point2D.class);
			if (shape != null){
				Shape2D geometry = shape.getGeometry().getShape();
				if (isClose(geometry, point))
					return shape;
			}
		}
		
		// Otherwise try to find the closest curve
		if (shapeClass.isAssignableFrom(Curve2D.class)) {
			EuclideFigure shape = this.getClosestShape(point, Curve2D.class);
			if (shape != null){
				Shape2D geometry = shape.getGeometry().getShape();
				if (isClose(geometry, point))
					return shape;
			}
		}
		
		// try with any kind of shape
		EuclideFigure shape = this.getClosestShape(point, shapeClass);
		if (shape != null){
			Shape2D geometry = shape.getGeometry().getShape();
			if (isClose(geometry, point))
				return shape;
		}
		
		// If no shape was found close enough, return null
		return null;
	}
	
	public EuclideFigure getClosestShape(Point2D point, 
			Class<? extends Shape2D> geometry){
		EuclideFigure closeShape = null;
		double dist, minDist = Double.POSITIVE_INFINITY;
		Shape2D geom;
				
		// iterate on each layer of the sheet
		for (EuclideLayer layer : sheet.getLayers()){
			// check only visible layers
			if (!layer.isVisible()) continue;
			
			// iterate on each element of the current layer
			for (EuclideFigure shape : layer.getShapes()){	
				// Extract geometry of dynamic shape, and check validity 
				DynamicShape2D dynamic = shape.getGeometry();
				if (!(dynamic.isDefined())) continue;
				geom = dynamic.getShape();
				
				// check geometry of current element
				if (geometry.isInstance(geom)){					
					// if distance is smaller than previous, keep element
					dist = geom.getDistance(point);
					if (dist<minDist){
						closeShape = shape;
						minDist = dist;
					}
				}
			}
		}
		return closeShape;
	}


	/**
	 * Returns a collection of elements whose distance to the given point is
	 * less than the specified distance, and whose class extends the specified
	 * class. 
	 * @param point a location
	 * @param geometry a class which inherits Shape2D
	 * @param minDist the minimum distance between a shape and the point
	 * @return a set of EuclideShape whose shapes are instances of geometry
	 */
	public Collection<EuclideFigure> getCloseShapes(Point2D point, 
			Class<? extends Shape2D> geometry, double minDist){
		ArrayList<EuclideFigure> shapes = new ArrayList<EuclideFigure>();
		double dist;
		Shape2D geom;
				
		// iterate on each layer of the sheet
		for (EuclideLayer layer : sheet.getLayers()){
			// check only visible layers
			if (!layer.isVisible()) continue;
			
			// iterate on each element of the current layer
			for (EuclideFigure shape : layer.getShapes()){
				// Extract the dynamic shape, and check it is defined 
				DynamicShape2D dynamic = shape.getGeometry();
				if (!(dynamic.isDefined())) continue;
				
				// extract geometry, and check it is an instance of specified geometry
				geom = dynamic.getShape();
				if (!geometry.isInstance(geom))
					continue;
				
				// if distance is smaller than threshold, keep element
				dist = geom.getDistance(point);
				if (dist<minDist)
					shapes.add(shape);		
			}
		}
		
		// return the set of elements close enough to the point
		return shapes;
	}
	
	/**
	 * Calls the method "createNewDynamicPoint", and creates a new
	 * EuclideShape with the resulting geometry.
	 */
	public EuclideFigure createNewPoint(Point2D point, EuclideApp appli) {
		return appli.createEuclideShape(createNewDynamicPoint(point, appli));
	}
	
	/**
	 * Returns a new free point, or creates a points from existing shapes.
	 * If two circles intersect close to the point, return a new
	 * Intersection2Circles2D object.
	 * If two lines intersect close to the point, return a new
	 * Intersection2Lines2D object.
	 * If distance to closest curve is 'small enough', create a PointOnLine or
	 * PointOnCircle object, initialized with position given as parameter.
	 * Otherwise, return a new Free point. 
	 * @param point a clicked point
	 * @param view the current view which was 'clicked'
	 * @return a new dynamic point, either free or on a curve
	 */
	public DynamicShape2D createNewDynamicPoint(Point2D point, EuclideApp appli) {
		//TODO: should be possible to rewrite in order to avoid multiple iterations over shapes
		// creates a dynamic shape for being able to call geometry constructors
		DynamicShape2D freePoint = new ShapeWrapper2D(point);

		// The distance of snapping
		double snap = pixelSnap/this.zoom/this.dpu;
		
		// Get the collection of lines close to the mouse
		Collection<EuclideFigure> pointSets = 
			this.getCloseShapes(point, PointSet2D.class, snap);

		// try to find a point set with one point element close to clicked
		// point
		if (pointSets.size() > 0) {
			// get the first found point set
			Iterator<EuclideFigure> iterator = pointSets.iterator();
			DynamicShape2D dynamic = iterator.next().getGeometry();
			PointSet2D set = (PointSet2D) dynamic.getShape();
			
			// find the index of the closest point
			double dist;
			double minDist = Double.MAX_VALUE;
			int i = 0;
			int index = 0;
			for (Point2D setPoint : set) {
				dist = setPoint.getDistance(point);
				if (dist < minDist) {
					index = i;
					minDist=dist;
				}
				i++;
			}
			
			// create dynamic shape selecting a point in a set
			return new PointPointSet2D(dynamic, index);
		}
		
		// Get the collection of lines close to the mouse
		Collection<EuclideFigure> lines = 
			this.getCloseShapes(point, LinearShape2D.class, snap);
		
		// try to find an intersection of lines
		if (lines.size() > 1){
			// extract lines
			Iterator<EuclideFigure> iterator = lines.iterator();
			DynamicShape2D line1 = iterator.next().getGeometry();
			DynamicShape2D line2 = iterator.next().getGeometry();
			
			// create intersection
			return new Intersection2StraightObjects2D(line1, line2);
		}

		// Get the collection of circles (or circle arcs) close to the mouse
		Collection<EuclideFigure> circles = 
			this.getCloseShapes(point, CircularShape2D.class, snap);
		
		// try to find an intersection of circles
		if (circles.size() > 1){
			// extract the 2 circles
			Iterator<EuclideFigure> iterator = circles.iterator();
			DynamicShape2D circle1 = iterator.next().getGeometry();
			DynamicShape2D circle2 = iterator.next().getGeometry();
			
			// compute intersections
			DynamicShape2D inter1 = 
				new Intersection2Circles2D(circle1, circle2, 0);
			DynamicShape2D inter2 = 
				new Intersection2Circles2D(circle1, circle2, 1);
			
			// keep the closest intersection
			DynamicShape2D res = closestPoint(point, inter1, inter2);
			if (res != null)
				return res;
		}

		// Try to find an intersection between a line and a circle
		
		if (lines.size() > 0 && circles.size() > 0){
			// extract line and circle
			DynamicShape2D line = lines.iterator().next().getGeometry();
			DynamicShape2D circle = circles.iterator().next().getGeometry();
			
			// compute the two intersections
			DynamicShape2D inter1 = 
				new IntersectionLineCircle2D(line, circle, 0);
			DynamicShape2D inter2 = 
				new IntersectionLineCircle2D(line, circle, 1);
			
			// keep the closest intersection
			DynamicShape2D res = closestPoint(point, inter1, inter2);
			if (res != null)
				return res;
		}
				
		// try to find the vertex of a polygon
		
		// find close polygons
		Collection<EuclideFigure> polygons = 
			this.getCloseShapes(point, Polygon2D.class, snap);
		if (polygons.size() > 0){
			DynamicShape2D 	closestShape=null;
			Point2D 		closestVertex = null;
			double dist;
			double minDist = Double.MAX_VALUE;
			int num = 0;
			
			for (EuclideFigure shape : polygons){
				Polygon2D polygon = (Polygon2D) shape.getGeometry().getShape();
				int i = 0;
				for (Point2D vertex : polygon.getVertices()){
					dist = vertex.getDistance(point);
					if (dist < minDist){
						minDist = dist;
						closestShape = shape.getGeometry();
						closestVertex = vertex;
						num = i;
					}
					i++;
				}
			}
			
			if (closestVertex.getDistance(point) < snap){
				return new PolygonVertex2D(closestShape, num);
			}
		}
		
		
		// add intersection of a line with a curve
		Collection<EuclideFigure> curves = 
			this.getCloseShapes(point, Curve2D.class, snap);
		if (lines.size() > 0 && curves.size() > 0){
			DynamicShape2D lineShape = lines.iterator().next().getGeometry();
			
			DynamicShape2D curveShape = null;
			for (EuclideFigure shape : curves){
				if (shape.getGeometry().getShape() instanceof LinearShape2D)
					continue;
				curveShape = shape.getGeometry();
				break;
			}
			
			if (curveShape != null){
				Curve2D 		curve = (Curve2D) curveShape.getShape();
				LinearShape2D 	line  = (LinearShape2D) lineShape.getShape();
				
				int i = 0;
				int indexClosest = 0;
				double dist;
				double minDist = Double.MAX_VALUE;
				
				for (Point2D inter : curve.getIntersections(line)){
					dist = inter.getDistance(point);
					if (dist < minDist){
						// add 1 because Euclide app. counts from 1 to N.
						indexClosest = i+1;
						minDist = dist;
					}
					i++;
				}
				
				return new IntersectionLineCurveIndex2D(
						lineShape,
						curveShape,
						new Constant2D(new CountMeasure2D(indexClosest)));
			}
		}
		
		// Check if a curve is close enough to the clicked point
		EuclideFigure curveShape = this.getSnappedShape(point, Curve2D.class);
		if (curveShape != null){
			return new PointOnCurve2D(curveShape.getGeometry(),
					freePoint);
		}
		
		// try to create a point on the boundary of a domain
		EuclideFigure domainItem = this.getSnappedShape(point, Domain2D.class);
		if (domainItem != null) {
			Domain2D domain = (Domain2D) domainItem.getGeometry().getShape();
			Boundary2D boundary = domain.getBoundary();
			if (this.isSnapped(point, boundary)) {
				return new PointOnBoundary2D(domainItem.getGeometry(),
						freePoint);
			}
		}

		// If no curve was found, create a new free point
		
		// snap to grid if needed
		if (sheet.isSnapToGrid()) {
			Grid2D grid = sheet.getSnapGrid();
			if (grid != null)
				point = grid.getClosestVertex(point);
		}
		
		// create the free point
		return new FreePoint2D(point.getX(), point.getY());
	}
	
	/**
	 * Tries to snap a shape, either a point, or if not found, a curve.
	 */
	public EuclideFigure snapShape(Point2D point) {
		// initialize to null
		EuclideFigure item = null;
		
		// first try to snap a point
		item = this.getSnappedShape(point, Point2D.class);
		
		// Then try to snap a curve
		if (item == null) 
			item = this.getSnappedShape(point, Curve2D.class);
		
		// return result
		return item;
	}
	
	private final static DynamicShape2D closestPoint(Point2D point, 
			DynamicShape2D dyn1, DynamicShape2D dyn2) {
		Point2D point1 = (Point2D) dyn1.getShape();
		Point2D point2 = (Point2D) dyn2.getShape();

		// keep the closest point
		if (point1 != null && point2 != null) {
			if (point1.getDistance(point) < point2.getDistance(point))
				return dyn1;
			else
				return dyn2;
		}
		return null;
	}
	
	/**
	 * Check if the shape is close enough from a given point, with respect
	 * to the current zoom factor of the view.
	 * @param shape a geometric shape
	 * @param point a point
	 * @return true if the shape is close enough to the point
	 */
	public boolean isClose(Shape2D shape, Point2D point){
		double d = shape.getDistance(point);
		return d < pixelSnap / this.zoom / this.dpu;
	}
	
	
	// ===================================================================
	// general functions
	
	/**
	 * Display some information on current tool.
	 * @param instruction
	 */
	public void setInstruction(String instruction){
		EuclideDocView view = getEuclideView();
		view.setToolInstruction(instruction);
	}
	
	private EuclideDocView getEuclideView() {
		Container contain = this;

		while (true) {
			contain = contain.getParent();
			if (contain == null)
				return null;
			if (contain instanceof EuclideDocView)
				return (EuclideDocView) contain;
		}
	}


	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);		
		
		Graphics2D g2 = (Graphics2D) g;
		
		this.paintSelectedShapes(g2);		

		this.paintPreviewShapes(g2);		

		this.paintMouseLabel(g2);
	}
	
	public void paintMouseLabel(Graphics2D g2) {
		// Get the current transform
		AffineTransform saveAT = g2.getTransform();		
		
		// Display the current mouse text
		if (mouseString != null)
			if (mouseString.length() > 0){
				Point2D point = mousePositionInUserUnit.transform(user2Display);
				int x = (int) point.getX() + 5;
				int y = (int) point.getY() - 5;
				
				// get the length of the text
				Font font = g2.getFont();
				FontRenderContext frc = g2.getFontRenderContext();
				TextLayout layout = new TextLayout(mouseString, font, frc);
				java.awt.geom.Rectangle2D bounds = layout.getBounds();
				int width = (int) bounds.getWidth();
				int height = (int) bounds.getHeight();
				
				// draw the label
				g2.setPaint(new Color(255, 255, 200));
				g2.fillRect(x - 2, y - height - 5, width + 7, height + 7);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(1f));
				g2.drawRect(x - 2, y - height - 6, width + 7, height + 7);
				g2.drawString(mouseString, x + 2, y - 2);
			}

		g2.setTransform(saveAT);
	}
	
	/**
	 * Draw Selected elements
	 * @param g the graphics to draw in.
	 */
	public void paintSelectedShapes(Graphics2D g2){
		
		Shape2D geometry;		
		
		Stroke outlineStroke = new BasicStroke(5.0f, 
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Area area = new Area();
		
		for(EuclideFigure item : selection){
			DynamicShape2D dynamic = item.getGeometry();
			if(!(dynamic.isDefined())) continue;
			geometry = dynamic.getShape();
			
			if(geometry instanceof PointShape2D){
			    PointShape2D points =
			        ((PointShape2D) geometry).clip(clippingBox);
			    for(Point2D point : points) {
			        point = point.transform(user2Display);
			        double xc = point.getX();
			        double yc = point.getY();
					double r = item.getDrawStyle().getMarkerSize() + 3;
			        area.add(new Area(new java.awt.geom.Ellipse2D.Double(
			                xc-r, yc-r, 2*r, 2*r)));
			    }
			} else if (geometry instanceof Domain2D) {
				// extract visible geometry of the geometry
				geometry = geometry.clip(clippingBox);
				geometry = geometry.transform(user2Display);
				Shape shape = createAWTShape(geometry);
				area.add(new Area(outlineStroke.createStrokedShape(
						new Area(shape))));
			} else{
				// extract visible geometry of the geometry					
				geometry = geometry.clip(clippingBox);
				geometry = geometry.transform(user2Display);
				Shape shape = createAWTShape(geometry);
				area.add(new Area(outlineStroke.createStrokedShape(shape)));
			}
		}
		g2.setColor(Color.BLUE);
		g2.setStroke(selectionStroke);

		// draw selection rectangle
		g2.draw(area);		
	}
	
	private final static Shape createAWTShape(Shape2D geometry){
	    GeneralPath path = new GeneralPath();
	    Curve2D curve;
	    
	    // extract the curve
        if (geometry instanceof Curve2D) {
            curve = (Curve2D) geometry;
	    } else if (geometry instanceof Domain2D) {
	        curve = ((Domain2D) geometry).getBoundary();
	    } else {
	        System.err.println("unknown shape type");
	        return null;
	    }
	    
	    // Convert curve to path
        Point2D point;
        for(ContinuousCurve2D cont : curve.getContinuousCurves()){
            // move to first point
            point = cont.getFirstPoint();
            path.moveTo(point.getX(), point.getY());
            
            //  append the rest of the path
            cont.appendPath(path);
            
            if (geometry instanceof Domain2D) {
                path.closePath();
            }
        }
        
        return path;
	}
	
	public void paintPreviewShapes(Graphics2D g2){
		for (EuclideFigure shape : previewShapes){
			DynamicShape2D dynamic = shape.getGeometry();
			if (!(dynamic.isDefined())) continue;
			paintEuclideShape(g2, shape);
		}
	}
}
