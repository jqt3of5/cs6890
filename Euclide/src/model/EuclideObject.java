/**
 * 
 */
package model;

/**
 * Base class for all objects manipulated by Euclide program.
 * @author dlegland
 */
public class EuclideObject {

	private String tag = null;
	
	private String name = "";
	
	public EuclideObject(){
	}
	
	public EuclideObject(String name){
		this.name = name;
	}

	public EuclideObject(String name, String tag){
		this.name = name;
		this.tag = tag;
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
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
