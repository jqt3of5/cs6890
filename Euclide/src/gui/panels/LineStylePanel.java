/**
 * 
 */
package gui.panels;

import gui.EuclideGui;
import gui.EuclidePanel;
import gui.EuclideSheetView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Collection;

import javax.swing.*;

import model.EuclideDoc;
import model.EuclideFigure;
import model.style.DefaultDrawStyle;
import model.style.DrawStyle;
import model.style.DrawStyle.EndCap;
import model.style.DrawStyle.LineJoin;
import model.style.DrawStyle.LineWidthUnit;
import app.EuclideApp.*;

/**
 * @author dlegland
 * @deprecated use LineStyleEditPanel instead (0.6.3)
 */
@Deprecated
public class LineStylePanel extends EuclidePanel {

	private static final long serialVersionUID = 1L;

	JCheckBox visibilityCheckbox = new JCheckBox("Visible", true);
	
	JLabel widthLabel 		= new JLabel("Line width:");
	JLabel widthUnitLabel 	= new JLabel("Unit:");
	JLabel colorLabel 		= new JLabel("Color:");
	JLabel joinLabel 		= new JLabel("Line join:");
	JLabel capLabel 		= new JLabel("Line end cap:");
	JLabel dashLabel 		= new JLabel("Dash:");
	JLabel phaseLabel 		= new JLabel("Phase:");
	
	JComboBox colorCombo 		= new JComboBox();
	JSpinner widthSpinner;
	JComboBox widthUnitCombo 	= new JComboBox();
	JComboBox joinCombo 		= new JComboBox();
	JComboBox capCombo 			= new JComboBox();
	JComboBox dashCombo 		= new JComboBox();
	JSpinner phaseSpinner;

	public LineStylePanel(EuclideGui gui){
		super(gui);
		
		// init the line color combo
		colorCombo.removeAllItems();
		for(AppColor color : AppColor.values())
			colorCombo.addItem(color);

		// init the width spinner
		double w0 = .5;
		SpinnerNumberModel widthModel = new SpinnerNumberModel(w0, 0, 10, .1);
        widthModel.setMaximum(null);
        widthSpinner = new JSpinner(widthModel);
        widthSpinner.setEditor(new JSpinner.NumberEditor(widthSpinner, "#0.0"));
      
		// init the line width unit combo
        widthUnitCombo.removeAllItems();
		for(AppLineWidthUnit unit : AppLineWidthUnit.values())
			widthUnitCombo.addItem(unit);

		// init the line join combo
		joinCombo.removeAllItems();
		for(AppLineJoin join : AppLineJoin.values())
			joinCombo.addItem(join);

		// init the line end cap combo
		capCombo.removeAllItems();
		for(AppEndCap cap : AppEndCap.values())
			capCombo.addItem(cap);

		// init the dash combo
        dashCombo.removeAllItems();
        for(AppDash dash : AppDash.values())
			dashCombo.addItem(dash);

		// init the width spinner
		double p0 = 0;
		SpinnerNumberModel phaseModel = new SpinnerNumberModel(p0, 0, 20, .05);
		phaseModel.setMaximum(null);
        phaseSpinner = new JSpinner(phaseModel);
        phaseSpinner.setEditor(new JSpinner.NumberEditor(phaseSpinner, "#0.00"));
 
        // Main panel for the different elements
        JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		// add visibility elements
		JPanel visibilityPanel = new JPanel(new FlowLayout());
		visibilityPanel.add(visibilityCheckbox);
		mainPanel.add(visibilityPanel);

		// add width elements
		JPanel widthPanel = new JPanel(new FlowLayout());
		widthPanel.add(widthLabel);
		widthPanel.add(widthSpinner);
		mainPanel.add(widthPanel);

		// add line width unit panel
		JPanel widthUnitPanel = new JPanel(new FlowLayout());
		widthUnitPanel.add(widthUnitLabel);
		widthUnitPanel.add(widthUnitCombo);
		mainPanel.add(widthUnitPanel);

		// add color elements
		JPanel colorPanel = new JPanel(new FlowLayout());
		colorPanel.add(colorLabel);
		colorPanel.add(colorCombo);
		mainPanel.add(colorPanel);
		
		// add line join elements
		JPanel joinPanel = new JPanel(new FlowLayout());
		joinPanel.add(joinLabel);
		joinPanel.add(joinCombo);
		mainPanel.add(joinPanel);

		// add dash elements
		JPanel capPanel = new JPanel(new FlowLayout());
		capPanel.add(capLabel);
		capPanel.add(capCombo);
		mainPanel.add(capPanel);

		// add dash elements
		JPanel dashPanel = new JPanel(new FlowLayout());
		dashPanel.add(dashLabel);
		dashPanel.add(dashCombo);
		mainPanel.add(dashPanel);

		// add dash phase
		JPanel phasePanel = new JPanel(new FlowLayout());
		phasePanel.add(phaseLabel);
		phasePanel.add(phaseSpinner);
		mainPanel.add(phasePanel);

		// set up global layout
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
		
		visibilityCheckbox.setSelected(style.isLineVisible());
		
		widthSpinner.setValue(style.getLineWidth());

		// set up marker fill combo
		widthUnitCombo.setSelectedItem(AppLineWidthUnit.NO_CHANGE); 
		LineWidthUnit unit = style.getLineWidthUnit();
		for(AppLineWidthUnit appUnit : AppLineWidthUnit.values()){
			if(unit.equals(appUnit.getUnit())){
				widthUnitCombo.setSelectedItem(appUnit);
				break;
			}
		}

		// select the "no change" option by default,
		// otherwise try to find element color
		colorCombo.setSelectedItem(AppColor.NO_CHANGE); 
		Color color = style.getLineColor();
		for(AppColor appColor : AppColor.values()){
			if(color.equals(appColor.getColor())){
				colorCombo.setSelectedItem(appColor);
				break;
			}
		}

		// select the "no change" option by default,
		// otherwise try to find element line join
		joinCombo.setSelectedItem(AppLineJoin.NO_CHANGE); 
		LineJoin join = style.getLineJoin();
		for(AppLineJoin appJoin : AppLineJoin.values()){
			if(join.equals(appJoin.getLineJoin())){
				joinCombo.setSelectedItem(appJoin);
				break;
			}
		}

		// select the "no change" option by default,
		// otherwise try to find element end cap
		capCombo.setSelectedItem(AppEndCap.NO_CHANGE); 
		EndCap cap = style.getLineEndCap();
		for(AppEndCap appCap : AppEndCap.values()){
			if(cap.equals(appCap.getEndCap())){
				capCombo.setSelectedItem(appCap);
				break;
			}
		}
		
		// select the "no change" option by default,
		// otherwise try to find element dash
		dashCombo.setSelectedItem(AppDash.NO_CHANGE); 
		float[] dash = style.getLineDash();
		for(AppDash appDash : AppDash.values()){
			if(dash.equals(appDash.getDash())){
				dashCombo.setSelectedItem(appDash);
				break;
			}
		}
		
		phaseSpinner.setValue((double)style.getLineDashPhase());
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
			DrawStyle newStyle = updateLineStyle(doc.getDrawStyle());
			doc.setDrawStyle(newStyle);
			return;
		}
		
		for(EuclideFigure item : selection){
			// update marker style of each element (creates a new instance for each)
			DrawStyle newStyle = updateLineStyle(item.getDrawStyle());
			item.setDrawStyle(newStyle);
		}
		
		view.repaint();
	}
	
	private DrawStyle updateLineStyle(DrawStyle style){
		DefaultDrawStyle newStyle = new DefaultDrawStyle(style);
		
		// update visibility
		boolean visible = visibilityCheckbox.isSelected();
		newStyle.setLineVisible(visible);
		
		// update line width
		double width = ((Double)widthSpinner.getValue()).doubleValue();
		if(width != Double.NaN)
			newStyle.setLineWidth(width);			
		
		// update line width unit
		AppLineWidthUnit appUnit = 
			(AppLineWidthUnit) widthUnitCombo.getSelectedItem();
		if(appUnit != AppLineWidthUnit.NO_CHANGE)
			newStyle.setLineWidthUnit(appUnit.getUnit());

		// update line color
		AppColor appColor = (AppColor) colorCombo.getSelectedItem();
		if(appColor != AppColor.NO_CHANGE)
			newStyle.setLineColor(appColor.getColor());
		
		// update line join
		AppLineJoin join = (AppLineJoin) joinCombo.getSelectedItem();
		if(join != AppLineJoin.NO_CHANGE)
			newStyle.setLineJoin(join.getLineJoin());
		
		// update line end cap
		AppEndCap cap = (AppEndCap) capCombo.getSelectedItem();
		if(cap != AppEndCap.NO_CHANGE)
			newStyle.setLineEndCap(cap.getEndCap());
		
		// update line dash
		AppDash appDash = (AppDash) dashCombo.getSelectedItem();
		if(appDash != AppDash.NO_CHANGE)
			newStyle.setLineDash(appDash.getDash());
		
		// update line dash phase
		float phase = (float) ((Double) phaseSpinner.getValue()).doubleValue();
		if(phase != Double.NaN)
			newStyle.setLineDashPhase(phase);			
		
		// return the modified style
		return newStyle;
	}
}
