/**
 * 
 */
package gui.dialogs;

import gui.EuclideDialog;
import gui.EuclideGui;
import gui.EuclideDocView;
import gui.EuclideSheetView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import math.geom2d.Box2D;
import model.EuclideSheet;

/**
 * @author dlegland
 *
 */
public class SheetEditDialog extends EuclideDialog implements ActionListener {

	/** serial */
	private static final long serialVersionUID = 1L;

	/** the reference sheet */
	private EuclideSheet sheet;
	
	
	private boolean valid = false;
	
	// GUI Items
	
	JLabel nameLabel = new JLabel("Sheet name:");
	JLabel xminLabel = new JLabel(" xmin:");
	JLabel xmaxLabel = new JLabel(" xmax:");
	JLabel yminLabel = new JLabel(" ymin:");
	JLabel ymaxLabel = new JLabel(" ymax:");
	JLabel clipModeLabel = new JLabel("Clipping mode:");
	
	JTextField nameEdit = new JTextField("New Sheet", 10);
	
	JTextField xminEdit = new JTextField("-10", 5);
	JTextField xmaxEdit = new JTextField("+10", 5);
	JTextField yminEdit = new JTextField("-10", 5);
	JTextField ymaxEdit = new JTextField("+10", 5);
	JComboBox clipModeCombo = new JComboBox(new String[]{"View box", "none"});
	
	
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	
	
	public SheetEditDialog(EuclideGui gui, EuclideSheet sheet, String title, boolean modal){
		super(gui, title, modal);
		
		this.sheet = sheet;
		this.valid = false;
		
		// init gui items with current sheet properties
		this.setSheet(sheet);
		
		// set up listener
		nameEdit.addActionListener(this);		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		// Set up layout
		this.setupLayout();		
	}
	
	public SheetEditDialog(EuclideGui gui){
		super(gui, "Edit sheet", true);
		
		this.valid = false;
		
		// set up listener
		nameEdit.addActionListener(this);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		// Set up layout
		this.setupLayout();		
	}
		
	private void setupLayout(){
		// Panel for name
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(nameLabel);
		namePanel.add(nameEdit);
		namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		namePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Name of the sheet"));		
		
		
		// inner min coord of viewbox
		JPanel minPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		minPanel.add(xminLabel);
		minPanel.add(xminEdit);
		minPanel.add(xmaxLabel);
		minPanel.add(xmaxEdit);
		minPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// inner panel for max coord of viewbox
		JPanel maxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		maxPanel.add(yminLabel);
		maxPanel.add(yminEdit);
		maxPanel.add(ymaxLabel);
		maxPanel.add(ymaxEdit);
		maxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Panel for viewbox
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
		sizePanel.add(minPanel);
		//sizePanel.add(Box.createHorizontalGlue());
		sizePanel.add(maxPanel);
		sizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// decorate with a nice border
		sizePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"View box"));		

//		JPanel clipModePanel = new JPanel();
//		clipModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		clipModePanel.add(clipModeLabel);
//		clipModePanel.add(clipModeCombo);
//		clipModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(namePanel);
		centerPanel.add(sizePanel);
		//centerPanel.add(clipModePanel);
			
		// Control Panel
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
		
		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		
		// Apply layout
		this.setContentPane(mainPanel);
		this.pack();
	}
	

	public void setSheet(EuclideSheet sheet){
		this.sheet = sheet;
		
		// init gui items with current sheet properties
		nameEdit.setText(sheet.getName());
		Box2D box = sheet.getViewBox();
		xminEdit.setText(Double.toString(box.getMinX()));
		xmaxEdit.setText(Double.toString(box.getMaxX()));
		yminEdit.setText(Double.toString(box.getMinY()));
		ymaxEdit.setText(Double.toString(box.getMaxY()));		
		
		this.valid = false;
	}
	
	@Override
	public boolean isValid(){
		return valid;
	}
	
	@Override
	public void updateWidgets() {
		// Extract current sheet
		EuclideDocView view = gui.getCurrentView();
		if (view == null)
			return;
		if (view.getAllSheetViews().size() == 0)
			return;
		EuclideSheet sheet = view.getCurrentSheetView().getSheet();

		// update sheet
		setSheet(sheet);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		
		if (event.getSource() == okButton) {
			// update name
			sheet.setName(nameEdit.getText());
			
			// update view box
			double xmin = Double.parseDouble(xminEdit.getText());
			double xmax = Double.parseDouble(xmaxEdit.getText());
			double ymin = Double.parseDouble(yminEdit.getText());
			double ymax = Double.parseDouble(ymaxEdit.getText());
			sheet.setViewBox(new Box2D(xmin, xmax, ymin, ymax));
			
			EuclideDocView view = gui.getCurrentView();
			if (view == null)
				return;
			
			// update display of current view
			if (view.getAllSheetViews().size() != 0) {
				EuclideSheetView sheetView = view.getCurrentSheetView();
				
				// set up position of origin point in the middle of the page
				sheetView.setOriginToPageCenter();
				sheetView.setViewCenterToPageCenter();
	
				sheetView.repaint();
				sheetView.validate();
			}
			
			this.valid = true;
			this.setVisible(false);
		}
		
		if (event.getSource() == cancelButton) {
			this.valid = false;
			this.setVisible(false);
		}
	}
}
