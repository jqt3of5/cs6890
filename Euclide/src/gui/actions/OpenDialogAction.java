/*
 * File : OpenDialogAction.java
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
 * Created on 09 Sep. 09
 */
 
package gui.actions;

import java.awt.event.ActionEvent;
import gui.*;

/**
 * Open About dialog.
 * @author Legland
 */
public class OpenDialogAction extends EuclideAction {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String dialogName = null;
	boolean modal = true;
	
	public OpenDialogAction(EuclideGui gui, String name, String dialogName){
		this(gui, name, dialogName, true);
	}
	
	public OpenDialogAction(EuclideGui gui, String name, String dialogName, boolean modal){
		super(gui, name);
		this.dialogName = dialogName;
		this.modal = modal;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		EuclideDialog dlg = gui.getDialog(dialogName);
		if(dlg != null) {
			dlg.updateWidgets();
			gui.showDialog(dlg);
		} else {
			System.out.println(
					"No dialog found for OpenDialogAction " + 
					this.getName());
		}
	}
}
