/* file : EuclideDoc.java
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

import gui.macros.SimpleMacro;

import java.awt.Color;
import java.util.*;

import model.event.EuclideDocEvent;
import model.event.EuclideDocListener;
import model.style.DefaultDrawStyle;
import model.style.DrawStyle;
import model.style.NamedDrawStyle;

import org.apache.log4j.Logger;

import dynamic.*;
import dynamic.measures.Constant2D;

/**
 * A document contains several things:
 * <ul>
 * <li>a set of sheets, which contain a set of layers. Each layer contains a
 * set of shapes.</li>
 * <li>a set of EuclideTransform2D</li>
 * <li>a set of EuclideMeasure2D</li>
 * <li>a set of EuclideVector2D</li>
 * <li>a set of EuclidePredicate2D</li>
 * </ul>
 * Moreover, the document manages a set of dynamic objects. This set is
 * updated when adding new shape, measure, transform, or predicate.
 * 
 * @author dlegland
 */
public class EuclideDoc extends DynamicObjectsManager {
	
	// ===================================================================
	// static variables
	
	private final static String defaultDocName = "NoName";

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");


	// ===================================================================
	// inner class variables

	/**
	 * name of the author
	 */
	private String authorName = "";

	/**
	 * date of last modification
	 */
	private Date creationDate;

	/**
	 * Complete file name of the doc.
	 */
	private String fileName = null;
		
	/** 
	 *  this flag indicates if the document has been modified since last save.
	 *  It should be reset to false when saving.
	 */
	private boolean modified = false;


	/**
	 * The sheets of the document.
	 */
	private LinkedList<EuclideSheet> sheets = new LinkedList<EuclideSheet>();

	/** Event listeners*/
	private ArrayList<EuclideDocListener> listeners =
		new ArrayList<EuclideDocListener>();
	
	/**
	 * Stores the layers where each shape is stored.
	 */
	private HashMap<EuclideFigure, EuclideLayer> figureLayer = 
		new HashMap<EuclideFigure, EuclideLayer>();

	/**
	 * Geometric shapes stored in the document. Each DynamicShape2D can be 
	 * referenced by one or several EuclideShape, hence the Collection.
	 */
	private HashMap<DynamicShape2D, Collection<EuclideFigure>> shapesMap =
		new HashMap<DynamicShape2D, Collection<EuclideFigure>>();

	/**
	 * Geometric shape constructions stored in the document. 
	 */
	private ArrayList<DynamicShape2D> shapes = 
		new ArrayList<DynamicShape2D>();

	/**
	 * Geometric vectors stored in the document. 
	 */
	private ArrayList<DynamicVector2D> vectors = 
		new ArrayList<DynamicVector2D>();

	/**
	 * Geometric transforms stored in the document. 
	 */
	private ArrayList<DynamicTransform2D> transforms = 
		new ArrayList<DynamicTransform2D>();

	/**
	 * Geometric measures stored in the document. 
	 */
	private ArrayList<DynamicMeasure2D> measures = 
		new ArrayList<DynamicMeasure2D>();
	
	/**
	 * Geometric predicates stored in the document. 
	 */
	private ArrayList<DynamicPredicate2D> predicates = 
		new ArrayList<DynamicPredicate2D>();
	
	
	/**
	 * Constants measures stored in the document, and that can be edited.
	 * These constants are also listed in the 'measures' set.
	 */
	private ArrayList<Constant2D> constants = 
		new ArrayList<Constant2D>();
	
	/**
	 * Macro stored in the document.
	 */
	private ArrayList<SimpleMacro> macros = 
		new ArrayList<SimpleMacro>();
	

	/**
	 * The draw style for new created shapes.
	 */
	private DrawStyle drawStyle;
	
	/** 
	 * Some styles to draw figures.
	 */
	private ArrayList<NamedDrawStyle> styles = 
		new ArrayList<NamedDrawStyle>();
	
	
	// ===================================================================
	// constructors

	public EuclideDoc(){
		this(defaultDocName);
	}
	
	public EuclideDoc(String name){
		super(name, "doc");
		initDrawStyles();
	}

	private void initDrawStyles() {
		// init default draw styles
		DefaultDrawStyle style = new DefaultDrawStyle();
		style.setMarkerColor(Color.BLACK);
		style.setMarkerFillColor(Color.RED);
		style.setMarkerSize(3);
		style.setMarkerLineWidth(1);
		style.setLineColor(Color.BLUE);
		style.setFillType(DrawStyle.FillType.COLOR);
		style.setFillColor(Color.CYAN);
		style.setFillTransparency(.5);
		this.drawStyle = style;
	}
	
	// ===================================================================
	// constructors

	public final static EuclideDoc createDefaultDoc(){
		// Create the doc
		EuclideDoc doc = new EuclideDoc();
		
		// Create a new sheet
		EuclideSheet sheet = doc.addNewSheet();

		// create a new layer in current sheet
		EuclideLayer layer = sheet.addNewLayer();
		sheet.setCurrentLayer(layer);

		// return created doc
		return doc;
	}
		

	// ===================================================================
	// processing of document attributes

	@Override
	public void setName(String name){
		super.setName(name);
		this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
		this.fireEvent(new DocumentModifiedEvent(this));
	}

	public String getFileName(){
		return fileName;
	}
	
	/**
	 * @param authorName The authorName to set.
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
		this.fireEvent(new DocumentModifiedEvent(this));
	}

	/**
	 * @return Returns the authorName.
	 */
	public String getAuthorName() {
		return this.authorName;
	}

	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	/**
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * changes the flag indicating that the document has been modified.
	 * @param modified a boolean with value true if document as been modified
	 */
	public void setModified(boolean modified){
		this.modified = modified;
	}
	
	/**
	 * returns the flag indicating whether the document has been modified
	 * @return true if the document was modified
	 */
	public boolean isModified(){
		return modified;
	}
	
	
	// ===================================================================
	// sheets management
	
	/**
	 * returns the inner collection of sheets.
	 * @return the inner collection of sheets.
	 */
	public Collection<EuclideSheet> getSheets(){
		return sheets;
	}
	
	public int getSheetPosition(EuclideSheet sheet){
		return sheets.indexOf(sheet);
	}

	public int getSheetNumber(){
		return sheets.size();
	}

	public EuclideSheet getSheet(int index){
		return sheets.get(index);
	}

	public EuclideSheet getFirstSheet(){
		return sheets.getFirst();
	}
	
	public void addSheet(EuclideSheet sheet){
		sheet.setDocument(this);
		sheets.addLast(sheet);
		addTaggedObject(sheet);
		for (EuclideLayer layer : sheet.getLayers()) {
			addTaggedObject(layer);
		}
		//this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	public void addSheet(int position, EuclideSheet sheet){
		sheets.add(position, sheet);
		sheet.setDocument(this);
		//this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	/**
	 * Creates a new sheet with a default name, and adds it to the end of the
	 * sheet list.
	 * @return the created sheet
	 */
	public EuclideSheet addNewSheet(){
		int i=1;
		String sheetName;
		boolean ok = true;
		do{
			// Create a name based on current index
			sheetName = String.format("Sheet %d", i++);
			ok = true;
			
			// Check the name does not already exist
			for(EuclideSheet sheet : this.getSheets()){
				if(sheetName.equals(sheet.getName())){
					ok = false;
					break;
				}
			}
		}while(!ok);
		
		EuclideSheet sheet = new EuclideSheet(this, sheetName);
		
		// add sheet to current document
		sheets.add(sheet);
		
		// set up sheet tag
		String tag = this.getNextFreeTag("sheet%d");
		sheet.setTag(tag);
		
		// add to document list of tagged objects
		this.addTaggedObject(sheet);
	
		//this.fireEvent(new DocumentModifiedEvent(this));
		
		return sheet;
	}

	public void removeSheet(EuclideSheet sheet){
		sheets.remove(sheet);
		sheet.setDocument(null);
		//this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	public boolean containsSheet(EuclideSheet sheet){
		return sheets.contains(sheet);
	}	

	// ===================================================================
	// euclide figure  management

	/**
	 * Returns all the shapes contained in the document. The set of shapes is
	 * computed by iterating on sheets, then on layers.
	 */
	public Collection<EuclideFigure> getFigures(){
		ArrayList<EuclideFigure> figures = new ArrayList<EuclideFigure>();
		
		// for each layer of each sheet...
		for(EuclideSheet sheet : this.sheets)
			for(EuclideLayer layer : sheet.getLayers())
				figures.addAll(layer.getShapes()); // add all shapes in the layer
				
		return figures;
	}

	/**
	 * Adds a figure to a specified layer.
	 */
	public void addFigure(EuclideFigure item, EuclideLayer layer){
		if(layer==null) return;
		setModified(true);
		
		// add to specified layer
		layer.addFigure(item);
		figureLayer.put(item, layer);
		
		DynamicShape2D dynamic = item.getGeometry();
		Collection<EuclideFigure> set;
		if (shapesMap.containsKey(dynamic)){
			shapesMap.get(dynamic).add(item);
		} else {
			set = new ArrayList<EuclideFigure>();
			set.add(item);
			shapesMap.put(dynamic, set);
		}
		
		// check tag is defined, otherwise create a default tag
		if (item.getTag() == null) {
			String tag = this.getNextFreeTag("item%d");
			logger.warn("Created default tag '" + tag + "' for a figure");
			item.setTag(tag);
		}
		
		// add to the list of tagged objects
		addTaggedObject(item);
		
		// add to dynamic hierarchy
		if(!containsTaggedObject(dynamic.getTag())) {
			logger.warn("Geometry of item " + item.getTag() + 
					" is not contained in document");
			addDynamicShape(item.getGeometry());
		}
		
		// fire event
	//	this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	/**
	 * Changes the layer which contains a given shape.
	 */
	public void setFigureLayer(EuclideFigure shape, EuclideLayer layer){
		if(layer==null) return;
		setModified(true);
		
		// remove from original layer
		figureLayer.get(shape).removeFigure(shape);
		
		// add to specified layer
		layer.addFigure(shape);
		figureLayer.put(shape, layer);

		// fire event
		this.fireEvent(new DocumentModifiedEvent(this));
	}
	
	/**
	 * Return the layer containing the specified shape
	 * @param shape a shape
	 * @return the layer which contains the shape
	 */
	public EuclideLayer getFigureLayer(EuclideFigure shape){
		return figureLayer.get(shape);
	}
	

	// ===================================================================
	// dynamic shapes management

	public boolean addDynamicShape(DynamicShape2D shape){
		setModified(true);
		if(shapes.contains(shape)) return false;
		shapes.add(shape);
		
		// add to dynamic hierarchy
		addDynamicObject(shape);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeDynamicShape(DynamicShape2D shape){
		setModified(true);
		if(!shapes.contains(shape)) return false;
				
		// for each dynamic object, removes EuclideObjects which contain it.
		Collection<DynamicObject2D> descendants = getDescendants(shape);
		descendants.add(shape);
		for(DynamicObject2D dynamic : descendants){
			if(dynamic instanceof DynamicShape2D){
				// Remove the figure(s) that use this shape as geometry
				Collection<EuclideFigure> figures = shapesMap.get(dynamic);
				for(EuclideFigure item : figures)
					this.figureLayer.get(item).removeFigure(item);
				shapes.remove(dynamic);
			} else if(dynamic instanceof DynamicMeasure2D){
				measures.remove(dynamic);
			} else if(dynamic instanceof DynamicTransform2D){
				transforms.remove(dynamic);
			} else if(dynamic instanceof DynamicVector2D){
				vectors.remove(dynamic);
			} else if(dynamic instanceof DynamicPredicate2D){
				predicates.remove(dynamic);
			}
		}
		
		// remove from dynamic hierarchy
		removeDynamicObject(shape);

		//this.fireEvent(new DocumentModifiedEvent(this));

		return true;
	}

	/**
	 * Returns all shape constructions in the document.
	 */
	public Collection<DynamicShape2D> getDynamicShapes(){
		return shapes;
	}
	
	// ===================================================================
	// dynamic transforms  management

	public boolean addTransform(DynamicTransform2D transform){
		setModified(true);
		if(transforms.contains(transform)) return false;
		transforms.add(transform);
		
		// add to dynamic hierarchy
		addDynamicObject(transform);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeTransform(DynamicTransform2D transform){
		setModified(true);
		if(!transforms.contains(transform))
			return false;
		
		transforms.remove(transform);
		
		// remove from dynamic hierarchy
		removeDynamicObject(transform);

		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}

	public Collection<DynamicTransform2D> getTransforms(){
		return transforms;
	}
	
	
	// ===================================================================
	// dynamic measures management

	public boolean addMeasure(DynamicMeasure2D measure){
		setModified(true);
		if(measures.contains(measure)) return false;
		measures.add(measure);
		
		if(measure instanceof Constant2D)
			constants.add((Constant2D) measure);
		
		// add to dynamic hierarchy
		addDynamicObject(measure);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeMeasure(DynamicMeasure2D measure){
		setModified(true);
		if(!measures.contains(measure)) return false;
		
		//this.fireEvent(new DocumentModifiedEvent(this));

		measures.remove(measure);
		if(measure instanceof Constant2D)
			constants.remove(measure);
		
		// remove from dynamic hierarchy
		removeDynamicObject(measure);

		setModified(true);
		return true;
	}
	
	public Collection<DynamicMeasure2D> getMeasures(){
		return measures;
	}
	
	// ===================================================================
	// constants management

	public Collection<Constant2D> getConstants(){
		return constants;
	}
	
	// ===================================================================
	// dynamic vectors management

	public boolean addVector(DynamicVector2D vector){
		setModified(true);
		if(vectors.contains(vector)) return false;
		vectors.add(vector);

		// add to dynamic hierarchy
		addDynamicObject(vector);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeVector(DynamicVector2D vector){
		setModified(true);
		if(!vectors.contains(vector))
			return false;
		
		vectors.remove(vector);

		// remove from dynamic hierarchy
		removeDynamicObject(vector);

		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public Collection<DynamicVector2D> getVectors(){
		return vectors;
	}


	// ===================================================================
	// dynamic predicates management

	public boolean addPredicate(DynamicPredicate2D predicate){
		setModified(true);
		if(predicates.contains(predicate))
			return false;
		
		predicates.add(predicate);

		// add to dynamic hierarchy
		addDynamicObject(predicate);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removePredicate(DynamicPredicate2D predicate){
		setModified(true);
		if(!predicates.contains(predicate))
			return false;
		
		predicates.remove(predicate);
		
		// remove from dynamic hierarchy
		removeDynamicObject(predicate);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public Collection<DynamicPredicate2D> getPredicates(){
		return predicates;
	}
	
	
	// ===================================================================
	// macros management

	public boolean addMacro(SimpleMacro macro){
		setModified(true);
		if(macros.contains(macro)) return false;
		macros.add(macro);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeMacro(SimpleMacro macro){
		setModified(true);
		if(!macros.contains(macro)) return false;
		
		//this.fireEvent(new DocumentModifiedEvent(this));

		macros.remove(macro);
		
		setModified(true);
		return true;
	}
	
	public Collection<SimpleMacro> getMacros(){
		return Collections.unmodifiableList(macros);
	}
	
	
	// ===================================================================
	// styles management

	public DrawStyle getDrawStyle(){
		return drawStyle;
	}
	
	public void setDrawStyle(DrawStyle drawStyle){
		this.drawStyle = drawStyle;
	}
		

	public boolean addStyle(NamedDrawStyle style){
		setModified(true);
		if(styles.contains(style)) return false;
		styles.add(style);
		
		//this.fireEvent(new DocumentModifiedEvent(this));
		return true;
	}
	
	public boolean removeStyle(NamedDrawStyle style){
		if(!styles.contains(style)) return false;
		
		//this.fireEvent(new DocumentModifiedEvent(this));

		styles.remove(style);
		
		setModified(true);
		return true;
	}
	
	public Collection<NamedDrawStyle> getStyles(){
		return Collections.unmodifiableList(styles);
	}
	

	

	// ===================================================================
	// listeners management

	public void addDocumentListener(EuclideDocListener listener){
		listeners.add(listener);
	}

	public void removeDocumentListener(EuclideDocListener listener){
		listeners.remove(listener);
	}
	
	public void removeAllDocumentListeners(){
		listeners.clear();
	}
	
	private void fireEvent(EuclideDocEvent evt){
		for(EuclideDocListener listener : listeners)
			listener.documentModified(evt);
	}
	//changed to static
	private static class DocumentModifiedEvent extends EuclideDocEvent{
		int state = 0;
		EuclideDoc doc;
		
		public DocumentModifiedEvent(EuclideDoc doc){
			this.doc = doc;
		}
		
		@Override
		public EuclideDoc getDocument(){
			return doc;
		}
		
		@Override
		public int getState(){
			return state;
		}
	}
}
