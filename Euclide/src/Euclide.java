/* file : Euclide.java
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

import java.applet.Applet;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import app.*;
import gui.*;
import model.*;

/**
 * The main class for launching Euclide software.
 * 
 * @author dlegland
 */
public class Euclide extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	@Override
	public void init() {
		logger.info("Start applet Euclide");
		build(new String[0]);
	}

	public static void main(String[] args) {
		logger.info("Start programm Euclide");
		build(args);
	}

	private static void build(String[] args) {
		// Create the application class
		EuclideApp appli = new EuclideApp();

		// Create the main frame
		logger.info("Create GUI");
		EuclideGui gui = new EuclideGui(appli);
		appli.addApplicationListener(gui);

		// set up default display options
		logger.info("Create Main frame");
		JFrame frame = gui.getCurrentFrame();
		frame.setVisible(true);

		logger.info("Initialize document");

		// Add listeners (could be managed by GUI ?)
		for (EuclideDoc doc : appli.getDocuments()) {
			doc.addDocumentListener(gui);
			for (EuclideSheet sheet : doc.getSheets()) {
				sheet.addSheetListener(gui);
				for (EuclideLayer layer : sheet.getLayers())
					layer.addLayerListener(gui);
			}
		}
	}
}
