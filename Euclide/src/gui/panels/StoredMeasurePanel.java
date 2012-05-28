/**
 * File: 	StoredMeasurePanel.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 25 janv. 09
 */
package gui.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import dynamic.DynamicMeasure2D;

import math.geom2d.AngleMeasure2D;
import math.geom2d.CountMeasure2D;
import math.geom2d.LengthMeasure2D;
import math.geom2d.Measure2D;
import model.EuclideDoc;
import gui.EuclideGui;
import gui.EuclidePanel;


/**
 * @author dlegland
 *
 */
public class StoredMeasurePanel extends EuclidePanel
implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    
    EuclideDoc doc;
    
    DynamicMeasure2D.Type type  = DynamicMeasure2D.Type.NONE;
    
    DynamicMeasure2D measure    = null;
    HashMap<String, DynamicMeasure2D> measures;

    JComboBox typeCombo         = new JComboBox();
    JList measureList           = new JList();
    DefaultListModel listModel  = new DefaultListModel();

    
    public StoredMeasurePanel(EuclideGui gui){
        super(gui);
        this.doc = gui.getCurrentView().getDoc();
        
        // init combo for measure types
        for(DynamicMeasure2D.Type type : DynamicMeasure2D.Type.values())
            typeCombo.addItem(type);
        typeCombo.addActionListener(this);
        
        // init measure list
        measureList.setLayoutOrientation(JList.VERTICAL);
        measureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        measureList.setVisibleRowCount(5);
        updateMeasureList(); 

        this.createLayout();
    }

    private void createLayout(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JLabel typeLabel = new JLabel("Measure type:");
        typeLabel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(typeLabel);
        
        this.add(typeCombo);
        typeCombo.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel selectMeasureLabel = new JLabel("Select Measure:");
        selectMeasureLabel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(selectMeasureLabel);
        
        JScrollPane  scroll = new JScrollPane(measureList);
        scroll.setMinimumSize(new Dimension(120, 80));
        scroll.setPreferredSize(new Dimension(120, 80));
        scroll.setAlignmentX(LEFT_ALIGNMENT);
        this.add(scroll);
    }
    
    /**
     * Set up the model for the measure list, and apply to list.
     */
    private void updateMeasureList() {
        // Create the class for required measure
        Class<? extends Measure2D> measureClass = Measure2D.class;
        switch(type){
        case LENGTH:
            measureClass = LengthMeasure2D.class;
            break;
        case ANGLE:
            measureClass = AngleMeasure2D.class;
            break;
        case COUNTING:
            measureClass = CountMeasure2D.class;
            break;
        }
        
        // create a new hashtable for the new measure selection
        measures = new HashMap<String, DynamicMeasure2D>();
        listModel = new DefaultListModel();
        
        // need to update the doc
        doc = gui.getCurrentDoc();
        
         // fill the list and the hashtable
        for(DynamicMeasure2D dynamic : doc.getMeasures()){
            // ensure measure has the correct type
            if(!measureClass.isInstance(dynamic.getMeasure()))
                continue;
            
            // add to the list and to the hashtable
            String name = dynamic.getName();
            measures.put(name, dynamic);
            listModel.addElement(name);
        }
        measureList.setModel(listModel);
    }
    
    /**
     * Returns the dynamic measure according to chosen type and to
     * given value.
     * @return the chosen measure
     */
    public DynamicMeasure2D getMeasure(){
        if(measureList.isSelectionEmpty())
            return null;
        return measures.get(measureList.getSelectedValue());
    }

    public void setType(DynamicMeasure2D.Type type){
        this.type = type;
        typeCombo.setSelectedItem(type);
        updateMeasureList();
    }

    @Override
    public void updateWidgets() {
    	this.updateMeasureList();
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource()==typeCombo){
            type = (DynamicMeasure2D.Type)typeCombo.getSelectedItem();
            updateMeasureList();
        }
    }   
}
