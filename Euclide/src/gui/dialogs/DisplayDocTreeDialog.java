/* file : DisplayDocTreeDialog.java
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
package gui.dialogs;

import gui.EuclideDialog;
import gui.EuclideGui;
import gui.panels.EuclideDocTreePanel;
import model.EuclideDoc;

/**
 * @author dlegland
 */
public class DisplayDocTreeDialog extends EuclideDialog {

	private static final long serialVersionUID = 1L;

	EuclideDoc doc;
	EuclideDocTreePanel treePanel;
	
	public DisplayDocTreeDialog(EuclideGui gui){
		this(gui, "Document tree", false);
	}

	public DisplayDocTreeDialog(EuclideGui gui, String title, boolean modal){
		super(gui, title, modal);
		
		this.doc = gui.getCurrentDoc();
		
		treePanel = new EuclideDocTreePanel(gui, doc);
		
		this.setContentPane(treePanel);
		this.setSize(300, 400);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			treePanel.updateTree();
		}
		super.setVisible(visible);
	}
}
