/* file : GuiBuilder.java
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

import gui.actions.SelectToolAction;
import gui.init.ActionsLoader;
import gui.init.ToolsLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import org.apache.log4j.Logger;

/**
 * @author dlegland
 */
public class GuiBuilder {

	// ===================================================================
	// static class variables

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	// ===================================================================
	// class variables

	/** The parent frame */
	EuclideGui gui = null;

	/** the resource file of the program */
	private ResourceBundle resources;

	/** the resource file of the program */
	private Properties menuProps = new Properties();

	/** list of menuItems in the MenuBar */
	private Hashtable<String, JMenuItem> menuItems = new Hashtable<String, JMenuItem>();

	/** flag to display debug info */
	boolean debug = false;

	// ===================================================================
	// static functions

	/**
	 * Take the given string and chop it up into a series of strings on
	 * whitespace boundaries. This is useful for trying to get an array of
	 * strings out of the resource file.
	 */
	protected final static Collection<String> getTokens(String input) {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer t = new StringTokenizer(input);

		while (t.hasMoreTokens())
			list.add(t.nextToken());

		return list;
	}

	public GuiBuilder(EuclideGui gui) {
		this.gui = gui;
		// load resources of program
		try {
			resources = ResourceBundle.getBundle("res.Euclide",
					Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.err.println("res/Euclide.properties not found");
		}
	}

	/**
	 * Open the resource file "Euclide.tools", and read it. Each line contains
	 * the code name of the tool, used to identify it, and the class name of the
	 * tool. This class name i used to dynamically construct the tool, add it to
	 * the tools pool of the application, and construct an action with the same
	 * code name used to select the tool, by using a SelectToolAction class.
	 */
	public Hashtable<String, EuclideTool> loadTools(EuclideGui gui) {

		Hashtable<String, EuclideTool> tools = new ToolsLoader(gui).loadTools();

		this.initToolInstructions(tools);

		logger.info("Tools loaded successfully");
		return tools;
	}

	public void initToolInstructions(Hashtable<String, EuclideTool> tools) {
		String instrString;

		for (String key : tools.keySet()) {
			instrString = this.getResourceString(key + "Instr");

			if (instrString != null) {
				EuclideTool tool = tools.get(key);
				logger.trace("Add tool instruction: " + key);
				String[] instr = parseInstructionString(instrString);
				tool.setInstructions(instr);
			}
		}
	}

	public Hashtable<String, EuclideAction> loadActions(EuclideGui gui,
			Hashtable<String, EuclideTool> tools) {

		Hashtable<String, EuclideAction> actions = new ActionsLoader(gui)
				.loadActions();

		this.initActionInstructions(actions);

		// also add an action of type 'select tool' for each Tool previously
		// loaded.
		for (String key : tools.keySet())
			actions.put(key, new SelectToolAction(gui, key));

		logger.info("Actions loaded successfully");
		return actions;
	}

	public void initActionInstructions(Hashtable<String, EuclideAction> actions) {
		String key;
		Enumeration<String> iter;
		String instr, tooltip;

		iter = actions.keys();
		while (iter.hasMoreElements()) {
			// get the current action by its key
			key = iter.nextElement();
			EuclideAction action = actions.get(key);

			// set up instruction
			instr = this.getResourceString(key + "Instr");
			if (instr != null) {
				action.setInstructions(parseInstructionString(instr));
				logger.trace("Add action instruction: " + key);
			}

			// set up tooltip string
			tooltip = this.getResourceString(key + "Tooltip");
			if (tooltip != null) {
				action.putValue(Action.SHORT_DESCRIPTION, tooltip);
				// System.out.println("add tooltip " + tooltip);
			}
		}
	}

	private String[] parseInstructionString(String string) {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(string, ";");
		while (st.hasMoreTokens())
			list.add(st.nextToken());

		String instr[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			instr[i] = list.get(i);

		return instr;
	}

	public Hashtable<String, String> loadTextStrings() {
		Hashtable<String, String> hash = new Hashtable<String, String>();

		// Load the resource file
		ResourceBundle textResource = null;
		try {
			textResource = ResourceBundle.getBundle("res.strings",
					Locale.getDefault());
		} catch (MissingResourceException mre) {
			logger.error("res/strings.properties not found");
			return hash;
		}

		// Convert ResourceBundle to Hashtable.
		for (String key : textResource.keySet())
			hash.put(key, textResource.getString(key));

		// return result
		return hash;
	}

	public JToolBar createMainFrameToolBar() {
		JToolBar tb = new JToolBar("Main toolbar");
		tb.setFloatable(false);
		
		// Add various action buttons
		addToolbarAction(tb, "newDoc");
		addToolbarAction(tb, "saveDoc");
		tb.addSeparator();
		addToolbarAction(tb, "select");
		addToolbarAction(tb, "moveFreePoints");
		addToolbarAction(tb, "zoomin");
		addToolbarAction(tb, "zoomout");
		tb.addSeparator();
		addToolbarAction(tb, "addFreePoint");
		addToolbarAction(tb, "addLineSegment");
		addToolbarAction(tb, "addStraightLine");
		addToolbarAction(tb, "addParallelLine");
		addToolbarAction(tb, "addPerpendicularLine");
		tb.addSeparator();
		addToolbarAction(tb, "addCircle2Points");
		addToolbarAction(tb, "addCircle3Points");
		addToolbarAction(tb, "addCircleArc3Points");
		addToolbarAction(tb, "addEllipse3Points");
		addToolbarAction(tb, "addBezierCurve4Points");
		addToolbarAction(tb, "addPolyline");
		addToolbarAction(tb, "addPolygonNPoints");
				
		return tb;
	}
	
	private void addToolbarAction(JToolBar tb, String actionKey) {
		
		//Create and initialize the button.
	    JButton button = new JButton();
	    button.setBorderPainted(false);
	    
	    // Associates button with current action
		EuclideAction action = gui.getAction(actionKey);
	    button.setAction(action);

	    // Set up tooltip
	    String tooltip = getResourceString(actionKey + "Tooltip");
	    button.setToolTipText(tooltip);

	    ImageIcon icon = gui.getActionIcon(actionKey);
	    if (icon != null) {
	        button.setIcon(icon);
	        button.setText(null);
	    } else {
	        button.setText(actionKey);
	        logger.error("Could not find toolbar icon for action: " + 
	        		actionKey);
	    }

	    tb.add(button);
	}
	
	/**
	 * Create the menubar for the app. By default this pulls the definition of
	 * the JMenu from the associated resource file.
	 */
	protected JMenuBar createMenuBar() {

		// Open URL containing menu information
		URL url = getClass().getResource("res/Euclide.menu");
		if (url == null) {
			logger.error("Couldn't find the tools resource file: <Euclide.menu>");
			System.exit(0);
		}

		// Load properties from the menu file
		try {
			menuProps.load(url.openStream());
		} catch (FileNotFoundException ex) {
			logger.error("Couldn't find the tools resource file: <Euclide.menu>");
			System.exit(0);
		} catch (IOException ex) {
			logger.error("Error while reading file: <Euclide.menu>");
			System.exit(0);
		}

		logger.info("Menu resource file opened");
		// System.out.println(menuProps);

		JMenuBar mb = new JMenuBar();

		// Check if "menubar" resource is present in the file
		String menubarString = menuProps.getProperty("menubar", null);
		if (menubarString == null) {
			logger.warn("Warning: no menu definition in resource file");
			return mb;
		}

		// find each menu in the resource bundle, and recursively
		// create each menu
		for (String token : getTokens(menubarString)) {
			JMenu m = createMenu(token);
			if (m != null)
				mb.add(m);
		}

		// return the created menubar
		return mb;
	}

	/**
	 * Create a JMenu for the app. By default this pulls the definition of the
	 * JMenu from the associated resource file.
	 */
	protected JMenu createMenu(String key) {

		// create the "JMenu" object
		JMenu menu = new JMenu(key);
		menu.setName(key);

		// create menu label (from resource, if present, or from key)
		String menuLabel = getResourceString(key + "Label");
		if (menuLabel == null)
			menuLabel = key;
		menu.setText(menuLabel);

		// create icon Name
		String iconName = getResourceString(key + "Icon");

		// Try to load the icon
		URL url = getClass().getResource("/res/icons/" + iconName + ".png");

		// Associate the icon to the menu
		if (url != null) {
			menu.setHorizontalTextPosition(SwingConstants.RIGHT);
			menu.setIcon(new ImageIcon(url));
		}

		// get the elements of menu from resource bundle
		String list = menuProps.getProperty(key + "Menu", null);
		if (list == null) {
			logger.warn("No menu definition for: " + key);
			return menu;
		}

		// Process each token associated with the menu string
		for (String token : getTokens(list)) {
			if (token.equals("-"))
				menu.addSeparator();
			else {
				// If a token with "Menu" exists, creates a new submenu,
				// otherwise adds an action via a new menu item
				menuLabel = menuProps.getProperty(token + "Menu", null);
				if (menuLabel != null)
					menu.add(createMenu(token));
				else
					menu.add(createMenuItem(token));
			}
		}

		// return the created menu
		return menu;
	}

	/**
	 * This is the hook through which all JMenu items are created. It registers
	 * the result with the menuitem hashtable so that it can be fetched with
	 * getMenuItem().
	 * 
	 * @see #getMenuItem
	 */
	protected JMenuItem createMenuItem(String cmd) {
		JMenuItem mi;

		// associates menu item with a stored action
		EuclideAction action = gui.getAction(cmd);
		if (action != null) {
			// System.out.println("create menu item for action " + cmd);
			mi = new JMenuItem(action);
		} else {
			logger.error("Could not find action for menu item " + cmd);
			mi = new JMenuItem();
		}
		mi.setName(cmd);

		// Create new JMenuItem, with name get from resource file,
		// or name equal to key if resource bundle doesn't provide
		// label for this item
		String itemName = getResourceString(cmd + "Label");
		if (itemName == null)
			itemName = cmd;
		mi.setText(itemName);

		// either load icon name, or build a generic name based on action tag
		String iconName = getResourceString(cmd + "Icon");
		if (iconName == null) {
			if (cmd.startsWith("add")) {
				iconName = cmd.substring(3, 4).toLowerCase() + cmd.substring(4)
						+ "Icon";
			} else {
				iconName = cmd + "Icon";
			}
		}

		// Try to load the icon
		URL url = getClass().getResource("/res/icons/" + iconName + ".png");

		// Associate the icon to the menu item
		if (url != null) {
			mi.setHorizontalTextPosition(SwingConstants.RIGHT);
			mi.setIcon(new ImageIcon(url));
		}

		// Create new JMenuItem, with name get from resource file,
		// or name equal to key if resource bundle doesn't provide
		// label for this item
		String tooltip = getResourceString(cmd + "Tooltip");
		if (tooltip != null)
			mi.setToolTipText(tooltip);

		// set EuclideAction associated to this item, either from resource
		// file, or directly from the key.
		String astr = menuProps.getProperty(cmd + "Action");
		if (astr == null)
			astr = cmd;
		mi.setActionCommand(astr);

		// set up accelerator
		String accel = this.getResourceString(cmd + "Accel");
		if (accel != null) {
			// action.putValue(Action.SHORT_DESCRIPTION, tooltip);
			logger.info("add accelerator: " + accel);
			KeyStroke stroke = parseAccelerator(accel);
			mi.setAccelerator(stroke);
		}

		menuItems.put(cmd, mi);
		return mi;
	}

	/**
	 * Parse the accelerator for a given menu item. Accelerator string is
	 * composed of a set of modifiers, and a key name, all separated by
	 * underscore. Example: "CTRL_SHIFT_A". This function creates the KeyStroke
	 * corresponding to the given string.
	 */
	private KeyStroke parseAccelerator(String accelString) {
		String[] tokens = accelString.split("_");
		if (tokens.length == 0)
			return null;

		int modifiers = 0;
		for (int i = 0; i < tokens.length - 1; i++) {
			if (tokens[i].compareToIgnoreCase("CTRL") == 0)
				modifiers = modifiers | ActionEvent.CTRL_MASK;
			else if (tokens[i].compareToIgnoreCase("SHIFT") == 0)
				modifiers = modifiers | ActionEvent.SHIFT_MASK;
			else if (tokens[i].compareToIgnoreCase("ALT") == 0)
				modifiers = modifiers | ActionEvent.ALT_MASK;
			else
				throw new IllegalArgumentException(
						"Cannot understand KeyCode modifier: " + tokens[i]);
		}

		String keyString = tokens[tokens.length - 1];
		int keyCode = parseKeyCode(keyString);

		KeyStroke key = KeyStroke.getKeyStroke(keyCode, modifiers);
		return key;
	}

	private int parseKeyCode(String keyString) {
		// first process single characters codes
		if (keyString.length() == 1) {
			switch (keyString.toLowerCase().charAt(0)) {
			case 'a':
				return KeyEvent.VK_A;
			case 'b':
				return KeyEvent.VK_B;
			case 'c':
				return KeyEvent.VK_C;
			case 'd':
				return KeyEvent.VK_D;
			case 'e':
				return KeyEvent.VK_E;
			case 'f':
				return KeyEvent.VK_F;
			case 'g':
				return KeyEvent.VK_G;
			case 'h':
				return KeyEvent.VK_H;
			case 'i':
				return KeyEvent.VK_I;
			case 'j':
				return KeyEvent.VK_J;
			case 'k':
				return KeyEvent.VK_K;
			case 'l':
				return KeyEvent.VK_L;
			case 'm':
				return KeyEvent.VK_M;
			case 'n':
				return KeyEvent.VK_N;
			case 'o':
				return KeyEvent.VK_O;
			case 'p':
				return KeyEvent.VK_P;
			case 'q':
				return KeyEvent.VK_Q;
			case 'r':
				return KeyEvent.VK_R;
			case 's':
				return KeyEvent.VK_S;
			case 't':
				return KeyEvent.VK_T;
			case 'u':
				return KeyEvent.VK_U;
			case 'v':
				return KeyEvent.VK_V;
			case 'w':
				return KeyEvent.VK_W;
			case 'x':
				return KeyEvent.VK_X;
			case 'y':
				return KeyEvent.VK_Y;
			case 'z':
				return KeyEvent.VK_Z;
			default:
				throw new IllegalArgumentException(
						"Cannot understand KeyCode string: " + keyString);
			}
		}
		
		// Process multi-characters key codes
		if (keyString.compareToIgnoreCase("suppr") == 0)
			return KeyEvent.VK_DELETE;
		else
			throw new IllegalArgumentException(
					"Cannot understand KeyCode string: " + keyString);
	}

	// Resource Management ------------------------------

	protected URL getResource(String key) {
		String name = getResourceString(key);

		if (name != null) {
			URL url = this.getClass().getResource(name);
			return url;
		}
		return null;
	}

	protected String getResourceString(String nm) {
		String str;
		try {
			str = resources.getString(nm);
		} catch (Exception ex) {
			str = null;
		}
		return str;
	}
}
