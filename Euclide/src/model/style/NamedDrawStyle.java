/**
 * File: 	NamedDrawStyle.java
 * Project: Euclide-Style
 * 
 * Distributed under the LGPL License.
 *
 * Created: 3 févr. 10
 */
package model.style;


/**
 * @author dlegland
 *
 */
public class NamedDrawStyle extends DrawStyleDecorator {

	String name;
	
	/**
	 * @param baseStyle
	 */
	public NamedDrawStyle(DrawStyle baseStyle, String name) {
		super(baseStyle);
		this.name = name;
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
