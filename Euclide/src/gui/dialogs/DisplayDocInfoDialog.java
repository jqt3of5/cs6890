/* file : DisplayDocInfoDialog.java
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
package gui.dialogs;

import java.awt.Component;

import javax.swing.*;

import model.*;
import gui.*;

/**
 * @author dlegland
 */
public class DisplayDocInfoDialog extends EuclideDialog {

	private static final long serialVersionUID = 1L;

	/** the panels of the dialog */
	JLabel docNameLabel = new JLabel();
	JLabel docSheetNumberLabel = new JLabel();
	JLabel docShapeNumberLabel = new JLabel();
	JLabel docMeasureNumberLabel = new JLabel();
	JLabel docTransformNumberLabel = new JLabel();
	JLabel docPredicateNumberLabel = new JLabel();

	/** the Euclide frame */
	EuclideGui gui = null;
	
	public DisplayDocInfoDialog(EuclideGui gui){
		this(gui, "Document information", false);
	}

	public DisplayDocInfoDialog(EuclideGui gui, String title, boolean modal){
		super(gui, title, modal);
		this.gui = gui;			

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		addALabel(mainPanel, docNameLabel);
		addALabel(mainPanel, docSheetNumberLabel);
		addALabel(mainPanel, docShapeNumberLabel);
		addALabel(mainPanel, docMeasureNumberLabel);
		addALabel(mainPanel, docTransformNumberLabel);
		addALabel(mainPanel, docPredicateNumberLabel);

		updateLabels();
		
		this.setContentPane(mainPanel);
		this.setSize(300, 300);
	}

	private void addALabel(JPanel panel, JLabel label){
		panel.add(label);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	public void updateLabels(){
		EuclideDoc doc = gui.getAppli().getCurrentDoc();
		if (doc == null)
			return;
		
		docNameLabel.setText("Document name: " + doc.getName());
		docSheetNumberLabel.setText("Number of sheets: " 
				+ doc.getSheetNumber());
		docShapeNumberLabel.setText("Number of shapes: " 
				+ doc.getFigures().size());
		docMeasureNumberLabel.setText("Number of measures: " 
				+ doc.getMeasures().size());
		docTransformNumberLabel.setText("Number of transforms: " 
				+ doc.getTransforms().size());
		docPredicateNumberLabel.setText("Number of predicates: " 
				+ doc.getPredicates().size());
	}
	
	@Override
	public void setVisible(boolean b) {
		this.updateLabels();
		super.setVisible(b);
	}
}
