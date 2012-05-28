
/*
 * File : PaintOptionsPanel.java
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
 * author : Legland
 * Created on 4 janv. 2004
 */

package gui.panels;

import gui.EuclideGui;
import gui.EuclideSheetView;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.EuclideDoc;
import model.EuclideFigure;
import model.style.DefaultDrawStyle;
import model.style.DrawStyle;

/**
 * @author Legland
 */
public class PaintOptionsPanel extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Color[] colors = {
		null, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW,
		Color.WHITE, Color.GRAY, Color.BLACK		
	};
	
	private final static String[] colorStrings = {
		" ", "red", "green", "blue", "cyan", "magenta", "yellow", "white", "gray", "black"
	};
	
	private final static double[] widths = {Double.NaN, 1, 2, 3, 5, 10, 20};
	
	
	private final static String[] widthStrings = {
		" ", "1", "2", "3", "5", "10", "20"
	};
	
	JLabel titleLabel = new JLabel("Options :  ");
	JLabel strokeWidthLabel = new JLabel("width");
	JComboBox strokeWidthCombo = new JComboBox(widthStrings);
	JLabel strokeColorLabel = new JLabel("color");
	JComboBox strokeColorCombo = new JComboBox(colorStrings);
	JLabel strokePaintLabel = new JLabel("paint");
	JComboBox strokePaintCombo = new JComboBox(colorStrings);
	
	EuclideGui gui;
	 
	public PaintOptionsPanel(EuclideGui appli){
		gui = appli;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(titleLabel);
		add(strokeWidthLabel);
		add(strokeWidthCombo);
		add(strokeColorLabel);
		add(strokeColorCombo);
		add(strokePaintLabel);
		add(strokePaintCombo);
		
		//updateState();
		
		strokeWidthCombo.addActionListener(this);
		strokeColorCombo.addActionListener(this);
		strokePaintCombo.addActionListener(this);
	}
	
	/**
	 * Set up widgets according to selection.
	 */
	public void updateState(){
		
		EuclideDoc doc = gui.getCurrentDoc();
		Collection<EuclideFigure> selection = gui.getCurrentView().getCurrentSheetView().getSelection();

		if(selection.size()==0){
			// uses default values of application
			double width = doc.getDrawStyle().getLineWidth();			
			for(int i=0; i<widths.length; i++)
				if(Math.abs(width-widths[i])<1e-8)
					strokeWidthCombo.setSelectedIndex(i);
		
			Color color = doc.getDrawStyle().getLineColor();
			for(int i=0; i<colors.length; i++)
				if(color==colors[i])  strokeColorCombo.setSelectedIndex(i);

			Paint paint = doc.getDrawStyle().getFillColor();
			for(int i=0; i<colors.length; i++)
				if(paint==colors[i])  strokePaintCombo.setSelectedIndex(i);
			
			return;
		}
		
		
//		// initialize with "empty" strings
//		strokeWidthCombo.setSelectedIndex(0);
//		strokeColorCombo.setSelectedIndex(0);
//		strokePaintCombo.setSelectedIndex(0);
//		
//		EuclideShape first = selection.iterator().next();
//		double width = first.getLineStyle().getStrokeWidth();
//		Color color = first.getLineStyle().getStrokeColor();
//		Paint paint = first.getFillStyle().getPaint();
//		boolean sameStrokeWidth = true;
//		boolean sameStrokeColor = true;
//		boolean sameFillColor = true;
//		
//		for(EuclideShape elem : selection){
//			if(Math.abs(elem.getLineStyle().getStrokeWidth()-width)>1e-8)
//				sameStrokeWidth = false;
//			if(elem.getLineStyle().getStrokeColor()!=color)
//				sameStrokeColor = false;
//			if(elem.getFillStyle().getFillColor()!=paint)
//				sameFillColor = false;
//		}
//		
//		if(sameStrokeWidth)
//			for(int i=0; i<widths.length; i++)
//				if(Math.abs(width-widths[i])<1e-8)
//					strokeWidthCombo.setSelectedIndex(i);
//		if(sameStrokeColor)
//			for(int i=0; i<colors.length; i++)
//				if(color==colors[i])  strokeColorCombo.setSelectedIndex(i);
//		if(sameFillColor)
//			for(int i=0; i<colors.length; i++)
//				if(paint==colors[i])  strokePaintCombo.setSelectedIndex(i);
	}
	
	public void actionPerformed(ActionEvent evt){
		
		if((evt.getModifiers() & AWTEvent.MOUSE_EVENT_MASK)==0){
			System.out.println("non mouse action");
			return;
		}else{
			System.out.println("mouse action");
		}
		
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		Collection<EuclideFigure> selection = view.getSelection();
		System.out.println("action performed in Paint options panel");
		
		EuclideDoc doc = gui.getCurrentDoc();
		DefaultDrawStyle newStyle;
		
		if(evt.getSource()==strokeWidthCombo){
			float width = (float) widths[strokeWidthCombo.getSelectedIndex()];
			if(selection.size()==0){
				// update line style
				newStyle = new DefaultDrawStyle(doc.getDrawStyle());
				newStyle.setLineWidth(width);
				doc.setDrawStyle(newStyle);
			}else for(EuclideFigure elem : selection){
				// update line style
				newStyle = new DefaultDrawStyle(elem.getDrawStyle());
				newStyle.setLineWidth(width);
				newStyle.setMarkerLineWidth(width);
				elem.setDrawStyle(newStyle);
			}
		}
		
		if(evt.getSource()==strokeColorCombo){
			Color color = colors[strokeColorCombo.getSelectedIndex()];
			if(selection.size()==0){
				// update line style
				newStyle = new DefaultDrawStyle(doc.getDrawStyle());
				newStyle.setLineColor(color);
				doc.setDrawStyle(newStyle);
			}else for(EuclideFigure elem : selection){				
				// update line style
				newStyle = new DefaultDrawStyle(elem.getDrawStyle());
				newStyle.setLineColor(color);
				elem.setDrawStyle(newStyle);
			}
		}
		if(evt.getSource()==strokePaintCombo){
			int index = strokePaintCombo.getSelectedIndex();
			
			if(index==0){
				if(selection.size()==0){
					newStyle = new DefaultDrawStyle(doc.getDrawStyle());
					newStyle.setFillType(DrawStyle.FillType.NONE);
					doc.setDrawStyle(newStyle);
				}else for(EuclideFigure elem : selection){
					// update fill style
					newStyle = new DefaultDrawStyle(elem.getDrawStyle());
					newStyle.setFillType(DrawStyle.FillType.NONE);
					elem.setDrawStyle(newStyle);
				}
			}else{
				Color color = colors[index];
				if(selection.size()==0){
					newStyle = new DefaultDrawStyle(doc.getDrawStyle());
					newStyle.setFillType(DrawStyle.FillType.COLOR);
					newStyle.setFillColor(color);
					doc.setDrawStyle(newStyle);
				}else for(EuclideFigure elem : selection){
					// update fill style
					newStyle = new DefaultDrawStyle(doc.getDrawStyle());
					newStyle.setFillType(DrawStyle.FillType.COLOR);
					newStyle.setFillColor(color);
					elem.setDrawStyle(newStyle);
				}
			}
		}
		
		//if(elem!=null)
		//	gui.getCurrentView().getDocument().setModified(true);
		
		gui.getCurrentView().repaint();
	}
}
