/**
 * 
 */
package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import app.EuclideApp;

/**
 * This should be the base class for all window frames of the application.
 * Currently, only one subclass exists: EuclideMainFrame.
 * @author dlegland
 *
 */
public class EuclideFrame extends JFrame 
implements WindowListener {

	// ===================================================================
	// static class variables

	private static final long 	serialVersionUID = 1L;


	// ===================================================================
	// class variables

	/** The parent application. */
	EuclideApp appli;
	
	/** the gui */
	EuclideGui gui;
	

	// ===================================================================
	// Constructor
	
	 /**
	  * Just initialize GUI and corresponding appli.
	 */
	public EuclideFrame(EuclideGui gui) {
		super();
		
		this.gui = gui;
		this.appli = gui.appli;
		
	}
	
	// ===================================================================
	// Management of window events
	
	public void windowClosing(WindowEvent evt) {
	}

	public void windowClosed(WindowEvent evt) {
	}

	public void windowOpened(WindowEvent evt) {
	}

	public void windowIconified(WindowEvent evt) {
	}

	public void windowDeiconified(WindowEvent evt) {
	}

	public void windowActivated(WindowEvent evt) {
	}

	public void windowDeactivated(WindowEvent evt) {
	}

}
