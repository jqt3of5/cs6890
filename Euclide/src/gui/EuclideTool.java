/* file : EuclideTool.java
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

import java.awt.event.*;

/**
 * An adapter which implements mouse listener and mouse motion listener, and
 * which provides basic management of tool step and instruction strings.
 * @author dlegland
 */
public class EuclideTool implements MouseListener, MouseMotionListener {

	/** 
	 * the parent application.
	 */
	protected EuclideGui gui;
	
	/**
	 * the name of the tool. This is the label used to find it in a hashtable
	 * in the main application.
	 */
	protected String name;
	
	/**
	 * Each tool consists in a series of actions. This variable keep the
	 * current state of evolution of the tool. Numbered from 1 to N.
	 */
	protected int step = 1;
	
	/**
	 * The Tool instruction corresponding to each step. Each step should
	 * correspond to one instruction line.
	 */
	private String[] instructions = new String[]{"no instruction"};
	
	
	// ===================================================================
	// constructors

	
	public EuclideTool(EuclideGui gui, String name){
		this.gui = gui;
		this.name = name;
	}
	
	
	public String getName(){
		return name;
	}
	
	/**
	 * Give instruction corresponding to current step of the tool.
	 * @return an instruction string for this tool
	 */
	public String getInstruction(){
		if(step>instructions.length)
			return "no instruction";
		else
			return instructions[step-1];
	}
	
	/**
	 * set up the instruction strings for the tool. The number of strings
	 * should be equal to the number of possible step for the tool.
	 * @param strings
	 */
	public void setInstructions(String[] strings){
		instructions = strings;
	}
	
	/**
	 * This function is called when the tool is selected. Overload this method
	 * to add initialization, but do not forget to call the parent method in
	 * the beginning of the new method by 
	 * <code>super.select();</code>.
	 *
	 */
	public void select(){
		step = 1;
	}
	
	/**
	 * This function is called when the tool is deselected. Overload this method to add 
	 * post processing to the tool, but do not forget to call the parent method in
	 * the beginning of the new method by 
	 * <code>super.deselect();</code>.
	 *
	 */
	public void deselect(){
		EuclideSheetView view = null;
		if(gui.getCurrentView()!=null)
			if(gui.getCurrentView().getCurrentSheetView()!=null)
				view = gui.getCurrentView().getCurrentSheetView();
		if(view!=null)
			view.clearPreview();
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseDragged(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {}
}
