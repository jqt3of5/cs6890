/*
 * File : ImportPointsAction.java
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
import gui.EuclideDocView;
import gui.EuclideGui;
import gui.EuclideSheetView;
import gui.util.GenericFileFilter;
import io.csv.PointSetAsciiReader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import math.geom2d.Point2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;
import model.EuclideLayer;
import model.EuclideFigure;
import dynamic.shapes.ShapeWrapper2D;

/**
 * Action creates a new document.
 * @author Legland
 */
public class ImportPointsAction extends EuclideAction {	

	private JFileChooser openWindow=null;
	private GenericFileFilter fileFilter = 
		new GenericFileFilter("txt", "Text files");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImportPointsAction(EuclideGui gui, String name){
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
		if(openWindow==null){
			openWindow = new JFileChooser(".");
			openWindow.setFileFilter(fileFilter);
		}
		
		PointSet2D pointSet = new PointArray2D(0);
		
		int ret = openWindow.showSaveDialog(this.gui.getCurrentFrame());
		if(ret==JFileChooser.APPROVE_OPTION){
			// choose file name
			File file = openWindow.getSelectedFile();
			
			try{
				// import set of points
				pointSet = new PointSetAsciiReader().readFile(file);
				
			}catch(IOException ex){
				System.out.println("Problem with file saving");
				return;
			}
			
			if(pointSet == null)
				return;
			
			EuclideLayer layer = view.getSheet().getCurrentLayer();
			if (layer == null)
				return;
			
			for(Point2D point : pointSet) {
				EuclideFigure shape = gui.getAppli().createEuclideShape(
						new ShapeWrapper2D(point));
				layer.addFigure(shape);
			}
			
			gui.getAppli().getCurrentDoc().setModified(true);
			gui.getCurrentFrame().updateTitle();
		}
	}
}
