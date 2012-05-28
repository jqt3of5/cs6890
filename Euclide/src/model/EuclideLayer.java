/* file : EuclideLayer.java
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

import java.util.*;
import model.event.*;

/**
 * @author dlegland
 */
public class EuclideLayer extends EuclideObject {

	EuclideSheet sheet = null;

	private LinkedList<EuclideFigure> figures = new LinkedList<EuclideFigure>();

	private boolean visible = true;

	private boolean editable = true;

	// Event propagations
	ArrayList<EuclideLayerListener> listeners = new ArrayList<EuclideLayerListener>();

	// ===================================================================
	// constructors

	/**
	 * empty constructor
	 */
	public EuclideLayer() {
	}

	/**
	 * constructor specifying name
	 */
	public EuclideLayer(String name) {
		super(name);
	}

	// ===================================================================
	// sheet management (package only)

	public EuclideSheet getSheet() {
		return sheet;
	}

	public void setSheet(EuclideSheet sheet) {
		this.sheet = sheet;
	}

	// ===================================================================
	// layer description

	@Override
	public void setName(String name) {
		super.setName(name);
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.NAME_CHANGED));
	}

	/**
	 * @return Returns the editable.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.EDITABLE_CHANGED));
	}

	/**
	 * @return Returns the visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.VISIBLE_CHANGED));
	}

	// ===================================================================
	// manage shapes in layer

	/**
	 * Add the shape at the end of the list. It will be the last one to be
	 * drawn.
	 */
	public void addFigure(EuclideFigure shape) {
		figures.addLast(shape);
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.ELEMENT_ADDED));
	}

	public void addFigure(int position, EuclideFigure shape) {
		figures.add(position, shape);
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.ELEMENT_ADDED));
	}

	public void removeFigure(EuclideFigure shape) {
		figures.remove(shape);
		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.ELEMENT_REMOVED));
	}

	public boolean containsFigure(EuclideFigure shape) {
		return figures.contains(shape);
	}

	public Collection<EuclideFigure> getShapes() {
		return figures;
	}

	public int getShapeNumber() {
		return figures.size();
	}

	public int getShapePosition(EuclideFigure shape) {
		return figures.indexOf(shape);
	}

	public EuclideFigure getShape(int index) {
		return figures.get(index);
	}

	/**
	 * Change the current index of the shape.
	 * 
	 * @param shape the shape to change
	 * @param indexthe new index of the shape
	 */
	public void setShapeIndex(EuclideFigure shape, int index) {
		int index0 = figures.indexOf(shape);
		if (index0==-1)
			return;

		figures.remove(index0);
		index = Math.max(Math.min(index, figures.size()), 0);
		figures.add(index, shape);

		this.fireEvent(new LayerModifiedEvent(this,
				EuclideLayerEvent.ELEMENT_MOVED));
	}

	// ===================================================================
	// events management

	public void addLayerListener(EuclideLayerListener listener) {
		listeners.add(listener);
	}

	public void removeLayerListener(EuclideLayerListener listener) {
		listeners.remove(listener);
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	private void fireEvent(EuclideLayerEvent evt) {
		for (EuclideLayerListener listener : listeners)
			listener.layerModified(evt);
	}

	private class LayerModifiedEvent extends EuclideLayerEvent {

		int state = 0;
		EuclideLayer layer;

		public LayerModifiedEvent(EuclideLayer layer, int state) {
			this.layer = layer;
			this.state = state;
		}

		@Override
		public EuclideLayer getLayer() {
			return layer;
		}

		@Override
		public int getState() {
			return state;
		}
	}
}
