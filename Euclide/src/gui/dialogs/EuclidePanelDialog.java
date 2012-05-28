/**
 * 
 */
package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.EuclideDialog;
import gui.EuclideGui;
import gui.EuclidePanel;

/**
 * A simple dialog which contains an EuclidePanel, and a set of control buttons.
 * @author dlegland
 *
 */
public class EuclidePanelDialog extends EuclideDialog implements ActionListener{

	JButton okButton 		= new JButton("OK");
	JButton applyButton 	= new JButton("Apply");
	JButton cancelButton 	= new JButton("Cancel");
	
	public enum Buttons{
		OK,
		OK_CANCEL,
		OK_APPLY_CANCEL;
	}
	
	public enum CloseButton{
		OK,
		CANCEL
	}
	CloseButton closeButton;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EuclidePanel panel;
	
	public EuclidePanelDialog(EuclideGui gui, EuclidePanel panel,
			String title, Buttons buttons, boolean modal){
		super(gui, title, modal);
		
		this.panel = panel;
		
		JPanel controlPanel = new JPanel(new FlowLayout());
		okButton.addActionListener(this);
		controlPanel.add(okButton);
		if(buttons==Buttons.OK_APPLY_CANCEL){
			applyButton.addActionListener(this);
			controlPanel.add(applyButton);
		}
		if(buttons==Buttons.OK_APPLY_CANCEL || buttons==Buttons.OK_CANCEL){
			cancelButton.addActionListener(this);
			controlPanel.add(cancelButton);
		}
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		
		this.pack();
	}

	public EuclidePanelDialog(EuclideGui gui, EuclidePanel panel, String title){
		this(gui, panel, title, Buttons.OK_APPLY_CANCEL, false);
	}

	@Override
	public void setVisible(boolean visible){
		super.setVisible(visible);
		
		if(visible){
			panel.updateWidgets();
			this.pack();
		}
	}
	
	public CloseButton getCloseButton(){
		return closeButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource()==okButton){
			panel.applyChanges();
			closeButton = CloseButton.OK;
			this.setVisible(false);
		}
		if(evt.getSource()==applyButton){
			panel.applyChanges();
		}
		if(evt.getSource()==cancelButton){
			closeButton = CloseButton.CANCEL;
			this.setVisible(false);
		}
	}
}
