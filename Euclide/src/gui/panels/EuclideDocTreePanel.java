/**
 * File: 	EuclideDocTreePanel.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 févr. 10
 */
package gui.panels;

import gui.EuclideGui;
import gui.EuclidePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.EuclideDoc;
import model.EuclideFigure;
import dynamic.*;


/**
 * @author dlegland
 *
 */
public class EuclideDocTreePanel extends EuclidePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EuclideDoc doc;
	
	private DefaultTreeCellRenderer basicRenderer = 
		new DefaultTreeCellRenderer();
	
	public EuclideDocTreePanel(EuclideGui gui, EuclideDoc doc) {
		super(gui);
		this.doc = doc;
		
		basicRenderer.setLeafIcon(null);
		basicRenderer.setOpenIcon(null);
		basicRenderer.setClosedIcon(null);
		
		this.setLayout(new BorderLayout());
		DefaultMutableTreeNode top = createTree(doc);

		JTree tree = new JTree(top);
		tree.setCellRenderer(basicRenderer);
		JScrollPane treeView = new JScrollPane(tree);		
		
		this.add(treeView, BorderLayout.CENTER);		
		this.setSize(new Dimension(200, 300));
		this.setPreferredSize(new Dimension(200, 300));
	}
	
	private DefaultMutableTreeNode createTree(EuclideDoc doc) {
		DefaultMutableTreeNode top =
	        new DefaultMutableTreeNode("Euclide Doc");

		DefaultMutableTreeNode node;
		
		String name = doc.getName();
		node = new DefaultMutableTreeNode ("Name = " + name);
		top.add(node);
		
		top.add(createConstructionsTree(doc));
		top.add(createFiguresTree(doc));
	
		return top;
	}
	
	private DefaultMutableTreeNode createConstructionsTree(EuclideDoc doc) {
		int n = doc.getDynamicObjects().size();
		DefaultMutableTreeNode top = createNode("Constructions (" + n + ")");
		
		top.add(createShapesTree(doc));
		top.add(createMeasuresTree(doc));
		top.add(createTransformsTree(doc));
		top.add(createVectorsTree(doc));
		top.add(createPredicatesTree(doc));
		
		return top;
	}
	
	private DefaultMutableTreeNode createShapesTree(EuclideDoc doc) {
		int n = doc.getDynamicShapes().size();
		DefaultMutableTreeNode top = createNode("Shapes (" + n + ")");
		for(DynamicShape2D dyn : doc.getDynamicShapes())
			top.add(createConstructionTree(dyn));
		return top;
	}
	
	private DefaultMutableTreeNode createMeasuresTree(EuclideDoc doc) {
		int n = doc.getMeasures().size();
		DefaultMutableTreeNode top = createNode("Measures (" + n + ")");
		for(DynamicMeasure2D dyn : doc.getMeasures())
			top.add(createConstructionTree(dyn));
		return top;
	}
	
	private DefaultMutableTreeNode createTransformsTree(EuclideDoc doc) {
		int n = doc.getTransforms().size();
		DefaultMutableTreeNode top = createNode("Transforms (" + n + ")");
		for(DynamicTransform2D dyn : doc.getTransforms())
			top.add(createConstructionTree(dyn));
		return top;
	}

	private DefaultMutableTreeNode createVectorsTree(EuclideDoc doc) {
		int n = doc.getVectors().size();
		DefaultMutableTreeNode top = createNode("Vectors (" + n + ")");
		for(DynamicVector2D dyn : doc.getVectors())
			top.add(createConstructionTree(dyn));
		return top;
	}

	private DefaultMutableTreeNode createPredicatesTree(EuclideDoc doc) {
		int n = doc.getPredicates().size();
		DefaultMutableTreeNode top = createNode("Predicates (" + n + ")");
		for(DynamicPredicate2D dyn : doc.getPredicates())
			top.add(createConstructionTree(dyn));
		return top;
	}

	private DefaultMutableTreeNode createConstructionTree(DynamicObject2D object) {
		String name = object.getName();
		String className = object.getClass().getSimpleName();
		DefaultMutableTreeNode top = createNode(name + " (" + className + ")");
		
		top.add(new DefaultMutableTreeNode("Name = " + object.getName()));
		top.add(new DefaultMutableTreeNode("Tag = " + object.getTag()));
		top.add(new DefaultMutableTreeNode("Class = " + className));
		
		DefaultMutableTreeNode parentNode =
	        new DefaultMutableTreeNode("Parents (" + object.getParents().size() + ")");
		for(DynamicObject2D parent : object.getParents())
			parentNode.add(createConstructionFinalNode(parent));
		top.add(parentNode);
	
		DefaultMutableTreeNode paramsNode =
	        new DefaultMutableTreeNode("Parameters (" + object.getParameters().size() + ")");
		for(Object param : object.getParameters())
			paramsNode.add(new DefaultMutableTreeNode(param));
		top.add(paramsNode);

		return top;
	}
	
	private DefaultMutableTreeNode createConstructionFinalNode(DynamicObject2D object) {
		String name = object.getName();
		String className = object.getClass().getSimpleName();
		DefaultMutableTreeNode top = createNode(name + " (" + className + ")");	
		return top;
	}

	private DefaultMutableTreeNode createFiguresTree(EuclideDoc doc) {
		int n = doc.getFigures().size();
		DefaultMutableTreeNode top = createNode("Figures (" + n + ")");
		for(EuclideFigure item : doc.getFigures())
			top.add(createFigureTree(item));
		return top;
	}
	
	private DefaultMutableTreeNode createFigureTree(EuclideFigure item) {
		String name = item.getName();
		DynamicShape2D geom = item.getGeometry();
		String geomClassName = geom.getClass().getSimpleName();
		DefaultMutableTreeNode top = createNode(name + " (" + geomClassName + ")");
		
		top.add(new DefaultMutableTreeNode("Name = " + item.getName()));
		top.add(new DefaultMutableTreeNode("Tag = " + item.getTag()));
		String geomTag = geom.getTag();
		top.add(new DefaultMutableTreeNode("Geometry = " + geomTag + 
				" (" + geomClassName + ")"));
		
		return top;
	}
	
	private DefaultMutableTreeNode createNode(String name) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(name);
		return top;		
	}
	
	public void updateTree() {
		DefaultMutableTreeNode top = createTree(doc);
		
		JTree tree = new JTree(top);
		tree.setCellRenderer(basicRenderer);
		JScrollPane treeView = new JScrollPane(tree);
		this.removeAll();
		this.add(treeView, BorderLayout.CENTER);		
	}
}
