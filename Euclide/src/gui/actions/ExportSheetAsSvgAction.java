/*
 * File : ExportSheetAsSvgAction.java
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

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;

import gui.*;
import gui.util.*;

import io.svg.*;

/**
 * Action creates a new document.
 * @author Legland
 */
public class ExportSheetAsSvgAction extends EuclideAction {	

	private JFileChooser saveWindow=null;
	private GenericFileFilter fileFilter = new GenericFileFilter("dgf", "Dynamic geometry files");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportSheetAsSvgAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
		
		// create file dialog if it doesn't exist
		if(saveWindow==null){
			saveWindow = new JFileChooser(".");
			saveWindow.setFileFilter(fileFilter);
		}
		
		int ret = saveWindow.showSaveDialog(this.gui.getCurrentFrame());
		if(ret==JFileChooser.APPROVE_OPTION){
			// choose file name
			File file = saveWindow.getSelectedFile();
			
			// add a valid extension if not present
			if (!file.getName().contains("."))
				file = new File(file.getParentFile(), file.getName().concat(".svg"));
				
			try{
				// Saves file
				EuclideSvgWriter writer = new EuclideSvgWriter();
				writer.writeFile(view.getSheet(), file);
			}catch(IOException ex){
				System.out.println("Problem with file saving");
				return;
			}
		}
	}
}
