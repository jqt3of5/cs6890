/**
 * 
 */
package gui;

import javax.swing.JPanel;

/**
 * @author dlegland
 *
 */
public abstract class EuclidePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected EuclideGui gui;
	
	protected EuclidePanel(EuclideGui gui){
		this.gui = gui;
	}
	
	/**
	 * updates the different widgets in the panel.
	 */
	public void updateWidgets(){}
	
	/**
	 * Overload this method to update current doc or view according to changes
	 * made in the panel.
	 */
	public void applyChanges(){}
}
