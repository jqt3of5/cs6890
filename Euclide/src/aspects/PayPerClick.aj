package aspects;

import gui.actions.*;
import java.util.ArrayList;

public aspect PayPerClick {

	pointcut toolSelect(SelectToolAction tool) : execution(* SelectToolAction.actionPerformed(..)) && this(tool);
	
	ArrayList<String> payTools;
	
	PayPerClick()
	{
		payTools = new ArrayList<String>();
		payTools.add("addCircle3Points");
	}
	
	void around(SelectToolAction tool) : toolSelect(tool)
	{
		System.out.println("name: " + tool.getName());
		
		if (payTools.contains(tool.getName()))
		{
			System.out.println("You haven't payed for that tool yet");
			return;
		}
		proceed(tool);
	}
	
	
}
