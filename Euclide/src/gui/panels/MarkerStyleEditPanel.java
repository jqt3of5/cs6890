/**
 * 
 */
package gui.panels;

import gui.EuclideGui;
import gui.EuclidePanel;
import gui.EuclideSheetView;
import gui.util.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;

import javax.swing.*;

import model.EuclideDoc;
import model.EuclideFigure;
import model.style.DefaultDrawStyle;
import model.style.DrawStyle;
import model.style.DrawStyle.MarkerSizeUnit;
import model.style.Marker;
import app.EuclideApp.AppColor;
import app.EuclideApp.AppMarker;
import app.EuclideApp.AppMarkerSizeUnit;

/**
 * @author dlegland
 *
 */
public class MarkerStyleEditPanel extends EuclidePanel {

	private static final long serialVersionUID = 1L;
		
	JLabel typeLabel 		= new JLabel("Type:");
	JLabel colorLabel 		= new JLabel("Color:");
	JLabel sizeLabel 		= new JLabel("Size:");
	JLabel sizeUnitLabel 	= new JLabel("Unit:");
	JLabel fillLabel 		= new JLabel("Fill color:");
	JLabel widthLabel 		= new JLabel("Line width:");
	
	JComboBox typeCombo 	= new JComboBox();
	JComboBox colorCombo 	= new JComboBox();
	JSpinner sizeSpinner;
	JComboBox sizeUnitCombo = new JComboBox();
	JComboBox fillCombo 	= new JComboBox();
	JSpinner widthSpinner;

	public MarkerStyleEditPanel(EuclideGui gui){
		super(gui);
		
		initializeWidgets();
		createLayout();
	}
	
	private void initializeWidgets() {
		// init the type combo
		// init the line join combo
		typeCombo.removeAllItems();
		for(AppMarker marker : AppMarker.values())
			typeCombo.addItem(marker);
		
		// init the marker color combo
		colorCombo.removeAllItems();
		for(AppColor color : AppColor.values())
			colorCombo.addItem(color);

		// init the size spinner
		double s0 = 2;
		double sMax = 1000;
        SpinnerModel sizeModel = new SpinnerNumberModel(s0, 0, sMax, 1);
        sizeSpinner = new JSpinner(sizeModel);
        sizeSpinner.setEditor(new JSpinner.NumberEditor(sizeSpinner, "#0.0"));
        
		// init the marker size unit combo
        sizeUnitCombo.removeAllItems();
		for(AppMarkerSizeUnit unit : AppMarkerSizeUnit.values())
			sizeUnitCombo.addItem(unit);

		// init the marker fill color combo
		fillCombo.removeAllItems();
		for(AppColor color : AppColor.values())
			fillCombo.addItem(color);

		double w0 = 1;
		double wMax = 100;
        SpinnerModel widthModel = new SpinnerNumberModel(w0, 0, wMax, 1);
        widthSpinner = new JSpinner(widthModel);
        widthSpinner.setEditor(new JSpinner.NumberEditor(widthSpinner, "#0.0"));		
	}
	
	private void createLayout() {
//        JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		
//		JPanel typePanel = new JPanel(new FlowLayout());
//		typePanel.add(typeLabel);
//		typePanel.add(typeCombo);
//		mainPanel.add(typePanel);
//		
//		JPanel colorPanel = new JPanel(new FlowLayout());
//		colorPanel.add(colorLabel);
//		colorPanel.add(colorCombo);
//		mainPanel.add(colorPanel);
//		
//		JPanel sizePanel = new JPanel(new FlowLayout());
//		sizePanel.add(sizeLabel);
//		sizePanel.add(sizeSpinner);
//		mainPanel.add(sizePanel);
//		
//		JPanel sizeUnitPanel = new JPanel(new FlowLayout());
//		sizeUnitPanel.add(sizeUnitLabel);
//		sizeUnitPanel.add(sizeUnitCombo);
//		mainPanel.add(sizeUnitPanel);
//		
//		JPanel fillPanel = new JPanel(new FlowLayout());
//		fillPanel.add(fillLabel);
//		fillPanel.add(fillCombo);
//		mainPanel.add(fillPanel);
//		
//		JPanel widthPanel = new JPanel(new FlowLayout());
//		widthPanel.add(widthLabel);
//		widthPanel.add(widthSpinner);
//		mainPanel.add(widthPanel);
//
//		this.setLayout(new BorderLayout());
//		this.add(mainPanel, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel(new SpringLayout());
		
		mainPanel.add(typeLabel);
		mainPanel.add(typeCombo);
		
		mainPanel.add(colorLabel);
		mainPanel.add(colorCombo);
		
		mainPanel.add(sizeLabel);
		mainPanel.add(sizeSpinner);
		
		mainPanel.add(fillLabel);
		mainPanel.add(fillCombo);
		
		mainPanel.add(sizeUnitLabel);
		mainPanel.add(sizeUnitCombo);
		
		mainPanel.add(widthLabel);
		mainPanel.add(widthSpinner);
		
		// Lay out the panel.
        SpringUtilities.makeCompactGrid(mainPanel, 
        		3, 4, 
        		6, 6, 6, 6);
        
        this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void updateWidgets(){
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		Collection<EuclideFigure> selection = view.getSelection();
		
		DrawStyle style;
		
		if(selection.isEmpty()){
			// update widgets from application defaults
			EuclideDoc doc = gui.getCurrentDoc();
			style = doc.getDrawStyle();
			updateWidgets(style);
			return;
		}

		// first init with style of first element in selection
		EuclideFigure shape = selection.iterator().next();
		style = shape.getDrawStyle();
				
		updateWidgets(style);
	}
	
	private void updateWidgets(DrawStyle style){
		// Set up marker type combo
		typeCombo.setSelectedItem(AppMarker.NO_CHANGE);
		if(style.getMarker()!=null)
			typeCombo.setSelectedItem(style.getMarker().toString());
		Marker marker = style.getMarker();
		for(AppMarker appMarker : AppMarker.values()){
			if(marker.equals(appMarker.getMarker())){
				fillCombo.setSelectedItem(appMarker);
				break;
			}
		}
	
		// set up marker color combo
		colorCombo.setSelectedItem(AppColor.NO_CHANGE); 
		Color color = style.getMarkerColor();
		for(AppColor appColor : AppColor.values()){
			if(color.equals(appColor.getColor())){
				colorCombo.setSelectedItem(appColor);
				break;
			}
		}
		
		// set up marker size spinner
		sizeSpinner.setValue(style.getMarkerSize());
		
		// set up marker fill combo
		sizeUnitCombo.setSelectedItem(AppMarkerSizeUnit.NO_CHANGE); 
		MarkerSizeUnit unit = style.getMarkerSizeUnit();
		for(AppMarkerSizeUnit appUnit : AppMarkerSizeUnit.values()){
			if(unit.equals(appUnit.getUnit())){
				sizeUnitCombo.setSelectedItem(appUnit);
				break;
			}
		}

		// set up marker fill combo
		fillCombo.setSelectedItem(AppColor.NO_CHANGE); 
		color = style.getMarkerFillColor();
		for(AppColor appColor : AppColor.values()){
			if(color.equals(appColor.getColor())){
				fillCombo.setSelectedItem(appColor);
				break;
			}
		}

		// set up marker line width spinner
		widthSpinner.setValue(style.getMarkerLineWidth());
	}
		
	/**
	 * Apply changes, either to the current selection, or to the default
	 * style of the document.
	 */
	@Override
	public void applyChanges(){
		
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		Collection<EuclideFigure> selection = view.getSelection();
		
		if(selection.isEmpty()){
			// update default marker style for appli
			EuclideDoc doc = gui.getCurrentDoc();
			DrawStyle style = updateMarkerStyle(doc.getDrawStyle());
			doc.setDrawStyle(style);
			return;
		}
		
		for(EuclideFigure item : selection){
			// update marker style of each element (creates a new instance for each)
			DrawStyle newStyle = updateMarkerStyle(item.getDrawStyle());
			item.setDrawStyle(newStyle);
		}
		
		view.repaint();
	}
	
	private DrawStyle updateMarkerStyle(DrawStyle style){
		DefaultDrawStyle newStyle = new DefaultDrawStyle(style);
		
		// update marker
		AppMarker appMarker = (AppMarker) typeCombo.getSelectedItem();
		if(appMarker != AppMarker.NO_CHANGE)
			newStyle.setMarker(appMarker.getMarker());
		
		// update marker color
		AppColor appColor = (AppColor) colorCombo.getSelectedItem();
		if(appColor != AppColor.NO_CHANGE)
			newStyle.setMarkerColor(appColor.getColor());
		
		// update marker size
		double size = ((Double)sizeSpinner.getValue()).doubleValue();
		if(size != Double.NaN)
			newStyle.setMarkerSize(size);			
		
		// update marker size unit
		AppMarkerSizeUnit appUnit = 
			(AppMarkerSizeUnit) sizeUnitCombo.getSelectedItem();
		if(appUnit != AppMarkerSizeUnit.NO_CHANGE)
			newStyle.setMarkerSizeUnit(appUnit.getUnit());

		// update marker fill color
		appColor = (AppColor) fillCombo.getSelectedItem();
		if(appColor != AppColor.NO_CHANGE)
			newStyle.setMarkerFillColor(appColor.getColor());

		// update marker line width
		double width = ((Double)widthSpinner.getValue()).doubleValue();
		if(width != Double.NaN)
			newStyle.setMarkerLineWidth(width);			
		
		// return the modified style
		return newStyle;
	}
}
