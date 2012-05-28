/**
 * File: 	DynamicHierarchyEvent.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 24 févr. 10
 */
package model.event;

import model.DynamicObjectsManager;


/**
 * @author dlegland
 *
 */
public abstract class DynamicHierarchyEvent {
	public abstract DynamicObjectsManager getManager();
}
