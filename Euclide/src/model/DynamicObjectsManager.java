/**
 * File: 	DynamicObjectsManager.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 24 févr. 10
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import model.event.DynamicHierarchyEvent;
import model.event.DynamicHierarchyListener;

import org.apache.log4j.Logger;

import dynamic.DynamicArray2D;
import dynamic.DynamicObject2D;


/**
 * @author dlegland
 *
 */
public class DynamicObjectsManager extends EuclideObject {
	
	// ===================================================================
	// static variables
	
	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");


	// ===================================================================
	// inner class variables

	/**
	 * The set of all tagged objects: dynamic objects (constructions),
	 * but also sheets, layers, macros... indexed by their tag.
	 */
	private HashMap<String, EuclideObject> taggedObjects = 
		new HashMap<String, EuclideObject>();
	
	/**
	 * An ordered list of DynamicShape2D, used to update geometry of different
	 * EuclideShape.
	 */
	private ArrayList<DynamicObject2D> dynamicObjects = 
		new ArrayList<DynamicObject2D>();
	
	/**
	 * Manage dependencies between objects.
	 * Each key is a DynamicObject2D. 
	 * Each value is a collection of DynamicObject2D which depend on the key.
	 */
	private HashMap<DynamicObject2D, Collection<DynamicObject2D>> childrenMap =
		new HashMap<DynamicObject2D, Collection<DynamicObject2D>>();

	/** Event listeners*/
	private ArrayList<DynamicHierarchyListener> listeners =
		new ArrayList<DynamicHierarchyListener>();
	

	// ===================================================================
	// constructor
	
	public DynamicObjectsManager(String name, String tag){
		super(name, tag);
	}
	
	
	// ===================================================================
	// tagged objects management
	
	public void addTaggedObject(EuclideObject object) {
		// get object's tag
		String tag = object.getTag();
		
		// check tag is defined, otherwise create a default tag
		if (tag == null) {
			tag = setUpDefaultTag(object);
			String msg = String.format(
					"Created default tag '%s' for an object of 'class %s'", 
					tag, object.getClass().getName());
			logger.warn(msg);
		}
		
		// Add object to dynamic and euclide list.
		taggedObjects.put(tag, object);		
	}
	
	/**
	 * Sets up the tag of the object, and returns the created tag.
	 */
	private String setUpDefaultTag(EuclideObject object) {
		// find tag pattern depending on image class
		String pattern = "obj%d";
		if (object instanceof DynamicArray2D)
			pattern = "array%d";
		else if (object instanceof EuclideLayer)
			pattern = "layer%d";
		else if (object instanceof EuclideSheet)
			pattern = "sheet%d";
		
		// create tag
		String tag = this.getNextFreeTag(pattern);
		object.setTag(tag);
		
		return tag;
	}
	
	public void removeTaggedObject(EuclideObject object) {
		taggedObjects.remove(object.getTag());
		if (dynamicObjects.contains(object))
			dynamicObjects.remove(object);
	}
	
	public EuclideObject getTaggedObject(String tag) {
		return taggedObjects.get(tag);
	}
	
	public boolean containsTaggedObject(String tag) {
		return taggedObjects.containsKey(tag);
	}
	
	/**
	 * Returns the first new tag that match a pattern like "item%d".
	 * The string pattern must contains one wildcard "%d" or "%2d".
	 * @throws InvalidArgumentException if the character '%' is not present
	 */
	public String getNextFreeTag(String pattern) {
		// input check
		if (!pattern.contains("%"))
			throw new IllegalArgumentException(
					"Pattern argument '" + pattern
					+ "' does not contain the '%' character");
		
		// find the first free number corresponding to the base name
		int num = 1;
		String tag;
		while (true) {
			tag = String.format(pattern, num);
			if (!taggedObjects.containsKey(tag))
				break;
			num++;
		}
		return tag;
	}
	
	// ===================================================================
	// dynamic objects  management

	/**
	 * Adds a dynamic object, and adds recursively all its ancestors if they
	 * do not already exist in document.
	 * @param object
	 */
	public void addDynamicObject(DynamicObject2D object){
		// Eventually add parents of object
		addObjectParents(object);
		
		// Add object to list of tagged objects
		addTaggedObject(object);
		
		// tag is now defined
		dynamicObjects.add(object);
		
		// add current object with list of parents and empty list of children
		childrenMap.put(object, new ArrayList<DynamicObject2D>());
		
		// add current object to the children list of each parent
		for(DynamicObject2D parent : object.getParents())
			childrenMap.get(parent).add(object);
		
		// fire event
		this.fireEvent(new DynamicHierarchyModifiedEvent(this));
	}
	
	private void addObjectParents(DynamicObject2D object) {
		// Extract parents of object
		Collection<? extends DynamicObject2D> parents = object.getParents();
		if(parents == null) {
			String msg = String.format(
					"Object with tag '%s' (class '%s') has null pointer for parents",
					object.getTag(), object.getClass().getSimpleName());
			logger.warn(msg);
			return;
		}
		
		// recursively adds each parents if they are not contained
		for(DynamicObject2D parent : parents)
			if(!dynamicObjects.contains(parent))
				addDynamicObject(parent);
	}
	
	/**
	 * Shortcut method to set up the tag of a dynamic object, then to
	 * add the object to the list of constructions.
	 */
	public void addDynamicObject(DynamicObject2D object, String tag) {
		object.setTag(tag);
		addDynamicObject(object);
	}
	
	/**
	 * get all the descendants of a given dynamic object.
	 * @param object the object whose descendants will be computed
	 * @return the list of descendants of the object
	 */
	public Collection<DynamicObject2D> getDescendants(DynamicObject2D object){		
		ArrayList<DynamicObject2D> list = new ArrayList<DynamicObject2D>();
		this.addDescendants(object, list);
		return list;
	}

	/**
	 * Get the children of the object, i.e. the dynamic objects which depend
	 * directly on the given object.
	 * @param object the object to consider
	 * @return the children of the object
	 */
	public Collection<DynamicObject2D> getChildren(DynamicObject2D object){		
		ArrayList<DynamicObject2D> list = new ArrayList<DynamicObject2D>();
		list.addAll(childrenMap.get(object));
		return list;
	}
	
	/**
	 * Recursively adds descendants. This function is intended to be used
	 * by getDescendants().
	 * @param object the object to add with its descendants
	 * @param list a list of descendants previously computed
	 * @return the updated list of descendants
	 */
	private Collection<DynamicObject2D> addDescendants(DynamicObject2D object,
			Collection<DynamicObject2D> list){
		for(DynamicObject2D child : childrenMap.get(object))
			if(!list.contains(child)){
				list.add(child);
				addDescendants(child, list);
			}
		return list;
	}
	
	/**
	 * Remove a dynamic object and all its descendants.
	 * @param object the object to remove
	 */
	public void removeDynamicObject(DynamicObject2D object){
		
		Collection<DynamicObject2D> descendants = getDescendants(object);
		
		// Remove dependencies between parents and children
		for(DynamicObject2D parent : object.getParents())
			childrenMap.get(parent).remove(object);		
		for(DynamicObject2D descendant : descendants)
			for(DynamicObject2D parent : descendant.getParents())
				childrenMap.get(parent).remove(descendant);
		
		// remove objects from children map and from dynamic objects
		descendants.add(object);
		for(DynamicObject2D dynamic : descendants){
			childrenMap.remove(dynamic);
			
			dynamicObjects.remove(dynamic);
			taggedObjects.remove(dynamic.getTag());
		}
				
		this.fireEvent(new DynamicHierarchyModifiedEvent(this));
	}
	
	public void updateDependencies(){
		for(DynamicObject2D object : dynamicObjects)
			object.update();
	}

	public Collection<DynamicObject2D> getDynamicObjects(){
		return Collections.unmodifiableList(dynamicObjects);
	}
	
	// ===================================================================
	// listeners management

	public void addDynamicHierarchyListener(DynamicHierarchyListener listener){
		listeners.add(listener);
	}

	public void removeDynamicHierarchyListener(DynamicHierarchyListener listener){
		listeners.remove(listener);
	}
	
	public void removeAllDynamicHierarchyListeners(){
		listeners.clear();
	}

	private void fireEvent(DynamicHierarchyEvent evt){
		for(DynamicHierarchyListener listener : listeners)
			listener.hierarchyModified(evt);
	}

	private class DynamicHierarchyModifiedEvent extends DynamicHierarchyEvent {
		DynamicObjectsManager manager;
		
		public DynamicHierarchyModifiedEvent(DynamicObjectsManager manager){
			this.manager = manager;
		}
		
		@Override
		public DynamicObjectsManager getManager(){
			return manager;
		}
	}

}
