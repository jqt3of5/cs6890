/**
 * File: 	EditDocStylesDialog.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 févr. 10
 */
package gui.dialogs;

import gui.EuclideDialog;
import gui.EuclideGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author dlegland
 *
 */
public class EditDocStylesDialog extends EuclideDialog implements
		ActionListener {

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EditDocStylesDialog(EuclideGui gui) {
		this(gui, "Edit document styles");
	}
	/**
	 * 
	 */
	public EditDocStylesDialog(EuclideGui gui, String title) {
		super(gui, title, false);
		
		// Set up layout
		this.setupLayout();		
	}

	private void setupLayout(){
		// Panel for name
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(new JLabel("label"));
		centerPanel.add(new JLabel("another label"));
		centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Name of the sheet"));
		
		// Control Panel
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);
		
		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.EAST);
		
		// Apply layout
		this.setContentPane(mainPanel);
		this.pack();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
