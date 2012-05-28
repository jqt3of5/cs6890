/*
 * File : ExportPointsAction.java
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
import io.csv.PointSetAsciiWriter;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polyline2D;
import model.EuclideFigure;

/**
 * Action creates a new document.
 * @author Legland
 */
public class ExportPointsAction extends EuclideAction {	

	private JFileChooser saveWindow=null;
	private GenericFileFilter fileFilter = 
		new GenericFileFilter("txt", "Text files");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportPointsAction(EuclideGui gui, String name){
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
		
		// extract the selected point set
		PointSet2D pointSet = new PointArray2D();
		
		// Try to find a selected point set
		for(EuclideFigure figure : view.getSelection()) {
			if(!figure.getGeometry().isDefined())
				continue;
			Shape2D geometry = figure.getGeometry().getShape();
			if(geometry instanceof PointSet2D) {
				pointSet.addPoints(((PointSet2D) geometry).getPoints());
			} else if (geometry instanceof Point2D) {
				pointSet.addPoint((Point2D) geometry);
			} else if (geometry instanceof Polyline2D) {
				pointSet.addPoints(((Polyline2D) geometry).getVertices());
			} else if (geometry instanceof Polygon2D) {
				pointSet.addPoints(((Polygon2D) geometry).getVertices());
			}
		}
		
		int ret = saveWindow.showSaveDialog(this.gui.getCurrentFrame());
		if(ret==JFileChooser.APPROVE_OPTION){
			// choose file name
			File file = saveWindow.getSelectedFile();
			
			// add a valid extension if not present
			if (!file.getName().contains("."))
				file = new File(file.getParentFile(), 
						file.getName().concat(".txt"));
				
			try{
				// export set of points
				new PointSetAsciiWriter().writeFile(pointSet, file);
				
			}catch(IOException ex){
				System.out.println("Problem with file saving");
				return;
			}
		}
	}
}
