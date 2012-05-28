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
import model.style.DrawStyle.FillType;
import app.EuclideApp.AppColor;
import app.EuclideApp.AppFillType;

/**
 * @author dlegland
 *
 */
public class FillStyleEditPanel extends EuclidePanel {

	private static final long serialVersionUID = 1L;

	JLabel typeLabel 	= new JLabel("Type:");
	JLabel colorLabel 	= new JLabel("Color:");
	JLabel alphaLabel 	= new JLabel("Opacity:");
	
	JComboBox typeCombo 	= new JComboBox();
	JComboBox colorCombo 	= new JComboBox();
	JSpinner alphaSpinner 	= new JSpinner();

	public FillStyleEditPanel(EuclideGui gui){
		super(gui);
		
		initializeWidgets();
        createLayout();
//        JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		
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
//		JPanel alphaPanel = new JPanel(new FlowLayout());
//		alphaPanel.add(alphaLabel);
//		alphaPanel.add(alphaSpinner);
//		mainPanel.add(alphaPanel);
//
//		this.setLayout(new BorderLayout());
//		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	private void initializeWidgets() {
		// init the fill type combo
		typeCombo.removeAllItems();
		for(AppFillType type : AppFillType.values())
			typeCombo.addItem(type);
		
		// init the fill color combo
		colorCombo.removeAllItems();
		for(AppColor color : AppColor.values())
			colorCombo.addItem(color);

		// init the size spinner
		double a0 = 100;
        SpinnerModel alphaModel = new SpinnerNumberModel(a0, 0, 100, 10);
        alphaSpinner = new JSpinner(alphaModel);
        alphaSpinner.setEditor(new JSpinner.NumberEditor(alphaSpinner, "#0.0"));		
	}
	
	private void createLayout() {
		JPanel mainPanel = new JPanel(new SpringLayout());
	
		mainPanel.add(typeLabel);
		mainPanel.add(typeCombo);
		
		mainPanel.add(colorLabel);
		mainPanel.add(colorCombo);
		
		mainPanel.add(alphaLabel);
		mainPanel.add(alphaSpinner);
		
		// Lay out the panel.
        SpringUtilities.makeCompactGrid(mainPanel, 
        		3, 2, 
        		6, 6, 6, 6);
        
        this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void updateWidgets(){
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		Collection<EuclideFigure> selection = view.getSelection();
		
		if(selection.isEmpty()){
			// update widgets from application defaults
			EuclideDoc doc = gui.getCurrentDoc();
			DrawStyle style = doc.getDrawStyle();
			
			updateWidgets(style);
			return;
		}

		// first init with style of first element in selection
		DrawStyle style = selection.iterator().next().getDrawStyle();
		
		updateWidgets(style);
	}
	
	private void updateWidgets(DrawStyle style) {
		// select the "no change" option by default,
		// otherwise try to find element fill type
		typeCombo.setSelectedItem(AppFillType.NO_CHANGE); 
		FillType fillType = style.getFillType();
		for(AppFillType appFill : AppFillType.values()){
			if(fillType.equals(appFill.getFillType())){
				typeCombo.setSelectedItem(appFill);
				break;
			}
		}
				
		// select the "no change" option by default,
		// otherwise try to find element fill color
		colorCombo.setSelectedItem(AppColor.NO_CHANGE); 
		Color color = style.getFillColor();
		for(AppColor appColor : AppColor.values()){
			if(color.equals(appColor.getColor())){
				colorCombo.setSelectedItem(appColor);
				break;
			}
		}
		
		double alpha = style.getFillTransparency()*100;
		alphaSpinner.setValue(alpha);
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
			DrawStyle fillStyle = updateFillStyle(doc.getDrawStyle());
			doc.setDrawStyle(fillStyle);
			return;
		}
		
		for(EuclideFigure elem : selection){
			// update marker style of each element (creates a new instance for each)
			DefaultDrawStyle drawStyle = new DefaultDrawStyle(elem.getDrawStyle());
			elem.setDrawStyle(updateFillStyle(drawStyle));
		}
		
		view.repaint();
	}
	
	private DrawStyle updateFillStyle(DrawStyle style){
		DefaultDrawStyle newStyle = new DefaultDrawStyle(style);
		
		// update fill type
		AppFillType appFillType = (AppFillType) typeCombo.getSelectedItem();
		if(appFillType != AppFillType.NO_CHANGE)
			newStyle.setFillType(appFillType.getFillType());
		
		// update fill color
		AppColor appColor = (AppColor) colorCombo.getSelectedItem();
		if(appColor != AppColor.NO_CHANGE)
			newStyle.setFillColor(appColor.getColor());
		
		// update marker transparency
		double alpha = ((Double)alphaSpinner.getValue()).doubleValue();
		if(alpha != Double.NaN)
			newStyle.setFillTransparency(alpha/100);			
		
		// return the modified style
		return newStyle;
	}
}
