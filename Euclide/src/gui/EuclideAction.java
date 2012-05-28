/* file : EuclideAction.java
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author dlegland
 */
public class EuclideAction extends AbstractAction {
	// ===================================================================
	// class variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected EuclideGui gui;
	
	/**
	 * The Tool instruction corresponding to each step. Each step should
	 * correspond to one instruction line.
	 */
	private String[] instructions = new String[]{"no instruction"};

	
	// ===================================================================
	// constructors

	public EuclideAction(EuclideGui gui, String name){
		super(name);
		this.gui = gui;
	}

	public EuclideAction(EuclideGui gui, String name, Icon icon){
		super(name, icon);
		this.gui = gui;
	}
	
	// ===================================================================
	// accessors

	/**
	 * Give instruction corresponding to current step of the action.
	 * @return an instruction string for this tool
	 */
	public String getInstruction(int step){
		if(step >= instructions.length)
			return "";
		else
			return instructions[step];
	}
	
	/**
	 * set up the instruction strings for the tool. The number of strings
	 * should be equal to the number of possible step for the tool.
	 * @param strings
	 */
	public void setInstructions(String[] strings){
		instructions = strings;
	}
	
	public String getName(){
		return (String)super.getValue(Action.NAME);
	}
	
	public EuclideGui getGui(){
		return gui;
	}

	/**
	 * This method will be overriden by classes extending EuclideAction.
	 */
	public void actionPerformed(ActionEvent arg0) {
	}
}
