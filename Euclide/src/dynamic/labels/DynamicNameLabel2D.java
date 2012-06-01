/**
 * File: 	StringLabel.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 28 f�vr. 09
 */
package dynamic.labels;

import math.geom2d.Label2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import dynamic.DynamicLabel2D;
import dynamic.DynamicObject2D;
import dynamic.DynamicShape2D;


/**
 * @author dlegland
 *
 */
public class DynamicNameLabel2D extends DynamicLabel2D {

	DynamicObject2D parent1;
    DynamicShape2D  parent2;
	
    Label2D label = new Label2D(new Point2D(), "");
    
    /**
     * 
     */
    public DynamicNameLabel2D(DynamicShape2D dynamic, DynamicShape2D position) {
        super();
        this.parent1 = dynamic;
        this.parent2 = position;
        parents.add(dynamic);
        parents.add(position);
        parents.trimToSize();
        
    //    update();
    }

    /* (non-Javadoc)
     * @see dynamic.DynamicLabel2D#getLabel()
     */
    @Override
    public Label2D getLabel() {
        return label;
    }

    /* (non-Javadoc)
     * @see dynamic.DynamicObject2D#update()
     */
    @Override
    public void update() {
        this.defined = false;
        if(!parent2.isDefined()) return;
        
        // extract label position
        Shape2D shape = parent2.getShape();
        if(!(shape instanceof Point2D)) return;
        this.label.setPosition((Point2D) shape);

        // update label status
        this.label.setText(parent1.getName());
        this.defined = true;
    }

}
