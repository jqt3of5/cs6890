/**
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

/**
 * Base Dialog class for Euclide Application. To display a dialog, it is
 * convenient to use the showDialog() method in the EuclideGui class.
 * @author dlegland
 *
 */
public class EuclideDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	protected EuclideGui gui;

	protected EuclideDialog(EuclideGui gui){
		this(gui, "", false);
	}
	
	protected EuclideDialog(EuclideGui gui, String title){
		this(gui, title, false);
	}

	protected EuclideDialog(EuclideGui gui, String title, boolean modal){
		super(gui.getCurrentFrame(), title, modal);
		this.gui = gui;
	}

	/**
	 * To be overloaded.
	 */
	public void updateWidgets() {
	}
	
	/**
	 * To be overloaded.
	 */
	public void actionPerformed(ActionEvent arg0) {
	}	
}
