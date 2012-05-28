/**
 * 
 */
package dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * An array of DynamicShape2D. This is mainly a Wrapper class to allow
 * creation of classes from a collection of classes.
 * @author dlegland
 *
 */
public class DynamicArray2D extends DynamicObject2D {

	ArrayList<DynamicObject2D> objects;

	
	public DynamicArray2D(DynamicObject2D [] objects){
		this.objects = new ArrayList<DynamicObject2D>(objects.length);
		for(DynamicObject2D object : objects)
			this.objects.add(object);
		this.parents.addAll(this.objects);
	}
	
	public DynamicArray2D(Collection<? extends DynamicObject2D> objects){
		this.objects = new ArrayList<DynamicObject2D>(objects.size());
		this.objects.addAll(objects);
		this.parents.addAll(objects);
	}
	
	/**
	 * Returns the number of elements stored by the array
	 */
	public int getSize() {
		return objects.size();
	}
	
	public Collection<DynamicObject2D> getObjects() {
		return Collections.unmodifiableList(objects);
	}
	
	@Override
	public void update() {
		// nothing to update
	}
}
