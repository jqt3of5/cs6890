package aspects;

import java.awt.event.MouseEvent;
import gui.actions.*;
import gui.tools.*;
import gui.EuclideAction;
import gui.EuclideSheetView;
import gui.EuclideTool;

public aspect RepaintAspect {

	pointcut zoomRepaint(EuclideAction ths) : execution(void Zoom*Action.actionPerformed(..)) && this(ths);
	pointcut selectionRepaint(EuclideAction ths) : execution(void Selection*Action.actionPerformed(..)) && this(ths);
	pointcut changeRepaint(EuclideAction ths)	: execution(void Change*Action.actionPerformed(..)) && this(ths);
	pointcut toolRepaint(MouseEvent evt, EuclideTool ths) : execution(void Add*Tool.mousePressed(..)) && this(ths) && args(evt);
	pointcut movepointRepaint(MouseEvent evt, EuclideTool ths) : execution(void Move*Tool.mouse*(..)) && this(ths) && args(evt);
	//==================================================================================
	
	void around(MouseEvent evt, EuclideTool ths) : movepointRepaint(evt, ths)
	{
		proceed(evt, ths);
		((EuclideSheetView) evt.getSource()).repaint();

	}
	
	void around(MouseEvent evt, EuclideTool ths) : toolRepaint(evt, ths)
	{	
		proceed(evt, ths);
		((EuclideSheetView) evt.getSource()).repaint();
		((EuclideSheetView) evt.getSource()).setInstruction(ths.getInstruction());
		System.out.println("We repainted after a mouse click " + ths.getInstruction());
	}
		
	
	//==================================================================================
	
	after(EuclideAction ths) returning : changeRepaint(ths)
	{
		ths.getGui().getCurrentView().getCurrentSheetView().repaint();
		System.out.println("We repainted a change");
	}
	
	
	after(EuclideAction ths) returning : selectionRepaint(ths)
	{
		ths.getGui().getCurrentSheetView().repaint();
		System.out.println("We repainted a selection");
	}
	
	after(EuclideAction ths) returning : zoomRepaint(ths)
	{
		
		ths.getGui().getCurrentView().getCurrentSheetView().invalidate();
		ths.getGui().getCurrentFrame().validate();
		ths.getGui().getCurrentFrame().repaint();
		System.out.println("We repainted something after a zoom");
	}
	
}
