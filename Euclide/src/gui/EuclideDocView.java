/* file : EuclideView.java
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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;

import java.util.*;

import model.*;
import model.event.EuclideSheetEvent;
import model.event.EuclideSheetListener;
import math.geom2d.*;

/**
 * A view for an Euclide document. It contains one sheet view for each sheet
 * of the doc, organized in a tabbed panel, and an info bar.
 * It could contain also sheet thumbnail views, rulers...
 * @author dlegland
 */
public class EuclideDocView extends JPanel
implements ChangeListener, MouseMotionListener, EuclideSheetListener {
	
	private static final long serialVersionUID = 842519000769327662L;

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	/** reference document */
	EuclideDoc doc;
	
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel container = new JPanel();

	/** the label used to explain what to do with the tool.*/
	JLabel toolInfoLabel = new JLabel("This is a default message");
	JLabel coordinateLabel = new JLabel("x=0, y=0");

	// ============
	// Constructors
	
	/**
	 * Main constructor of an EuclideView. EuclideSheetViews are automatically
	 * added by extracting sheets contained in the document.
	 * @param doc
	 */
	public EuclideDocView(EuclideDoc doc){
		super();
		this.doc = doc;
		
		setLayout(new BorderLayout());

		// Add one tabbed panel for each sheet
		for(EuclideSheet sheet : doc.getSheets())
			this.addEuclideSheetView(new EuclideSheetView(sheet));
		
		// Creates info panel
		JPanel infoBarPanel = new JPanel();
		infoBarPanel.setLayout(new BoxLayout(infoBarPanel, BoxLayout.LINE_AXIS));
		infoBarPanel.add(toolInfoLabel);
		infoBarPanel.add(Box.createHorizontalGlue());
		infoBarPanel.add(coordinateLabel);
		infoBarPanel.invalidate();
		
		// puts all panels in the main panel
		add(tabbedPane, BorderLayout.CENTER);
		add(infoBarPanel, BorderLayout.SOUTH);
		
		//validate();
	}

	// ============
	// General methods
	
	public EuclideDoc getDoc(){
		return doc;
	}

	public void setToolInstruction(String instr){
		toolInfoLabel.setText(instr);
	}
	
	/**
	 * Change information relative to coordinate of mouse cursor.
	 * If point is null, label is not displayed.
	 * @param point coordinate to display
	 */
	public void setCoordinate(Point2D point){
		String format = "x=%1$6.2f y=%2$6.2f";
		if(point == null){
			coordinateLabel.setText("x=      y=      ");
		}else{
			coordinateLabel.setText(String.format(format, point.getX(), point.getY()));
		}
	}
	
	// ============
	// Sheets management
	
	public EuclideSheetView getCurrentSheetView(){
		JScrollPane scroll = (JScrollPane) tabbedPane.getSelectedComponent();
		if(scroll==null) return null;
		
		Object obj = ((JViewport)scroll.getComponent(0)).getComponent(0);
		return (EuclideSheetView) obj;
	}
	
	public void setCurrentSheetView(EuclideSheetView view){
		int pos = doc.getSheetPosition(view.getSheet());
		view.invalidate();
		tabbedPane.setSelectedIndex(pos);
	}
	
	public void addEuclideSheetView(EuclideSheetView view){
		EuclideSheet sheet = view.getSheet();
		
		sheet.addSheetListener(this);
		
		tabbedPane.addTab(sheet.getName(), new JScrollPane(view));
		view.addMouseMotionListener(this);
		view.invalidate();
		tabbedPane.invalidate();
	}
	
	public void removeEuclideSheetView(EuclideSheetView view){
		EuclideSheetView view2;
		int i=0;
		for(Component component : tabbedPane.getComponents()){
			view2 = (EuclideSheetView) ((JScrollPane) component).getViewport().getView();
			if(view == view2){
				tabbedPane.remove(i);
				tabbedPane.invalidate();
			}
			i++;
		}
	}
	
	
	public ArrayList<EuclideSheetView> getAllSheetViews(){
		ArrayList<EuclideSheetView> list = new ArrayList<EuclideSheetView>();
		
		for(int i=0; i<tabbedPane.getTabCount(); i++){
			JScrollPane scroll = (JScrollPane) tabbedPane.getComponentAt(i);
			Object obj = ((JViewport)scroll.getComponent(0)).getComponent(0);
			list.add((EuclideSheetView)obj);
		}
		return list;
	}
		
	// ============
	// Methods overriding JPanel methods
	
	@Override
	public void setSize(Dimension dim){
		super.setSize(dim);
		logger.info("set size of euclide view");
		JComponent component;
		for(int i=0; i<tabbedPane.getTabCount(); i++){
			component = (JComponent) tabbedPane.getComponentAt(i);
			component.setSize(component.getVisibleRect().getSize());
		}

		for(Component comp : tabbedPane.getComponents()) {
			logger.info("Component size: " + comp.getSize());
		}
	}
	
	@Override
	public void validate(){
		logger.trace("validate EuclideView");
		
		JPanel sheetPanel=null;

		EuclideSheetView sheetView = getCurrentSheetView();
		if(sheetView==null) return;
		sheetView.validate();
		
		if(doc.getSheetNumber()!=tabbedPane.getComponentCount()){
			tabbedPane.removeAll();
			for(EuclideSheet sheet : doc.getSheets()){
				sheetPanel = new EuclideSheetView(sheet);
				//sheetView.setSize(sheetView.getPreferredSize());
			
				tabbedPane.addTab(sheet.getName(), new JScrollPane(sheetPanel));
			}			
		}
		
		EuclideSheet currentSheet = sheetView.getSheet();
		if(doc.containsSheet(currentSheet)){
			int index = doc.getSheetPosition(currentSheet);
			tabbedPane.setSelectedIndex(index);
		}
		
		super.validate();
	}
	
	
	
	// ============
	// Implementation of ChangeListener
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent arg0) {
	}

	// ============
	// Implementation of SheetListener
	
	public void sheetModified(EuclideSheetEvent evt) {
		int nSheets = this.doc.getSheetNumber();
		EuclideSheet sheet;
		for (int i = 0; i < nSheets; i++) {
			sheet = doc.getSheet(i);
			tabbedPane.setTitleAt(i, sheet.getName());
		}
		
		this.tabbedPane.invalidate();
	}

	// ============
	// Implementation of MouseMotionListener
	
	public void mouseDragged(MouseEvent evt) {
		updateMousePosition(evt);
	}

	public void mouseMoved(MouseEvent evt) {
		updateMousePosition(evt);
	}
	
	private void updateMousePosition(MouseEvent evt) {
		EuclideSheetView view = (EuclideSheetView)evt.getSource();
		Point2D point = view.getPosition(evt.getX(), evt.getY(), true, true);
		this.setCoordinate(point);

		point = view.getPosition(evt.getX(), evt.getY(), false, false);
		view.mousePositionInUserUnit = point;
		
		view.mousePosition.setLocation(evt.getX(), evt.getY());
	
		view.repaint();
	}
}
