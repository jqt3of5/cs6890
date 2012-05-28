/**
 * 
 */
package gui.init;

import gui.*;
import gui.actions.*;

import java.util.Hashtable;

import dynamic.measures.*;
import dynamic.shapes.Point2Values2D;
import dynamic.transforms.*;
import dynamic.vectors.*;

import math.geom2d.LengthMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Vector2D;
import math.geom2d.AffineTransform2D;


/**
 * @author dlegland
 *
 */
public class ActionsLoader {

	EuclideGui gui;
	
	public ActionsLoader(EuclideGui gui){
		this.gui = gui;
	}
	
	public Hashtable<String, EuclideAction> loadActions(){
		Hashtable<String, EuclideAction> actions = 
			new Hashtable<String, EuclideAction>();
		
		addFileActions(actions);		
		addEditActions(actions);
		addViewActions(actions);
		
		addShapeActions(actions);		
		addTransformActions(actions);		
		addVectorActions(actions);
		addMeasureActions(actions);
		
		addAction(actions, new ClearLociAction(gui, "clearLoci"));
		
		// Help
		addAction(actions, new OpenDialogAction(gui, "about", "about"));

		// return the created actions
		return actions;
	}

	private void addFileActions(Hashtable<String, EuclideAction> actions) {
		// File actions
		addAction(actions, new NewDocAction(gui, "newDoc"));
		addAction(actions, new OpenFileAction(gui, "openDoc"));
		addAction(actions, new SaveAction(gui, "saveDoc"));
		addAction(actions, new SaveFileAction(gui, "saveAs"));
		addAction(actions, new ExportPointsAction(gui, "exportPoints"));		
		addAction(actions, new ExportSheetAsSvgAction(gui, "exportSheetAsSvg"));
		addAction(actions, new CloseCurrentDocumentAction(gui, "closeDoc"));
		addAction(actions, new ExitAction(gui, "exit"));
		
	}

	private void addEditActions(Hashtable<String, EuclideAction> actions) {
		// Edit actions
		addAction(actions, new ImportPointsAction(gui, "importPoints"));		
		addAction(actions, new AddSheetAction(gui, "addSheet"));
		
		addAction(actions, new OpenDialogAction(gui, "editSheet", "editSheet"));
		addAction(actions, new RemoveSheet(gui, "removeSheet"));
		addAction(actions, new AddLayerAction(gui, "addLayer"));
		addAction(actions, new OpenDialogAction(gui, "editDocStyles", "editDocStyles"));
		addAction(actions, new ToggleLayersPanelAction(gui, "toggleLayersDialog"));
		addAction(actions, new EditGridAction(gui, "editGrid"));
		
		// Style edition actions
		addAction(actions, new ChangeMarkerStyleAction(gui, "editMarkerStyle"));
		addAction(actions, new ChangeLineStyleAction(gui, "editLineStyle"));
		addAction(actions, new ChangeFillStyleAction(gui, "editFillStyle"));

		addAction(actions, new SelectionUpAction(gui, "selectionUp"));
		addAction(actions, new SelectionDownAction(gui, "selectionDown"));
		addAction(actions, new SelectionTopAction(gui, "selectionTop"));
		addAction(actions, new SelectionBottomAction(gui, "selectionBottom"));
		addAction(actions, new SelectionChangeLayerAction(gui, "selectionChangeLayer"));
		addAction(actions, new SelectionDeleteAction(gui, "selectionDelete"));

		// Document
		addAction(actions, new EditConstantsAction(gui, "editConstants"));
		//addAction(actions, new DisplayDocInfoAction(gui, "displayDocInfo"));
		addAction(actions, new OpenDialogAction(gui, "displayDocInfo", "displayDocInfo"));
		addAction(actions, new OpenDialogAction(gui, "displayDocTree", "displayDocTree"));
		addAction(actions, new EditGridAction(gui, "editGrid"));
	}

	private void addViewActions(Hashtable<String, EuclideAction> actions) {		
		// View
		addAction(actions, new ZoomInAction(gui, "zoomin"));
		addAction(actions, new ZoomOutAction(gui, "zoomout"));
		addAction(actions, new ZoomOneAction(gui, "zoomOne"));
		addAction(actions, new ZoomFitWidthAction(gui, "zoomFitWidth"));
		addAction(actions, new ZoomFitHeightAction(gui, "zoomFitHeight"));
		addAction(actions, new ZoomFitBestAction(gui, "zoomBestFit"));
	}
	
	private void addShapeActions(Hashtable<String, EuclideAction> actions) {
		// Constructions
		addAction(actions, new AddConstructionAction(gui, 
				"addPoint2Values",
				Point2Values2D.class, 
				new Class[]{LengthMeasure2D.class, Measure2D.class}));
	}
	
	private void addTransformActions(Hashtable<String, EuclideAction> actions) {
		addAction(actions, new AddConstructionAction(gui,
				"addTranslationVector",
				TranslationVector2D.class, 
				new Class[]{Vector2D.class}));

		addAction(actions, new AddConstructionAction(gui,
				"addComposedTransform",
				ComposedTransform2D.class, 
				new Class[]{AffineTransform2D.class, AffineTransform2D.class}));
	}
	
	private void addVectorActions(Hashtable<String, EuclideAction> actions) {		
		addAction(actions, new AddConstructionAction(gui,
				"addVectorAdd2Vectors",
				VectorAdd2Vectors2D.class, 
				new Class[]{Vector2D.class, Vector2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addVectorMultiplyVector",
				VectorMultiplyVector2D.class, 
				new Class[]{Vector2D.class, Measure2D.class}));
	}
	
	private void addMeasureActions(Hashtable<String, EuclideAction> actions) {
		addAction(actions, new AddConstructionAction(gui,
				"addDotProduct2Vectors",
				DotProduct2Vectors2D.class, 
				new Class[]{Vector2D.class, Vector2D.class}));

		addAction(actions, new AddConstructionAction(gui,
				"addCrossProduct2Vectors",
				CrossProduct2Vectors2D.class, 
				new Class[]{Vector2D.class, Vector2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addSqrtVariable",
				VariableSqrt2D.class, 
				new Class[]{Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addSquareVariable",
				VariableSquared2D.class, 
				new Class[]{Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addInvertVariable",
				VariableInvert2D.class, 
				new Class[]{Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addVariablesAdd",
				VariablesAdd2D.class, 
				new Class[]{Measure2D.class, Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addVariablesSubtract",
				VariablesSubtract2D.class, 
				new Class[]{Measure2D.class, Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addVariablesMultiply",
				VariablesMultiply2D.class, 
				new Class[]{Measure2D.class, Measure2D.class}));
		
		addAction(actions, new AddConstructionAction(gui,
				"addVariablesDivide",
				VariablesDivide2D.class, 
				new Class[]{Measure2D.class, Measure2D.class}));
	}
	
	private final static Hashtable<String, EuclideAction> addAction(
			Hashtable<String, EuclideAction> actions, EuclideAction action){
		actions.put(action.getName(), action);
		return actions;
	}
}
