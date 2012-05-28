/* file : ZoomFitHeightAction.java
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
package gui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import gui.*;

/**
 * Fit the zoom to view the entire height of the page.
 * @author dlegland
 */
public class ZoomFitHeightAction extends EuclideAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	public ZoomFitHeightAction(EuclideGui gui, String name){
		super(gui, name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		logger.info("zoom fit height");
		
		EuclideDocView docView = gui.getCurrentView();
		if (docView == null)
			return;
		
		EuclideSheetView view = docView.getCurrentSheetView();
		if (view == null)
			return;
		
		// extract the parent of the view
		Container parent = view.getParent();
		
		// get the size of the sheet, and of the container
		int parentHeight  = parent.getHeight();
		double height = view.getSheet().getHeight();
		
		// compute the ratio, to obtain the zoom
		double zoom = parentHeight/height;
		
		// set up the zoom
		view.setZoom(zoom);
		
		// update display
		view.invalidate();
		gui.getCurrentFrame().validate();
		gui.getCurrentFrame().repaint();
	}
}
