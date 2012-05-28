/* file : ChooseMeasureDialog.java
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
 * Created on 9 avr. 2006
 *
 */

package gui.dialogs;

import gui.EuclideDialog;
import gui.EuclideGui;
import gui.panels.InputMeasurePanel;
import gui.panels.StoredMeasurePanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.EuclideDoc;

import dynamic.DynamicMeasure2D;

/**
 * A dialog to choose a storedMeasure. The chosen storedMeasure can be either a
 * stored storedMeasure identified by name, or a constant. The storedMeasure is
 * accessible via method 'getMeasure()'.
 * 
 * @author dlegland
 */
public class ChooseMeasureDialog extends EuclideDialog implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	InputMeasurePanel inputPanel;
	StoredMeasurePanel storedPanel;

	JButton inputOkButton = new JButton("OK");
	JButton storedOkButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	JLabel description = new JLabel();

	DynamicMeasure2D measure = null;
	DynamicMeasure2D inputMeasure = null;
	DynamicMeasure2D storedMeasure = null;
	Hashtable<String, DynamicMeasure2D> storedMeasures =
		new Hashtable<String, DynamicMeasure2D>();

	public ChooseMeasureDialog(EuclideGui gui) {
		super(gui, "Choose a measure", true);

		inputOkButton.addActionListener(this);
		storedOkButton.addActionListener(this);
		cancelButton.addActionListener(this);

		inputPanel = (InputMeasurePanel) gui.getPanel("inputMeasure");

		storedPanel = new StoredMeasurePanel(gui);

		// control panel (only "cancel" buttons)
		JPanel controlPanel = new JPanel();
		controlPanel.add(cancelButton);

		// input new measure container
		JPanel inputContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		inputOkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputContainer.add(inputPanel);
		inputContainer.add(inputOkButton);

		// decorate with a nice border
		String str = gui.getText("ChooseMeasureDialog.inputPanel.label",
				"Constant Measure");
		inputContainer.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray), str));

		// stored measure panel
		storedOkButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel storedContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		storedContainer.add(storedPanel);
		storedContainer.add(storedOkButton);

		// decorate with a nice border
		storedContainer.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Use a stored Measure"));

		// The main panel, which contains other panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		inputContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(inputContainer);
		storedContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(storedContainer);
		controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(controlPanel);

		// set content panel.
		setContentPane(mainPanel);
		pack();
	}

	public void setType(DynamicMeasure2D.Type type) {
		inputPanel.setType(type);
	}

	public DynamicMeasure2D getMeasure() {
		return measure;
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			storedPanel.updateWidgets();
		}
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == inputOkButton) {
			// keep a measure wrapper -> need to add the wrapper to the doc
			inputPanel.applyChanges();
			measure = inputPanel.getMeasure();
			this.setVisible(false);
			
			// set up tag
			EuclideDoc doc = gui.getCurrentDoc();
			String tag = doc.getNextFreeTag("const%02d");
			measure.setTag(tag);
			
			// add the new measure wrapper to the doc
			doc.addMeasure(measure);
		}
		
		if (event.getSource() == storedOkButton) {
			// use a measure already defined.
			measure = storedMeasure;
			measure = storedPanel.getMeasure();
			this.setVisible(false);
		}
		
		if (event.getSource() == cancelButton) {
			measure = null;
			this.setVisible(false);
		}
	}
}
