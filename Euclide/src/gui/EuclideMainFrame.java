/* file : EuclideMainFrame.java
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
package gui;

// gui library
import gui.actions.ExitAction;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.EuclideDoc;
import model.EuclideSheet;

import org.apache.log4j.Logger;

import app.EuclideApp;

/**
 * @author dlegland
 */
public class EuclideMainFrame extends EuclideFrame 
implements ChangeListener, WindowListener{
	
	// ===================================================================
	// static class variables
	
	private static final long 	serialVersionUID = 1L;
	
	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");
	
	public final static String 	defaultBaseTitle = "Euclide";
	public final static String 	defaultNoNameLabel = "NoName";
	
	// ===================================================================
	// class variables

	String baseTitle = defaultBaseTitle;
	
	String noNameLabel = defaultNoNameLabel;
		
	/** Current tool */
	EuclideTool currentTool = null;
	

	// gui resources	
	
	/**
	 * the list of views
	 */
	ArrayList<EuclideDocView> views = new ArrayList<EuclideDocView>();
	
	/**
	 * stores the view associated with each opened document.
	 */
	Hashtable<EuclideDoc, EuclideDocView> docView = 
		new Hashtable<EuclideDoc, EuclideDocView>();
	
	EuclideDocView currentView=null;
	
	Container viewPanel = null;
	

	// ===================================================================
	// Constructor
	
	 /**
	 * @throws java.awt.HeadlessException
	 */
	public EuclideMainFrame(EuclideGui gui) throws HeadlessException {
		super(gui);
		
		// create main view, associated with this document
		EuclideDoc doc = appli.getCurrentDoc();
		if(doc != null){
			currentView = new EuclideDocView(doc);
			this.addView(currentView);
			this.setCurrentView(currentView);
			docView.put(doc, currentView);//TODO: add DocListener to the view ?
		}
		
		JToolBar toolbar = gui.guiBuilder.createMainFrameToolBar();
		
		// Try to load the icon
		URL url = getClass().getResource("/res/icons/mainAppIcon.png");
		if (url != null) {
			setIconImage(new ImageIcon(url).getImage());
		}

		// layout of main panel components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(toolbar, BorderLayout.NORTH);
		mainPanel.add(currentView, BorderLayout.CENTER);
		this.setContentPane(mainPanel);
		
		// set up behavior when closing frame
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		// set up frame size depending on screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(800, screenSize.width-100);
        int height = Math.min(700, screenSize.width-100);
        Dimension frameSize = new Dimension(width, height);
        this.setSize(frameSize);
        logger.info("Frame size set to " + (width) + "*" + (height));
	
        // set up frame position depending on frame size
		int posX = (screenSize.width - width)/4;
        int posY = (screenSize.height - height)/4;
        this.setLocation(posX, posY);
	}

	
	// ===================================================================
	// Management of appli

	public EuclideApp getAppli(){
		return appli;
	}
	
	public EuclideGui getGui() {
		return gui;
	}
	
	
	// ===================================================================
	// Management of views

	public EuclideDocView getCurrentView(){
		return currentView;
	}
	
	public EuclideSheetView getCurrentSheetView(){
		if(currentView==null)
			return null;
		return currentView.getCurrentSheetView();
	}
	
	public EuclideSheet getCurrentSheet(){
		EuclideSheetView view = this.getCurrentSheetView();
		if(view==null)
			return null;
		return view.getSheet();
	}
	
	public void addView(EuclideDocView view){
		logger.info("Add a view");
		if(views.contains(view)) 
			return;
		
		views.add(view);
		docView.put(view.getDoc(), view);
		
		view.tabbedPane.addChangeListener(this);
	}
	
	public void removeAllViews(){
		views.clear();
		docView.clear();
	}
	
	public void setCurrentView(EuclideDocView view){
		logger.info("Change current view");
		
		// remove old view
		if (this.currentView != null)
			this.getContentPane().remove(this.currentView);

		// change to the new view
		this.currentView = view;

		if(view != null){
			// change the current document
			EuclideDoc doc = view.getDoc();
			logger.info("  change current doc to: " + doc.getName());

			// add panel corresponding to the view
			this.getContentPane().add(view, BorderLayout.CENTER);

			// stores the view associated with the doc
			docView.put(doc, view);
		}
		
		// update the display
		updateTitle();
		this.invalidate();
		this.validate();
	}

	/**
	 * Set up the current doc of the gui. Does not change the current doc
	 * of the application.
	 * @param doc the new current document
	 */
	public void setCurrentDoc(EuclideDoc doc){
		// remove old doc view
		if(currentView!=null)
			this.getContentPane().remove(currentView);
		
		if(doc==null)
			return;
		
		// get the stored view, or create it if it does not exist
		currentView = docView.get(doc);
		if(currentView==null){
			currentView = new EuclideDocView(doc);
			addView(currentView);
			docView.put(doc, currentView);
		}
		setCurrentView(currentView);
				
		// add the new doc view
		this.getContentPane().add(currentView, BorderLayout.CENTER);
	}
	
	
	// ===================================================================
	// Management of tools
	
	public EuclideTool getCurrentTool(){
		return currentTool;
	}
	
	public void setCurrentTool(EuclideTool tool){
		// Work only if there is a current view
		if (currentView == null)
			return;
		
		// call the deselect() method of the tool
		if(currentTool != null)
			currentTool.deselect();		
		
		// change Mouse listeners for all Views of all sheets
		for(EuclideDocView view : views)
			for(EuclideSheetView sheetView : view.getAllSheetViews()){
				// Remove listeners of old tool
				sheetView.removeMouseListener(currentTool);
				sheetView.removeMouseMotionListener(currentTool);
				
				// update labels to display
				sheetView.setMouseLabel("");

				// add listeners to the new current tool
				sheetView.addMouseListener(tool);
				sheetView.addMouseMotionListener(tool);
			}
		
		// set up the new current tool
		currentTool = tool;
		currentTool.select();
		
		currentView.setToolInstruction(currentTool.getInstruction());
	}
	
	
	// ===================================================================
	// mutators
	
	@Override
	public void setSize(int width, int height){
		super.setSize(width, height);
		logger.info("resize main frame");
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x, y, width, height);
		logger.info("setBounds of main frame");
	}

	
	// ===================================================================
	// display functions
	
	public void updateTitle(){
		
		// if no doc is open, displays a default title
		if (this.currentView == null) {
			this.setTitle(this.baseTitle);
			return;
		}
		
		// base name for title
		StringBuffer title = new StringBuffer(this.baseTitle);
		title.append(" - ");

		// extract name of current doc
		String fileName = currentView.getDoc().getName();
		if(fileName == null)
			fileName = this.noNameLabel;

		// update frame title with name of current file
		title.append(fileName);

		// also add flag for modified files
		if (currentView.getDoc().isModified())
			title = title.append("*");

		// also add current zoom
		EuclideSheetView sheetView = currentView.getCurrentSheetView();
		if (sheetView != null) {
			double zoom = sheetView.getZoom();
			String zoomString;
			if (zoom >= 1) {
				zoom = Math.round(zoom * 100) / 100;
				if (zoom == Math.round(zoom))
					zoomString = String.format("%d:1", (int) zoom);
				else
					zoomString = String.format("%.2f:1", zoom);
			} else {
				zoom = Math.round(1 / zoom * 100) / 100;
				if (zoom == Math.round(zoom))
					zoomString = String.format("1:%d", (int) zoom);
				else
					zoomString = String.format("1:%.2f", zoom);
			}
			title.append(" - ").append(zoomString);
		}
		// set up the new title
		this.setTitle(title.toString());
	}

	
	@Override
	public void validate(){
		//System.out.println("validate EuclideMainFrame");
		EuclideDocView view = this.getCurrentView();
		if (view!=null)
			view.validate();
		this.updateTitle();
		super.validate();
	}


	// ===================================================================
	// Event listeners
	
	public void stateChanged(ChangeEvent e){
		// reset state of current tool, so that elements can be created
		// only on one layer.
		logger.trace("State changed");
		if(currentTool != null){ 
			currentTool.deselect();
			currentTool.select();
		}
	}
	
	@Override
    public void windowClosing(WindowEvent evt) {
    	logger.info("close frame");
		new ExitAction(gui, "").actionPerformed(null);
    }
    
	@Override
    public void windowOpened(WindowEvent evt) {
    	this.repaint();
    }   
}
