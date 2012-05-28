/**
 * File: 	StringLabel.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 28 févr. 09
 */
package dynamic.labels;

import math.geom2d.Label2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import dynamic.DynamicLabel2D;
import dynamic.DynamicShape2D;


/**
 * @author dlegland
 *
 */
public class StringLabel2D extends DynamicLabel2D {

    DynamicShape2D  parent1;
    
    Label2D label = new Label2D(new Point2D(), "");
    
    /**
     * 
     */
    public StringLabel2D(DynamicShape2D position, String text) {
        super();
        this.parent1 = position;
        label.setText(text);
        
        parents.add(position);
        parents.trimToSize();
        
        parameters.add(text);
        parameters.trimToSize();
        
        update();
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
        if(!parent1.isDefined()) return;
        
        // extract first point
        Shape2D shape = parent1.getShape();
        if(!(shape instanceof Point2D)) return;
        this.label.setPosition((Point2D) shape);

        this.defined = true;
    }

}
