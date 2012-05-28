/* file : EuclideGui.java
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
import gui.actions.SelectDocumentAction;
import gui.dialogs.*;
import gui.panels.ConstantsPanel;
import gui.panels.InputMeasurePanel;
import gui.panels.LayersPanel;
import gui.util.GenericFileFilter;
import io.dgf.EuclideDGFWriter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.conic.EllipseArc2D;
import math.geom2d.conic.Parabola2D;
import math.geom2d.conic.ParabolaArc2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.Transform2D;
import model.EuclideDoc;
import model.EuclideFigure;
import model.EuclideLayer;
import model.EuclideSheet;
import model.event.*;

import org.apache.log4j.Logger;

import app.EuclideApp;
import app.event.EuclideAppEvent;
import app.event.EuclideAppListener;
import dynamic.*;
import dynamic.shapes.*;

/**
 * @author dlegland
 */
public class EuclideGui implements EuclideAppListener,
		EuclideDocListener, EuclideLayerListener, EuclideSheetListener {

	// ===================================================================
	// static class variables

	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	private final static String defaultBaseTitle = "Euclide";
	private final static String defaultNoNameLabel = "NoName";
	
	// ===================================================================
	// class variables

	/** The parent application. */
	EuclideApp appli;

	/** the instance of resource class */
	GuiBuilder guiBuilder;
	
	/** Main frame of the GUI */
	EuclideMainFrame mainFrame;

	String baseTitle = defaultBaseTitle;
	
	String noNameLabel = defaultNoNameLabel;

	/** list of commands (=actions) of application */
	private Hashtable<String, EuclideAction> actions = new Hashtable<String, EuclideAction>();

	/** list of available tools */
	private Hashtable<String, EuclideTool> tools = new Hashtable<String, EuclideTool>();

	/** A list of EuclideDialog, which can be accessed through their tag. */
	private Hashtable<String, EuclideDialog> dialogs = new Hashtable<String, EuclideDialog>();

	/** A list of EuclidePanel, which can be accessed through their tag. */
	private Hashtable<String, EuclidePanel> panels = new Hashtable<String, EuclidePanel>();

	/** A list of EuclidePanel, which can be accessed through their tag. */
	private Hashtable<EuclideSheet, Hashtable<String, EuclidePanel>> sheetPanels = 
		new Hashtable<EuclideSheet, Hashtable<String, EuclidePanel>>();

	/** list of text strings which can be translated */
	private Hashtable<String, String> textStrings = new Hashtable<String, String>();

	private JFileChooser saveWindow = null;
	private GenericFileFilter fileFilter = new GenericFileFilter("dgf",
			"Dynamic geometry files");

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
	
	// ===================================================================
	// Static method(s)

	public static void setUIFont (javax.swing.plaf.FontUIResource f){
		//
		// sets the default font for all Swing components.
		// ex. 
		//  setUIFont (new javax.swing.plaf.FontUIResource
		//   ("Serif",Font.ITALIC,12));
		//
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}    
	
	
	// ===================================================================
	// Constructor

	/**
	 * @throws java.awt.HeadlessException
	 */
	public EuclideGui(EuclideApp appli) throws HeadlessException {
		super();
		this.appli = appli;
		
		setupLookAndFeel();
		
		// Create a GUI Builder, which loads the necessary resources
		this.guiBuilder = new GuiBuilder(this);

		// Init tools and actions
		this.tools = this.guiBuilder.loadTools(this);
		this.actions = this.guiBuilder.loadActions(this, tools);

		// Init text strings used in dialogs and panels
		this.textStrings = this.guiBuilder.loadTextStrings();

		createMainFrame();
	}
	
	private void setupLookAndFeel() {
		// set up default font
//		setUIFont(new javax.swing.plaf.FontUIResource(Font.DIALOG, 
//				Font.PLAIN, 12));
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		UIManager.put("ComboBox.background", 
				UIManager.get("TextArea.background"));
	}

	/**
	 * Creates the main frame, and initialize all its GUI Items.
	 * @param builder the current instance of GUI Builder
	 */
	private void createMainFrame() {
		this.mainFrame = new EuclideMainFrame(this);

		// create menubar from resource file
		this.mainFrame.setJMenuBar(this.guiBuilder.createMenuBar());

		// Add listeners
		for (EuclideDoc doc : appli.getDocuments()) {
			addNewDocument(doc);
		}
		
		this.mainFrame.setCurrentTool(getTool("addFreePoint"));
		this.mainFrame.updateTitle();
	}
	
	
	// ===================================================================
	// Management of appli

	public EuclideApp getAppli() {
		return this.appli;
	}

	/**
	 * Return the frame containing the current document. At the moment, this
	 * correspond to the main frame, but this can evolve to a multi frame
	 * interface.
	 * 
	 * @return
	 */
	// TODO: should not return such specific class. Use "EuclideFrame" ?
	public EuclideMainFrame getCurrentFrame() {
		return this.mainFrame;
	}

	public String getVersion() {
		return this.getText("version", EuclideApp.version.toString());
	}

	// ===================================================================
	// Management of documents

	/**
	 * Returns the current document, or null if no document is open.
	 */
	public EuclideDoc getCurrentDoc() {
		if (getCurrentFrame() == null)
			return null;
		
		EuclideDocView view = getCurrentFrame().getCurrentView();
		if (view == null)
			return null;
		
		return view.getDoc();
	}

	// ===================================================================
	// Management of views

	public EuclideDocView getCurrentView() {
		return getCurrentFrame().getCurrentView();
	}

	public EuclideSheetView getCurrentSheetView() {
		EuclideDocView currentView = this.getCurrentView();
		if (currentView==null)
			return null;
		return currentView.getCurrentSheetView();
	}

	public EuclideSheet getCurrentSheet() {
		EuclideSheetView view = this.getCurrentSheetView();
		if (view==null)
			return null;
		return view.getSheet();
	}

	// ===================================================================
	// document list management

	private void addNewDocument(EuclideDoc doc) {
		// add a new menu item

		// Create action for selecting doc
		// TODO: add a number if same doc is open twice
		String docName = doc.getName(); 
		String actionName = "selectDoc:" + docName;
		EuclideAction action = new SelectDocumentAction(this, actionName, doc);
		JMenuItem item = new JMenuItem(action);
		item.setText(docName);
		
		// Find the menu item corresponding to document list, 
		// and add the created action to it
		EuclideMainFrame frame = getCurrentFrame();
		JMenu docListItem = (JMenu) findDocListItem(frame);
		docListItem.add(item);

		
		// create main view, associated with this document
		EuclideDocView view = new EuclideDocView(doc);

		frame.removeAllViews();
		frame.addView(view);
		frame.setCurrentView(view);
		frame.setCurrentTool(getTool("select"));
		view.repaint();
	}

	private void removeDocument(EuclideDoc doc) {
		EuclideMainFrame frame = getCurrentFrame();
		JMenu docListMenu = (JMenu) findDocListItem(frame);
		
		// removes the menu item that correspond to this document
		for (int i = 0; i < docListMenu.getMenuComponentCount(); i++) {
			JMenuItem item = (JMenuItem) docListMenu.getMenuComponent(i);
			if (item.getAction() == null)
				return;
			if (!(item.getAction() instanceof SelectDocumentAction))
				return;
			SelectDocumentAction action = (SelectDocumentAction) item.getAction();
			
			if (action.getDocument() == doc){
				docListMenu.remove(item);
				docListMenu.validate();
				return;
			}
		}
		
		logger.warn("Could not find JMenuItem associated to doc " + doc.getName());
	}

	private MenuElement findDocListItem(JFrame frame) {
		JMenuBar bar = frame.getJMenuBar();
		if (bar==null)
			return null;

		// Find menu item "file"
		JMenu fileMenu = null;
		for (int i = 0; i<bar.getComponentCount(); i++) {
			Component component = bar.getComponent(i);
			if (component.getName()==null)
				continue;
			if (component.getName().equals("file")) {
				fileMenu = (JMenu) bar.getComponent(i);
				break;
			}
		}

		// check file menu was found before continuing
		if (fileMenu==null)
			return null;

		// find item "documentList" in component list
		for (int i = 0; i<fileMenu.getMenuComponentCount(); i++) {
			Component component = fileMenu.getMenuComponent(i);
			if (component.getName()==null)
				continue;
			if (component.getName().equals("documentList")) {
				return (JMenuItem) component;
			}
		}

		return null;
	}

	/**
	 * Save the given document in its current file, or open a new dialog to
	 * choose the file if this is the first save.
	 */
	public boolean saveDocument(EuclideDoc doc) {
		// Save file
		String fileName = doc.getFileName();
		File file;

		if (fileName!=null) {
			file = new File(fileName);
		} else {
			file = chooseDocumentFile(doc);
		}

		if (file==null)
			return false;

		return saveDocument(doc, file);
	}

	public File chooseDocumentFile(EuclideDoc doc) {
		// create file dialog if it doesn't exist
		if (saveWindow==null) {
			saveWindow = new JFileChooser(".");
			saveWindow.setFileFilter(fileFilter);
		}

		if (doc.getFileName()!=null)
			saveWindow.setSelectedFile(new File(doc.getFileName()));
		else
			saveWindow.setSelectedFile(new File(doc.getName()+".dgf"));

		int ret = saveWindow.showSaveDialog(getCurrentFrame());
		if (ret!=JFileChooser.APPROVE_OPTION)
			return null;

		// choose file name
		File file = saveWindow.getSelectedFile();

		// add a valid extension if not present
		if (!file.getName().contains("."))
			file = new File(file.getParentFile(), file.getName().concat(".dgf"));
		return file;
	}

	public boolean saveDocument(EuclideDoc doc, File file) {
		// Open writer for document
		EuclideDGFWriter writer = null;
		try {
			writer = new EuclideDGFWriter(file);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		// save document
		try {
			writer.writeDocument(doc);
		} catch (IOException ex) {
			ex.printStackTrace();
			writer.close();
			return false;
		}

		// close the writer
		writer.close();

		// updates filename and name
		doc.setName(file.getName());
		doc.setFileName(file.getAbsolutePath());

		// update frame
		doc.setModified(false);
		getCurrentFrame().updateTitle();

		return true;
	}

	/**
	 * Closes the specified document, and return true if succeed. If the
	 * document has unsaved modifications, ask for saving it.
	 * 
	 * @param doc the document to close.
	 * @return true if document was successfully closed
	 */
	public boolean closeDocument(EuclideDoc doc) {

		// If no modification in the doc, we can remove it directly
		if (!doc.isModified()) {
			appli.removeDoc(doc);
			return true;
		}

		// The available options
		Object[] options = { "Save", "Close", "Cancel" };

		// Message to display
		String message = "The document ["+doc.getName()
				+"] was modified.\nClose anyway ?";

		// open the dialog of confirmation for closing the doc
		int res = JOptionPane.showOptionDialog(this.getCurrentFrame(), message,
				"Close document", JOptionPane.WARNING_MESSAGE,
				JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);

		switch (res) {
		case 0:
			boolean docSaved = saveDocument(doc);
			if (!docSaved)
				return false;

			appli.removeDoc(doc);
			return true;
		case 1:
			// Close document without saving
			appli.removeDoc(doc);
			return true;
		case 2:
		default:
			// If cancel, return false
			return false;
		}
	}

	// ===================================================================
	// Management of default dialogs

	/**
	 * Show a message dialog, centered with current frame.
	 */
	public void showMessage(String title, String msg) {
		// default title and icon
		JOptionPane.showMessageDialog(this.getCurrentFrame(), msg, title,
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Computes the mouse label when creating the given figure. This label
	 * depends on the class of the geometry of the created shape.
	 * 
	 * @param figure the figure which will be created
	 * @return the mouse label to display
	 */
	public String getMouseLabel(DynamicShape2D dynamic) {

		if (dynamic instanceof PointPointSet2D) {
			DynamicShape2D parent = (DynamicShape2D) dynamic.getParents()
					.iterator().next();
			String shapeName = getAppli().getShapeString(parent);
			return "new point in this "+shapeName;
		}

		if (dynamic instanceof PointOnCurve2D) {
			DynamicShape2D parent = (DynamicShape2D) dynamic.getParents()
					.iterator().next();
			String shapeName = getAppli().getShapeString(parent);
			return "new point on this "+shapeName;
		}

		if (dynamic instanceof PointOnBoundary2D) {
			DynamicShape2D parent = (DynamicShape2D) dynamic.getParents()
					.iterator().next();
			String shapeName = getAppli().getShapeString(parent);
			return "new point on this "+shapeName;
		}

		if (dynamic instanceof Intersection2StraightObjects2D)
			return "new intersection";

		if (dynamic instanceof IntersectionLineCurveIndex2D)
			return "new intersection";

		if (dynamic instanceof Intersection2Circles2D)
			return "new intersection";

		if (dynamic instanceof IntersectionLineCircle2D)
			return "new intersection";

		if (dynamic instanceof PolygonVertex2D)
			return "this vertex";

		return "";
	}

	/**
	 * Choose a dynamic object, given its class. Instruction can be provided.
	 * 
	 * @param class1
	 * @param instr
	 * @return
	 */
	public DynamicObject2D chooseDynamicObject(Class<?> class1, String instr) {

		if (Measure2D.class.isAssignableFrom(class1)) {
			DynamicMeasure2D measure = chooseMeasure(class1, instr);

			// Check null measure case
			if (measure==null) {
				logger.info("Cancel measure selection");
				return null;
			}

			// Store parameter
			return measure;

		} else if (Transform2D.class.isAssignableFrom(class1)) {
			DynamicTransform2D transform = chooseTransform(class1, instr);

			// Check null transform case
			if (transform==null) {
				logger.error("null transform");
				return null;
			}

			// Store parameter
			return transform;

		} else if (Vector2D.class.isAssignableFrom(class1)) {
			DynamicVector2D vector = chooseVector(class1, instr);

			// Check null transform case
			if (vector==null) {
				logger.error("null vector");
				return null;
			}

			// Store parameter, and change the parent class
			return vector;

		} else if (DynamicPredicate2D.class.isAssignableFrom(class1)) {

			DynamicPredicate2D predicate = choosePredicate(class1, instr);

			// Check null transform case
			if (predicate==null) {
				logger.error("null predicate");
				return null;
			}

			// Store parameter, and change the parent class
			return predicate;
		}

		return null;
	}

	public DynamicMeasure2D chooseMeasure(Class<?> measureClass, String instr) {
		
		// Open dialog to choose a measure
		ChooseMeasureDialog dialog = 
			(ChooseMeasureDialog) getDialog("chooseMeasure");

		// select appropriate measure type
		if (CountMeasure2D.class.isAssignableFrom(measureClass))
			dialog.setType(DynamicMeasure2D.Type.COUNTING);
		if (AngleMeasure2D.class.isAssignableFrom(measureClass))
			dialog.setType(DynamicMeasure2D.Type.ANGLE);
		if (LengthMeasure2D.class.isAssignableFrom(measureClass))
			dialog.setType(DynamicMeasure2D.Type.LENGTH);

		// show the dialog
		this.showDialog(dialog);

		// Get the measure
		DynamicMeasure2D measure = dialog.getMeasure();
		dialog.setVisible(false);

		return measure;
	}

	public DynamicPredicate2D choosePredicate(Class<?> predicateClass,
			String instr) {
		
		// Open dialog to choose a measure
		ChoosePredicateDialog dialog = 
			(ChoosePredicateDialog) getDialog("choosePredicate");
		//TODO: pass instr to dialog, but not as title
		
		// show the dialog
		this.showDialog(dialog);

		// Get the measure
		DynamicPredicate2D pred = dialog.getPredicate();
		dialog.setVisible(false);

		return pred;
	}

	public DynamicTransform2D chooseTransform(Class<?> transformClass,
			String instr) {

		// Open dialog to choose a measure
		ChooseTransformDialog dialog = 
			(ChooseTransformDialog) getDialog("chooseTransform");
		//TODO: pass instr to dialog, but not as title

		// show the dialog
		this.showDialog(dialog);

		// Get the measure
		DynamicTransform2D transform = dialog.getTransform();
		dialog.setVisible(false);

		return transform;
	}

	public DynamicVector2D chooseVector(Class<?> vectorClass, String instr) {

		// Open dialog to choose a measure
		ChooseVectorDialog dialog = 
			(ChooseVectorDialog) getDialog("chooseVector");
		//TODO: pass instr to dialog, but not as title

		// show the dialog
		this.showDialog(dialog);

		// Get the measure
		DynamicVector2D vector = dialog.getVector();
		dialog.setVisible(false);

		return vector;
	}

	/**
	 * Opens a dialog to input a string, and returns the written string, or null
	 * if cancel.
	 */
	public String chooseString(String instr) {
		if (instr==null)
			instr = "Enter the string:";

		// Open a dialog to specify the string
		String string = (String) JOptionPane.showInputDialog(
				this.getCurrentFrame(), instr, "Input String",
				JOptionPane.PLAIN_MESSAGE, null, // no icon
				null, // no string list -> use a text field
				"label"); // default value
		return string;
	}

	/**
	 * Adds a created object to the current document. Depending on object class,
	 * it will be added to the set of shapes, of vectors...
	 * 
	 * @param doc the document to add the object in
	 * @param newObject the object to add
	 */
	public void addNewObject(EuclideDoc doc, DynamicObject2D newObject) {
		// create a tag for the construction
		createDynamicObjectTag(doc, newObject);

		// Different processing depending on object class
		if (newObject instanceof DynamicShape2D) {
			addNewShape(doc, (DynamicShape2D) newObject);
		} else if (newObject instanceof DynamicTransform2D) {
			// class cast
			DynamicTransform2D transform = (DynamicTransform2D) newObject;

			// open dialog to save name of the transform
			this.showDialog(new SetTransformNameDialog(this, transform));

		} else if (newObject instanceof DynamicMeasure2D) {
			// class cast
			DynamicMeasure2D measure = (DynamicMeasure2D) newObject;

			// open dialog to save name of the measure
			this.showDialog(new SetMeasureNameDialog(this, measure));

		} else if (newObject instanceof DynamicVector2D) {
			// class cast
			DynamicVector2D vector = (DynamicVector2D) newObject;

			// open dialog to save name of the vector
			this.showDialog(new SetVectorNameDialog(this, vector));

		} else if (newObject instanceof DynamicPredicate2D) {
			// class cast
			DynamicPredicate2D predicate = (DynamicPredicate2D) newObject;

			// open dialog to save name of the predicate
			this.showDialog(new SetPredicateNameDialog(this, predicate));

		} else if (newObject instanceof DynamicArray2D) {
			doc.addDynamicObject(newObject);
			logger.info("Add a dynamic array");
		} else {
			logger.error("Unhandled class in AddDependentElement"
					+newObject.getClass());
		}
	}

	private void createDynamicObjectTag(EuclideDoc doc, DynamicObject2D dynamic) {
		String baseName = "obj";

		if (dynamic instanceof DynamicShape2D) {
			Shape2D shape = ((DynamicShape2D) dynamic).getShape();

			// try to find a better baseName according to shape class
			if (shape==null)
				baseName = "shape";
			else if (shape instanceof Point2D)
				baseName = "pt";
			else if (shape instanceof LinearShape2D)
				baseName = "lin";
			else if (shape instanceof Circle2D)
				baseName = "circ";
			else if (shape instanceof CircleArc2D)
				baseName = "acirc";
			else if (shape instanceof Polyline2D)
				baseName = "plin";
			else if (shape instanceof Polygon2D)
				baseName = "pgon";
			else if (shape instanceof Parabola2D)
				baseName = "par";
			else if (shape instanceof EllipseArc2D)
				baseName = "aell";
			else if (shape instanceof ParabolaArc2D)
				baseName = "apar";
			else if (shape instanceof Label2D)
				baseName = "lbl";

		} else if (dynamic instanceof DynamicVector2D) {
			baseName = "vect";
		} else if (dynamic instanceof DynamicTransform2D) {
			baseName = "trans";
		} else if (dynamic instanceof DynamicMeasure2D) {
			baseName = "meas";
		} else if (dynamic instanceof DynamicPredicate2D) {
			baseName = "pred";
		} else if (dynamic instanceof DynamicArray2D) {
			baseName = "arr";
		}

		// find the first free number corresponding to the base name
		String tag = doc.getNextFreeTag(baseName+"%d");

		// add to the tagged objects
		dynamic.setTag(tag);
	}

	private void addNewShape(EuclideDoc doc, DynamicShape2D shape) {
		// add the construction to the doc
		doc.addDynamicShape(shape);

		// create element with default styles
		EuclideFigure item = appli.createEuclideShape(shape);

		// create tag for the shape
		String tag = doc.getNextFreeTag("item%d");
		item.setTag(tag);

		// and add it to the current layer
		EuclideLayer layer = this.getCurrentSheet().getCurrentLayer();
		doc.addFigure(item, layer);
	}

	// ===================================================================
	// GUI management functions

	/**
	 * Gets a tool associated with a command name
	 * 
	 * @param name the name of the tool
	 * @return an instance of EuclideTool
	 */
	public EuclideTool getTool(String name) {
		EuclideTool tool = tools.get(name);
		if (tool==null)
			logger.error("Unknown tool: " + name);
		return tool;
	}

	/**
	 * Gets an action associated with a command name
	 * 
	 * @param name the name of action
	 * @return an instance of EuclideAction
	 */
	public EuclideAction getAction(String name) {
		return actions.get(name);
	}

	public String getText(String textTag, String defaultText) {
		String res = textStrings.get(textTag);
		return res==null ? defaultText : res;
	}

	/**
	 * Returns the image icon associated to a given action key.
	 */
	public ImageIcon getActionIcon(String key) {
		// either load icon name, or build a generic name based on action tag
		String iconName = this.guiBuilder.getResourceString(key + "Icon");
		if (iconName == null) {
			if (key.startsWith("add")) {
				iconName = key.substring(3, 4).toLowerCase() + key.substring(4)
						+ "Icon";
			} else {
				iconName = key + "Icon";
			}
		}

		// Try to load the icon
		URL url = getClass().getResource("/res/icons/" + iconName + ".png");

		// Associate the icon to the menu item
		ImageIcon icon = null;
		if (url != null) {
			icon = new ImageIcon(url);
		}
		
		return icon;
	}
	
	/**
	 * Returns the panel associated with a given key. If the panel was already
	 * created, the method returns the created instance. Otherwise, a new
	 * instance is created and returned.
	 * 
	 * @param key the key of the panel
	 * @return the EuclidePanel corresponding to the key
	 */
	public EuclidePanel getPanel(String key) {
		// Checks if panel was created
		if (panels.containsKey(key))
			return panels.get(key);

		// Try to create panel according to the key
		if (key.equals("inputMeasure"))
			panels.put("inputMeasure", new InputMeasurePanel(this));
		else if (key.equals("editConstants"))
			panels.put("editConstants", new ConstantsPanel(this));

		// returns created panel
		if (panels.containsKey(key))
			return panels.get(key);

		// Consider layers specific to the sheet
		EuclideSheet sheet = this.getCurrentSheet();
		Hashtable<String, EuclidePanel> panels2;
		if (sheetPanels.contains(sheet)) {
			panels2 = sheetPanels.get(sheet);
		} else {
			panels2 = new Hashtable<String, EuclidePanel>();
		}

		// returns found panel
		if (panels2.containsKey(key))
			return panels2.get(key);

		if (key.equals("editLayers"))
			panels2.put("editLayers", new LayersPanel(this, sheet));

		// returns created panel
		if (panels2.containsKey(key)) {
			sheetPanels.put(sheet, panels2);
			return panels2.get(key);
		}

		logger.error("Could not create panel for key '"+key+"'");
		return null;
	}

	/**
	 * Returns the dialog associated with a given key. If the dialog was already
	 * created, the method returns the created instance. Otherwise, a new
	 * instance is created and returned.
	 * 
	 * @param key the key of the dialog
	 * @return the EuclideDialog corresponding to the key
	 */
	public EuclideDialog getDialog(String key) {
		// Check if dialog was already created
		if (dialogs.containsKey(key))
			return dialogs.get(key);

		// Try to create a dialog according to the key
		if (key.equals("about"))
			dialogs.put("about", new AboutDialog(this));
		else if (key.equals("editSheet"))
			dialogs.put("editSheet", new SheetEditDialog(this));
		else if(key.equals("editDocStyles"))
			dialogs.put("editDocStyles", new EditDocStylesDialog(this));
		else if(key.equals("displayDocInfo"))
			dialogs.put("displayDocInfo", new DisplayDocInfoDialog(this, 
					"Document informations", false));
		else if (key.equals("displayDocTree"))
			dialogs.put("displayDocTree", new DisplayDocTreeDialog(this,
					"Document Tree", false));
		else if (key.equals("chooseMeasure"))
			dialogs.put("chooseMeasure", new ChooseMeasureDialog(this));
		else if (key.equals("choosePredicate"))
			dialogs.put("choosePredicate", new ChoosePredicateDialog(this));
		else if (key.equals("chooseTransform"))
			dialogs.put("chooseTransform", new ChooseTransformDialog(this));
		else if (key.equals("chooseVector"))
			dialogs.put("chooseVector", new ChooseVectorDialog(this));
		else if (key.equals("chooseLayer"))
			dialogs.put("chooseLayer", new ChooseLayerDialog(this));

		// Returns the created dialog
		if (dialogs.containsKey(key))
			return dialogs.get(key);

		logger.error("Could not create dialog for key '"+key+"'");
		return null;
	}

	/**
	 * Show the dialog in the middle of the current frame.
	 * 
	 * @param dialog the dialog to display
	 */
	public void showDialog(EuclideDialog dialog) {
		// get the reference frame
		JFrame frame = this.getCurrentFrame();

		// extract position and dimension of reference frame
		Point pos = frame.getLocation();
		Dimension dimF = frame.getSize();
		Dimension dimD = dialog.getSize();

		// compute position of the dialog
		int px = pos.x+dimF.width/2-dimD.width/2;
		int py = pos.y+dimF.height/2-dimD.height/2;

		// set up location of dialog
		dialog.setLocationRelativeTo(frame);
		dialog.setLocation(px, py);

		// show the dialog
		dialog.setVisible(true);
	}

	public void appliModified(EuclideAppEvent evt) {
		if (evt.getState() == EuclideAppEvent.CURRENTDOC_CHANGED) {
			EuclideDoc doc = appli.getCurrentDoc();
			if (doc != null)
				logger.info("Change current doc to: " + doc.getName());
			mainFrame.setCurrentDoc(doc);
		}

		JFrame frame = this.getCurrentFrame();
		frame.validate();
	}
	
	public void appliDocAdded(EuclideAppEvent evt) {
		EuclideDoc doc = ((EuclideApp.AppDocsModifiedEvent) evt).getDoc();
		logger.info("Add new doc to gui: " + doc.getName());
		addNewDocument(doc);
		mainFrame.setCurrentDoc(doc);
	}

	public void appliDocRemoved(EuclideAppEvent evt) {
		EuclideDoc doc = ((EuclideApp.AppDocsModifiedEvent) evt).getDoc();
		logger.info("Remove doc from gui: " + doc.getName());
		removeDocument(doc);
	}

	public void documentModified(EuclideDocEvent evt) {
		evt.getDocument().setModified(true);
	}

	public void sheetModified(EuclideSheetEvent evt) {
		EuclideSheet sheet = evt.getSheet();
		sheet.getDocument().setModified(true);
		this.getCurrentFrame().updateTitle();
	}

	public void layerModified(EuclideLayerEvent evt) {
		EuclideLayer layer = evt.getLayer();
		layer.getSheet().getDocument().setModified(true);
		this.getCurrentFrame().updateTitle();
	}
}
