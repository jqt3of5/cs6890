package aspects;

import java.util.ArrayList;

import dynamic.DynamicObject2D;
import dynamic.DynamicLabel2D;
import dynamic.shapes.ShapeWrapper2D;
import dynamic.shapes.DynamicMoveablePoint2D;

public aspect TrimmerAspect {

	pointcut dynamic2DDerivedConstructor(DynamicObject2D shape) : 
		initialization(DynamicObject2D+.new(..))//update is called always in constructors.
		&& !within(dynamic.*) // these classes are base classes.. should not update
		&& !within(ShapeWrapper2D)  // this seems to be a special class that does not update in cons
		&& !within(DynamicMoveablePoint2D) // another special class
		&& target(shape);
	
	
	after (DynamicObject2D shape) returning: dynamic2DDerivedConstructor(shape)
	{
		ArrayList<? extends DynamicObject2D> parent = (ArrayList<? extends DynamicObject2D>) shape.getParents();
		ArrayList<? extends Object> params = (ArrayList<? extends Object>) shape.getParameters();
		
		params.trimToSize();
		parent.trimToSize();
	}

}