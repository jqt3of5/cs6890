/**
 * 
 */
package gui.init;

import gui.EuclideGui;
import gui.EuclideTool;
import gui.tools.*;

import java.lang.reflect.Array;
import java.util.Hashtable;

import math.geom2d.AngleMeasure2D;
import math.geom2d.CountMeasure2D;
import math.geom2d.LengthMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircularShape2D;
import math.geom2d.conic.Conic2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.OrientedCurve2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.line.Ray2D;
import math.geom2d.point.PointSet2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.Transform2D;
import dynamic.DynamicPredicate2D;
import dynamic.labels.DynamicNameLabel2D;
import dynamic.labels.MeasureLabel2D;
import dynamic.labels.PredicateLabel2D;
import dynamic.labels.StringLabel2D;
import dynamic.measures.Angle2Lines2D;
import dynamic.measures.AreaPolygon2D;
import dynamic.measures.CurvePositionPoint2D;
import dynamic.measures.Distance2Points2D;
import dynamic.measures.DistancePointCurve2D;
import dynamic.measures.HorizontalAngleLine2D;
import dynamic.measures.LengthCurve2D;
import dynamic.measures.LengthLineSegment2D;
import dynamic.measures.PowerPointCircle2D;
import dynamic.measures.RadiusCircle2D;
import dynamic.measures.SignedAreaPolygon2D;
import dynamic.measures.SignedDistancePointCurve2D;
import dynamic.measures.SizePointSet2D;
import dynamic.measures.VertexNumberPolygon2D;
import dynamic.measures.WindingAnglePointCurve2D;
import dynamic.predicates.IsBounded2D;
import dynamic.predicates.IsColinearPoints2D;
import dynamic.predicates.IsEmpty2D;
import dynamic.predicates.IsPointInside2D;
import dynamic.shapes.*;
import dynamic.transforms.CircleInversionCircle2D;
import dynamic.transforms.LineSymmetryLine2D;
import dynamic.transforms.PointSymmetryPoint2D;
import dynamic.transforms.Rotation3Points2D;
import dynamic.transforms.RotationPointAngle2D;
import dynamic.transforms.ScalingCenter2Points2D;
import dynamic.transforms.ScalingCenterFactor2D;
import dynamic.transforms.Translation2Points2D;
import dynamic.vectors.TangentVectorCurvePosition2D;
import dynamic.vectors.Vector2Points2D;

/**
 * @author dlegland
 *
 */
public class ToolsLoader {
	
	EuclideGui gui;
	
	public ToolsLoader(EuclideGui gui){
		this.gui = gui;
	}
	
	public Hashtable<String, EuclideTool> loadTools(){
		
		Hashtable<String, EuclideTool> tools = 
			new Hashtable<String, EuclideTool>();
		
		// base shapes
		addBaseTools(tools);
		addPointTools(tools);
		addLineTools(tools);
		addCircleTools(tools);
		addConicTools(tools);
		addBezierTools(tools);
		
		// utility shapes
		addCurveTools(tools);
		addDomainTools(tools);
		addPolygonTools(tools);
		
		// other dynamic objects
		addLocusTools(tools);
		addTransformTools(tools);
		addMeasureTools(tools);
		addPredicateTools(tools);
		addVectorTools(tools);
		addLabelTools(tools);
		
		// other dynamic objects
		addMacroTools(tools);
		
		return tools;
	}

	public void addBaseTools(Hashtable<String, EuclideTool> tools){
		
		addTool(tools, new SelectShapeTool(gui, 
				"select"));

		addTool(tools, new PrintShapeInfoTool(gui, 
				"printShapeInfo"));

		addTool(tools, new MoveFreePointsTool(gui, 
				"moveFreePoints"));
	}
	
	public void addPointTools(Hashtable<String, EuclideTool> tools){
			
		addTool(tools, new AddFreePointTool(gui, 
				"addFreePoint"));

		addTool(tools, new AddConstructionTool(gui, 
				"addIntersection2Lines",
				Intersection2StraightObjects2D.class, 
				new Class[]{LinearShape2D.class, LinearShape2D.class}));

		addTool(tools, new AddIntersectionLineCircleTool(gui, 
				"addIntersectionLineCircle"));

		addTool(tools, new AddConstructionTool(gui, 
				"addIntersectionsLineCurve",
				IntersectionLineCurve2D.class, 
				new Class[]{LinearShape2D.class, Curve2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addIntersectionLineCurveIndex",
				IntersectionLineCurveIndex2D.class, 
				new Class[]{
			LinearShape2D.class, Curve2D.class,	CountMeasure2D.class}));

		addTool(tools, new AddIntersection2CirclesTool(gui, 
				"addIntersection2Circles"));

		addTool(tools, new AddConstructionTool(gui, 
				"addCentroidNPoints",
				CentroidNPoints2D.class, 
				new Class[]{createArrayClass(Point2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCircleCenter",
				CircleCenter2D.class, 
				new Class[]{Circle2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addMidPoint2Points",
				MidPoint2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddPointOnCurveTool(gui, 
				"addPointOnCurve"));

		addTool(tools, new AddConstructionTool(gui, 
				"addPointOnCurvePosition",
				PointOnCurvePosition2D.class, 
				new Class[]{Curve2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addGeodesicPointsOnCurve",
				GeodesicPointsOnCurve2D.class, 
				new Class[]{Curve2D.class, CountMeasure2D.class}));

//		addTool(tools, new AddDependentElement(gui, 
//				"addPolygonVertex",
//				PolygonVertex2D.class, 
//				new Class[]{Polygon2D.class, Point2D.class}));
		addTool(tools, new AddPolygonVertexTool(gui, "addPolygonVertex"));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addPolygonCentroid",
				PolygonCentroid2D.class, 
				new Class[]{Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPointPointSetIndex",
				PointPointSet2D.class, 
				new Class[]{PointSet2D.class, CountMeasure2D.class}));

		addTool(tools, new AddPointRelativeToPointTool(gui, 
				"addPointRelativePoint"));

		addTool(tools, new AddConstructionTool(gui, 
				"addPointRelative3Points",
				PointRelative3Points2D.class, 
				new Class[]{
			Point2D.class, Point2D.class, Point2D.class, 
			Measure2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPointSet",
				PointSetNPoints2D.class, 
				new Class[]{createArrayClass(Point2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPolygonVertices",
				PolygonVertices2D.class, 
				new Class[]{Polygon2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addPolylineVertices",
				PolylineVertices2D.class, 
				new Class[]{Polyline2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCurveSingularPoints",
				SingularPointsCurve2D.class, 
				new Class[]{Curve2D.class}));
		
		addTool(tools, new AddBiPointTool(gui, 
				"addBiPoint"));

	}
	
	// Tools for building new lines or linear objects		
	public void addLineTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addLineSegment",
				LineSegment2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddPolygonEdgeTool(gui, "addPolygonEdge"));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addStraightLine",
				StraightLine2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addLinePointAngle",
				StraightLinePointAngle2D.class, 
				new Class[]{Point2D.class, AngleMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addHorizontalLine",
				HorizontalLine2D.class, 
				new Class[]{Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addVerticalLine",
				VerticalLine2D.class, 
				new Class[]{Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRay2Points",
				Ray2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addInvertedRay2Points",
				InvertedRay2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addMedian2Points",
				Median2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addParallelLine",
				ParallelLine2D.class, 
				new Class[]{LinearShape2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addParallelLineDistance",
				ParallelLineDistance2D.class, 
				new Class[]{LinearShape2D.class, LengthMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPerpendicularLine",
				PerpendicularLine2D.class, 
				new Class[]{LinearShape2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSupportingLine",
				SupportingLine2D.class, 
				new Class[]{LinearShape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRadicalLine2Circles",
				RadicalLine2Circles2D.class, 
				new Class[]{Circle2D.class, Circle2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBisector2Lines",
				Bisector2Lines2D.class, 
				new Class[]{LinearShape2D.class, LinearShape2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addBisector3Points",
				Bisector3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addReflectedRay",
				ReflectedRay2D.class, 
				new Class[]{Ray2D.class, Curve2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addTangentCircleLine",
				TangentCircleLine2D.class, 
				new Class[]{Circle2D.class, LinearShape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addTangentLineCurvePoint",
				TangentLineCurvePoint2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addTangentRayCurvePoint",
				TangentRayCurvePoint2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addTangent2Circles",
				Tangent2Circles2D.class, 
				new Class[]{Circle2D.class, Circle2D.class, 
			DynamicPredicate2D.class, DynamicPredicate2D.class}));
	}
	
	// Tools for building new circles or conics		
	public void addCircleTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addCircle2Points",
				Circle2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCircle3Points",
				Circle3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCircleDiameter",
				CircleDiameter2D.class, 
				new Class[]{Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCircleCenterRadius",
				CirclePointRadius2D.class, 
				new Class[]{Point2D.class, LengthMeasure2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addOrthogonalCircle",
				OrthogonalCircle2D.class, 
				new Class[]{Circle2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addOsculatingCircle",
				OsculatingCircle2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCircleArc3Points",
				CircleArc3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCircleArc",
				CircleArcCenter2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class,
			DynamicPredicate2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addSupportingCircle",
				SupportingCircle2D.class, 
				new Class[]{CircularShape2D.class}));
	}
	
	// Tools for building new circles or conics		
	public void addConicTools(Hashtable<String, EuclideTool> tools){
		addTool(tools, new AddConstructionTool(gui, 
				"addEllipse3Points",
				Ellipse3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui,
				"addEllipse2FocusLength",
				Ellipse2Focus2D.class,
				new Class[]{Point2D.class, Point2D.class, 
			LengthMeasure2D.class}));		

		addTool(tools, new AddConstructionTool(gui,
				"addEllipse2FocusPoint",
				Ellipse2FocusPoint2D.class,
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addParabola2Points",
				Parabola2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addParabolaLinePoint",
				ParabolaLinePoint2D.class, 
				new Class[]{LinearShape2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addHyperbola3Points",
				Hyperbola3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addConic5Points",
				Conic5Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class,
			Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addConicFoci",
				ConicFocii2D.class, 
				new Class[]{Conic2D.class}));
	}
	
	public void addBezierTools(Hashtable<String, EuclideTool> tools){
		
		addTool(tools, new AddConstructionTool(gui, 
				"addBezierCurve3Points",
				BezierCurve3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBezierCurve4Points",
				BezierCurve4Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class,
			Point2D.class}));

	}

	public void addPolygonTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addPolyline",
				PolylineNPoints2D.class, 
				new Class[]{createArrayClass(Point2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addClosedPolyline",
				ClosedPolylineNPoints2D.class, 
				new Class[]{createArrayClass(Point2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"convertPointSetToPolyline",
				PolylinePointSet2D.class, 
				new Class[]{PointSet2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"convertPointSetToClosedPolyline",
				ClosedPolylinePointSet2D.class, 
				new Class[]{PointSet2D.class}));

        addTool(tools, new AddConstructionTool(gui, 
                "addPolygonNPoints",
                PolygonNPoints2D.class, 
                new Class[]{createArrayClass(Point2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"convertPointSetToPolygon",
				PolygonPointSet2D.class, 
				new Class[]{PointSet2D.class}));

         addTool(tools, new AddConstructionTool(gui, 
                "addConvexHullNPoints",
                ConvexHullNPoints2D.class, 
                new Class[]{createArrayClass(Point2D.class)}));

        addTool(tools, new AddConstructionTool(gui, 
                "addConvexHullPointSet",
                ConvexHullPointSet2D.class, 
                new Class[]{PointSet2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addIntersection2Polygons",
				Intersection2Polygons2D.class, 
				new Class[]{Polygon2D.class, Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addUnion2Polygons",
				Union2Polygons2D.class, 
				new Class[]{Polygon2D.class, Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addDifference2Polygons",
				Difference2Polygons2D.class, 
				new Class[]{Polygon2D.class, Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addExclusiveOr2Polygons",
				ExclusiveOr2Polygons2D.class, 
				new Class[]{Polygon2D.class, Polygon2D.class}));


        addTool(tools, new AddConstructionTool(gui, 
                "addBufferPolygon",
                BufferPolygon2D.class, 
                new Class[]{Polygon2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPolygonCenterVertexNumber",
				PolygonCenterVertexNumber2D.class, 
				new Class[]{Point2D.class, Point2D.class, 
			CountMeasure2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addPolygonCenterApothemNumber",
				PolygonCenterApothemNumber2D.class, 
				new Class[]{Point2D.class, Point2D.class, 
			CountMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRectangle2Points",
				Rectangle2Corners2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSquare2Points",
				Square2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addEquiTriangle2Points",
				EquiTriangle2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPolygon3Points",
				RegularPolygon3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPolygonCenter2Points",
				RegularPolygonCenter2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBox2Points",
				Box2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBoundingBox",
				BoundingBoxCurve2D.class, 
				new Class[]{Curve2D.class}));
	}

	public void addCurveTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addSubCurve2Points",
				SubCurve2D.class, 
				new Class[]{Curve2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSubCurve2Positions",
				SubCurvePositions2D.class, 
				new Class[]{Curve2D.class, Measure2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCurveToPolyline",
				CurveToPolyline2D.class, 
				new Class[]{ContinuousCurve2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addReverseCurve",
				ReversedCurve2D.class, 
				new Class[]{Curve2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCurveSetNCurves",
				CurveSetNCurves2D.class, 
				new Class[]{createArrayClass(Curve2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPolySmoothCurve",
				PolySmoothCurveNCurves2D.class, 
				new Class[]{createArrayClass(SmoothCurve2D.class)},
				new Object[]{new Boolean(false)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addClosedPolySmoothCurve",
				PolySmoothCurveNCurves2D.class, 
				new Class[]{createArrayClass(SmoothCurve2D.class)},
				new Object[]{new Boolean(true)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addContinuousBoundarySet",
				BoundarySetNBoundaries2D.class, 
				new Class[]{createArrayClass(Contour2D.class)}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCurveCurveSetIndex",
				CurveCurveSetIndex2D.class, 
				new Class[]{CurveSet2D.class, CountMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addParallelCurveDistance",
				ParallelCurveDistance2D.class, 
				new Class[]{Curve2D.class, LengthMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addParallelCurvePoint",
				ParallelCurvePoint2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addReuleaux2Points",
				ReuleauxTriangle2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));
	}
	
	public void addDomainTools(Hashtable<String, EuclideTool> tools) {
		addTool(tools, new AddConstructionTool(gui, 
				"addCurveToDomain",
				CurveToDomain2D.class, 
				new Class[]{Curve2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBoundaryDomain",
				BoundaryDomain2D.class, 
				new Class[]{Domain2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addComplementaryDomain",
				ComplementaryDomain2D.class, 
				new Class[]{Domain2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addDomainToPolygon",
				DomainToPolygon2D.class, 
				new Class[]{Domain2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBufferShapeDistance",
				BufferShape2D.class, 
				new Class[]{Shape2D.class, LengthMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addBufferShapePoint",
				BufferShapePoint2D.class, 
				new Class[]{Shape2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addBoundingBox",
				BoundingBoxCurve2D.class, 
				new Class[]{Shape2D.class}));
	}
	
	public void addLocusTools(Hashtable<String, EuclideTool> tools) {
		addTool(tools, new AddConstructionTool(gui, 
				"addPointLocus",
				PointLocus2D.class, 
				new Class[]{Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addCurveLocus",
				CurveLocus2D.class, 
				new Class[]{Curve2D.class}));
	}
	
	public void addTransformTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addTranslation2Points",
				Translation2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRotation3Points",
				Rotation3Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRotationPointAngle",
				RotationPointAngle2D.class, 
				new Class[]{Point2D.class, AngleMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addScalingCenter2Points",
				ScalingCenter2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addScalingCenterFactor",
				ScalingCenterFactor2D.class, 
				new Class[]{Point2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCircleInversion",
				CircleInversionCircle2D.class, 
				new Class[]{Circle2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addLineSymmetry",
				LineSymmetryLine2D.class, 
				new Class[]{LinearShape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPointSymmetry",
				PointSymmetryPoint2D.class, 
				new Class[]{Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addTransformedShape",
				TransformedShape2D.class, 
				new Class[]{Shape2D.class, Transform2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addMultiTransformedShape",
				MultiTransformedShape2D.class, 
				new Class[]{Shape2D.class, Transform2D.class, CountMeasure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRosace",
				MultiRotatedShape2D.class, 
				new Class[]{Shape2D.class, Point2D.class, CountMeasure2D.class}));
	}

	public void addMeasureTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addLengthLineSegment",
				LengthLineSegment2D.class, 
				new Class[]{LinearShape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"calcCurveLength",
				LengthCurve2D.class, 
				new Class[]{Curve2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addDistance2Points",
				Distance2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addDistancePointCurve",
				DistancePointCurve2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSignedDistancePointCurve",
				SignedDistancePointCurve2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addWindingAnglePointCurve",
				WindingAnglePointCurve2D.class, 
				new Class[]{Point2D.class, OrientedCurve2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addPowerPointCircle",
				PowerPointCircle2D.class, 
				new Class[]{Point2D.class, Circle2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addRadiusCircle",
				RadiusCircle2D.class, 
				new Class[]{Circle2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addAreaPolygon",
				AreaPolygon2D.class, 
				new Class[]{Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSignedAreaPolygon",
				SignedAreaPolygon2D.class, 
				new Class[]{Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addVertexNumberPolygon",
				VertexNumberPolygon2D.class, 
				new Class[]{Polygon2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addSizePointSet",
				SizePointSet2D.class, 
				new Class[]{PointSet2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addCurvePositionPoint",
				CurvePositionPoint2D.class, 
				new Class[]{Curve2D.class, Point2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addAngle2Lines",
				Angle2Lines2D.class, 
				new Class[]{LinearShape2D.class, LinearShape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addHorizontalAngle",
				HorizontalAngleLine2D.class, 
				new Class[]{LinearShape2D.class}));
	}
	
	// Tools for building new circles or conics		
	public void addPredicateTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addIsPointInside",
				IsPointInside2D.class, 
				new Class[]{Point2D.class, OrientedCurve2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addIsColinearPoints",
				IsColinearPoints2D.class, 
				new Class[]{Point2D.class, Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"isShapeEmpty",
				IsEmpty2D.class, 
				new Class[]{Shape2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"isShapeBounded",
				IsBounded2D.class, 
				new Class[]{Shape2D.class}));
	}

	// Tools for building new circles or conics		
	public void addVectorTools(Hashtable<String, EuclideTool> tools){

		addTool(tools, new AddConstructionTool(gui, 
				"addVector2Points",
				Vector2Points2D.class, 
				new Class[]{Point2D.class, Point2D.class}));
		
		addTool(tools, new AddConstructionTool(gui, 
				"addTangentVectorCurvePosition",
				TangentVectorCurvePosition2D.class, 
				new Class[]{Curve2D.class, Measure2D.class}));
		
	}

	// Tools for building new circles or conics		
	public void addLabelTools(Hashtable<String, EuclideTool> tools){

        addTool(tools, new AddConstructionTool(gui, 
                "addStringLabel",
                StringLabel2D.class, 
                new Class[]{Point2D.class, String.class}));

        addTool(tools, new AddConstructionTool(gui, 
                "addShapeNameLabel",
                DynamicNameLabel2D.class, 
                new Class[]{Shape2D.class, Point2D.class}));

        addTool(tools, new AddConstructionTool(gui, 
                "addMeasureLabel",
                MeasureLabel2D.class, 
                new Class[]{Point2D.class, Measure2D.class}));

		addTool(tools, new AddConstructionTool(gui, 
				"addPredicateLabel",
				PredicateLabel2D.class, 
				new Class[]{Point2D.class, DynamicPredicate2D.class}));
	}

	// Tools for building new circles or conics		
	public void addMacroTools(Hashtable<String, EuclideTool> tools){
		addTool(tools, new CreateMacroTool(gui, "createMacro"));
		addTool(tools, new RunMacroTool(gui, "runMacro"));
	}
	
	private final static Class<?> createArrayClass(Class<?> itemClas) {
		return Array.newInstance(itemClas, 0).getClass();
	}
	
	private final static Hashtable<String, EuclideTool> addTool(
			Hashtable<String, EuclideTool> tools, EuclideTool tool){
		tools.put(tool.getName(), tool);
		return tools;
	}
}
