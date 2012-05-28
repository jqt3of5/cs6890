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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import math.geom2d.grid.Grid2D;
import math.geom2d.grid.SquareGrid2D;
import math.geom2d.grid.TriangleGrid2D;
import model.EuclideSheet;

/**
 * @author dlegland
 *
 */
public class GridPanel extends EuclidePanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private enum GridType {
		SQUARE("Square"), 
		TRIANGLE1("Triangle (up-down)"),
		TRIANGLE2("Triangle (left-right)");
		private String name;		
		GridType(String name){
			this.name = name;
		}
		@Override
		public String toString(){return name;};
	};
		
	// ===================================================================
	// class variables
	
	private JLabel 		gridTypeLabel 		= new JLabel("Grid Type:");
	private JLabel 		gridSizeLabel 		= new JLabel("Grid Size:");
	private JLabel 		snapNumberLabel 	= new JLabel("Snap Number:");
	
	private JCheckBox 	viewGridCheckbox 	= new JCheckBox("View grid");
	private JComboBox 	gridTypeCombo 		= new JComboBox(GridType.values());
	private JCheckBox 	snapGridCheckbox 	= new JCheckBox("Snap to grid");
	private JTextField 	gridSizeField 		= new JTextField(10);
	private JSpinner 	snapNumberSpinner 	= null;
	private JCheckBox 	viewAxesCheckbox 	= new JCheckBox("View axes");
	
	public GridPanel(EuclideGui gui){
		super(gui);
		this.buildPanel();
	}
	
	private void buildPanel(){
		// spinner for number of main grid subdivision for snap
        SpinnerModel snapNumberModel = new SpinnerNumberModel(10, 1, 100, 1);
        snapNumberSpinner = new JSpinner(snapNumberModel);
        snapNumberSpinner.setEditor(new JSpinner.NumberEditor(snapNumberSpinner, "#0"));

        // check boc for view grid
        JPanel viewGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewGridPanel.add(viewGridCheckbox);
		
        // type of grid
        JPanel gridTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gridTypePanel.add(gridTypeLabel);
        gridTypePanel.add(gridTypeCombo);
		
        // grid size (one double value)
        JPanel gridSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gridSizePanel.add(gridSizeLabel);
        gridSizePanel.add(gridSizeField);
		
        // Main grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Main Grid"));
        gridPanel.add(gridTypePanel);
        gridPanel.add(gridSizePanel);

        JPanel snapGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        snapGridPanel.add(snapGridCheckbox);
		
        JPanel snapNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        snapNumberPanel.add(snapNumberLabel);
        snapNumberPanel.add(snapNumberSpinner);
        
        // Snap grid panel
        JPanel snapPanel = new JPanel();
        snapPanel.setLayout(new BoxLayout(snapPanel, BoxLayout.Y_AXIS));
        snapPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Snap Grid"));
        snapPanel.add(snapGridPanel);
        snapPanel.add(snapNumberPanel);
        
		
		JPanel viewAxesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		viewAxesPanel.add(viewAxesCheckbox);
		
		// build the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(viewGridPanel);
        mainPanel.add(gridPanel);
		mainPanel.add(snapPanel);
		mainPanel.add(viewAxesPanel);
		
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void updateWidgets(){
		EuclideSheetView view = gui.getCurrentSheetView();
		EuclideSheet sheet = view.getSheet();
		
		viewGridCheckbox.setSelected(sheet.isGridVisible());
		snapGridCheckbox.setSelected(sheet.isSnapToGrid());
		
		double gridSize=1, snapSize=1;
		
		// Update grid type and grid size
		Grid2D grid = sheet.getGrid();		
		if(grid instanceof SquareGrid2D){
			gridTypeCombo.setSelectedIndex(0);
			gridSize = ((SquareGrid2D) grid).getSizeX();
		}else if(grid instanceof TriangleGrid2D){
			// Choose type triangle 1 or triangle 2 depending on orientation
			double theta = ((TriangleGrid2D) grid).getTheta();
			if (Math.abs(theta)<1e-5)
				gridTypeCombo.setSelectedIndex(1);
			else
				gridTypeCombo.setSelectedIndex(2);
			gridSize = ((TriangleGrid2D) grid).getSize();
		}		
		gridSizeField.setText(Double.toString(gridSize));

		// update snap number
		Grid2D snapGrid = sheet.getSnapGrid();		
		if(snapGrid instanceof SquareGrid2D){
			snapSize = ((SquareGrid2D) snapGrid).getSizeX();
		}else if(snapGrid instanceof TriangleGrid2D){
			snapSize = ((TriangleGrid2D) snapGrid).getSize();
		}
		int n = (int) Math.max(1, Math.round(gridSize/snapSize));
		snapNumberSpinner.getModel().setValue(n);
		
		// update view axes flag
		viewAxesCheckbox.setSelected(sheet.isAxesVisible());
	}
	
	/**
	 * Apply changes to the current doc.
	 */
	@Override
	public void applyChanges(){		
		EuclideSheetView view = gui.getCurrentView().getCurrentSheetView();
		EuclideSheet sheet = view.getSheet();
		
		Grid2D grid, snapGrid;
		double size = Double.parseDouble(gridSizeField.getText());
		int n = (Integer) snapNumberSpinner.getModel().getValue();
		double snap = size/n;
		
		switch(gridTypeCombo.getSelectedIndex()){
		case 0:
			grid = new SquareGrid2D(0, 0, size, size);
			snapGrid = new SquareGrid2D(0, 0, snap, snap);
			break;
		case 1:
			grid = new TriangleGrid2D(0, 0, size);
			snapGrid = new TriangleGrid2D(0, 0, snap);
			break;
		case 2:
			double theta = Math.PI/6;
			grid = new TriangleGrid2D(0, 0, size, theta);
			snapGrid = new TriangleGrid2D(0, 0, snap, theta);
			break;
		default:
			grid = new SquareGrid2D(0, 0, 1, 1);
			snapGrid = new SquareGrid2D(0, 0, snap, snap);
		}
		
		sheet.setGrid(grid);
		sheet.setSnapGrid(snapGrid);
		
		// update display options		
		sheet.setGridVisible(viewGridCheckbox.isSelected());
		sheet.setSnapToGrid(snapGridCheckbox.isSelected());
		sheet.setAxesVisible(viewAxesCheckbox.isSelected());

		// repaint current sheet
		view.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
	}
}
