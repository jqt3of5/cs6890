/* file : EuclideSheet.java
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
 * Created on 25 déc. 2005
 *
 */
package model;

import java.awt.Dimension;
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.grid.*;
import math.geom2d.AffineTransform2D;
import model.event.*;


/**
 * @author dlegland
 */
public class EuclideSheet extends EuclideObject{

	//TODO: stores margin
	
	// ===================================================================
	// private class variables

	/** The document which contains the sheet. */
	private EuclideDoc doc = null;
	
	/** The set of layers which compose the sheet. */
	private LinkedList<EuclideLayer> layers = new LinkedList<EuclideLayer>();
	
	/** The current layer */
	private EuclideLayer currentLayer;
		
	/** 
	 * Width of the sheet, in user unit.
	 */
	private double width 	= 200.0; // in user unit
	
	/** 
	 * Height of the sheet, in user unit.
	 */
	private double height 	= 200.0; // in user unit
	
	private Margin margin = new Margin(0);
	
	
	/** 
	 * The clipping box of geometric objects, expressed in the user coordinate. 
	 * By default, the size of the page.
	 */
	private Box2D viewBox = new Box2D(-10, 10, -10, 10);
	
	private Grid2D grid 	= new SquareGrid2D(new Point2D(0, 0), 1);
	private Grid2D snapGrid = new SquareGrid2D(new Point2D(0, 0), .1);
	
	private boolean gridVisible = true;
	private boolean snapToGrid 	= true;
	
	/**
	 * The user to page transform. Converts objects in user coordinate to objects
	 * in page coordinate.
	 */
	private AffineTransform2D transform = new AffineTransform2D();
	
	private boolean viewPage = false;
	
	private boolean axesVisible = true;
	
	
	/** stores listeners for events propagation */
	ArrayList<EuclideSheetListener> listeners = new ArrayList<EuclideSheetListener>();
	
	
	// ===================================================================
	// constructor

	public EuclideSheet(EuclideDoc doc){
		this(doc, "");
	}
	
	public EuclideSheet(EuclideDoc doc, String name){
		super(name);
		this.doc=doc;
		this.margin = new Margin(20);
	}
	
	public EuclideSheet(EuclideDoc doc, String name, Collection<EuclideLayer> layers){
		this(doc, name);
		for(EuclideLayer layer : layers)
			addLayer(layer);
	}
	
	
	// ===================================================================
	// name management
	
	@Override
	public void setName(String name){
		super.setName(name);
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.NAME_CHANGED));
	}
	
	// ===================================================================
	// Document management 
	
	public EuclideDoc getDocument(){
		return doc;
	}
	
	public void setDocument(EuclideDoc document){
		this.doc = document;
	}

	
	// ===================================================================
	// dimension management
	
	/**
	 * Get page dimension of the document
	 * @return dimension in mm.
	 */
	public java.awt.Dimension getDimension(){
		return new java.awt.Dimension(
				(int)Math.ceil(width), 
				(int)Math.ceil(height));
	}

	
	public void setDimension(double width, double height){
		this.width = width;
		this.height = height;
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.SIZE_CHANGED));
	}
	
	public void setDimension(Dimension dim){
		this.width = dim.width;
		this.height = dim.height;
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.SIZE_CHANGED));
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}

	public Margin getMargin(){
		return margin;
	}
	
	public void setMargin(Margin margin){
		this.margin = margin;
	}
	
	
	// ===================================================================
	// grid management
	
	public Grid2D getGrid(){
		return grid;
	}
	
	public void setGrid(Grid2D grid){
		this.grid = grid;
	}
	
	public boolean isGridVisible(){
		return gridVisible;
	}
	
	public void setGridVisible(boolean b){
		this.gridVisible = b;
	}
	
	
	public Grid2D getSnapGrid(){
		return snapGrid;
	}
	
	public void setSnapGrid(Grid2D grid){
		this.snapGrid = grid;
	}
	
	public boolean isSnapToGrid(){
		return snapToGrid;
	}
	
	public void setSnapToGrid(boolean b){
		this.snapToGrid = b;
	}
	
	
	// ===================================================================
	// layers management
	
	public void addLayer(EuclideLayer layer){
		layers.add(layer);
		if(layers.size()==1)
			currentLayer = layer;
		layer.setSheet(this);
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.LAYER_ADDED));
	}

	public void addLayer(int position, EuclideLayer layer){
		layers.add(position, layer);
		layer.setSheet(this);
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.LAYER_ADDED));
	}
	
	/**
	 * Creates a new layer with a default name, and adds it to the end of the
	 * layer list.
	 * @return the created layer
	 */
	public EuclideLayer addNewLayer(){
		int i=1;
		String layerName;
		boolean ok = true;
		do{
			// Create a name based on current index
			layerName = String.format("Layer %d", i++);
			ok = true;
			
			// Check the name does not already exist
			for(EuclideLayer layer : this.getLayers()){
				if(layerName.equals(layer.getName())){
					ok = false;
					break;
				}
			}
		}while(!ok);
		
		EuclideLayer layer = new EuclideLayer(layerName);
		
		// add layer to current sheet
		layers.add(layer);
		layer.setSheet(this);
		
		// set up layer tag
		String tag = doc.getNextFreeTag("layer%d");
		layer.setTag(tag);
		
		// add to document list of tagged objects
		doc.addTaggedObject(layer);
	
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.LAYER_ADDED));
		
		return layer;
	}

	/**
	 * Remove a layer, and change current layer if it was removed. It there
	 * is no more layer in the sheet, current layer is set to null.
	 */
	public void removeLayer(EuclideLayer layer){
		boolean res = layers.remove(layer);
		if(!res) return;
		
		layer.setSheet(null);
		
		// update current layer
		if(currentLayer==layer){
			if(layers.isEmpty())
				currentLayer = null;
			else
				currentLayer = layers.getFirst();
		}
		
		// fire event
		this.fireEvent(new SheetModifiedEvent(this, 
				EuclideSheetEvent.LAYER_REMOVED));
	}
	
	public boolean containsLayer(EuclideLayer layer){
		return layers.contains(layer);
	}
	
	public void setCurrentLayer(EuclideLayer layer){
		if(!layers.contains(layer)) return;
		currentLayer = layer;
		this.fireEvent(new SheetModifiedEvent(this, 
				EuclideSheetEvent.CURRENT_LAYER_CHANGED));
	}
	
	public Collection<EuclideLayer> getLayers(){
		return layers;
	}

	public int getLayerNumber(){
		return layers.size();
	}

	public int getLayerPosition(EuclideLayer layer){
		return layers.indexOf(layer);
	}
	
	public void setLayerPosition(EuclideLayer layer, int newPos){
		if(!layers.contains(layer)) return;
		if(newPos<0) throw new IndexOutOfBoundsException();
		if(newPos>layers.size()-1) throw new IndexOutOfBoundsException();
		layers.remove(layer);
		layers.add(newPos, layer);
		this.fireEvent(new SheetModifiedEvent(this, EuclideSheetEvent.LAYER_MOVED));
	}
	
	public EuclideLayer getLayer(int index){
		return layers.get(index);
	}
	
	public EuclideLayer getCurrentLayer(){
		return currentLayer;
	}
	
	

	// ===================================================================
	// Page management

	/**
	 * @return the clip
	 */
	public Box2D getViewBox() {
		return viewBox;
	}

	/**
	 * @param box the clip to set
	 */
	public void setViewBox(Box2D box) {
		this.viewBox = box;
	}

	/**
	 * @return the transform
	 */
	public AffineTransform2D getTransform() {
		return transform;
	}

	/**
	 * @param transform the transform to set
	 */
	public void setTransform(AffineTransform2D transform) {
		this.transform = transform;
	}

	/**
	 * @return the viewPage
	 */
	public boolean isViewPage() {
		return viewPage;
	}

	/**
	 * @param viewPage the viewPage to set
	 */
	public void setViewPage(boolean viewPage) {
		this.viewPage = viewPage;
	}

	
	// ===================================================================
	// events management

	/**
	 * @return the drawAxis
	 */
	public boolean isAxesVisible() {
		return axesVisible;
	}

	/**
	 * @param drawAxis the drawAxis to set
	 */
	public void setAxesVisible(boolean axesVisible) {
		this.axesVisible = axesVisible;
	}

	public void addSheetListener(EuclideSheetListener listener){
		listeners.add(listener);
	}
	
	public void removeSheetListener(EuclideSheetListener listener){
		listeners.remove(listener);
	}
	
	public void removeAllListeners(){
		listeners.clear();
	}
	
	private void fireEvent(EuclideSheetEvent evt){
		for(EuclideSheetListener listener : listeners)
			listener.sheetModified(evt);
	}
	
	private class SheetModifiedEvent extends EuclideSheetEvent{
		EuclideSheet sheet;
		int state = 0;
				
		public SheetModifiedEvent(EuclideSheet sheet, int state){
			this.sheet = sheet;
			this.state = state;
		}
		
		@Override
		public EuclideSheet getSheet(){
			return sheet;
		}
		
		@Override
		public int getState(){
			return state;
		}
	}
	
	/**
	 * Margin of the page in each direction.
	 * @author dlegland
	 *
	 */
	public class Margin {
		public double left 		= 0;
		public double right 	= 0;
		public double top 		= 0;
		public double bottom 	= 0;
		
		public Margin(double left, double right, double top, double bottom){
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
		
		/** use the same margin in all directions */
		public Margin(double val){
			this(val, val, val, val);
		}
		
		/** use null margin in each direction */
		public Margin(){
			this(0,0,0,0);
		}
		
	}
}
