/*
 * File : OpenFileAction.java
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
 * author : Legland
 * Created on 08 May 2007
 */
 
package gui.actions;

import gui.EuclideAction;
import gui.EuclideGui;
import gui.util.GenericFileFilter;
import io.dgf.EuclideDGFReader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import model.EuclideDoc;

import org.apache.log4j.Logger;

import app.EuclideApp;

/**
 * Action creates a new document.
 * @author Legland
 */
public class OpenFileAction extends EuclideAction {	

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	private JFileChooser openWindow=null;
	private GenericFileFilter fileFilter = 
		new GenericFileFilter("dgf", "Dynamic geometry files");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenFileAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// create file dialog if it doesn't exist
		if(openWindow==null){
			openWindow = new JFileChooser(".");
			openWindow.setFileFilter(fileFilter);
		}
		
		EuclideDoc doc=null;
		
		// Open dialog to choose the file, and create document from file
		int ret = openWindow.showOpenDialog(this.gui.getCurrentFrame());
		if(ret==JFileChooser.APPROVE_OPTION){
			File file = openWindow.getSelectedFile();
			if(file.isFile()){
				logger.info("Load file: " + file.getName());
				EuclideDGFReader reader = new EuclideDGFReader();
				try{
					doc = reader.readFile(file);
					doc.setName(file.getName());
					doc.setFileName(file.getCanonicalPath());
				}catch(IOException ex){
					logger.error("Problem when reading file " + file.getName());
					return;
				}
			}
		}

		if (doc==null) return;
		
		// add the new doc to application
		EuclideApp appli = gui.getAppli();
		appli.addDoc(doc);
		appli.setCurrentDoc(doc);
	}
}
