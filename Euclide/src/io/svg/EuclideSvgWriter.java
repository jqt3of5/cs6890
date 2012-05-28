
/**
 * 
 */

package io.svg;

import gui.util.HtmlColors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.point.PointSet2D;
import model.EuclideLayer;
import model.EuclideFigure;
import model.EuclideSheet;
import model.style.DrawStyle;
import dynamic.DynamicShape2D;

/**
 * Write an EuclideDoc in a text file in SVG format.
 * @author dlegland
 *
 */
public class EuclideSvgWriter {
	/** used for debugging */
	private static boolean verbose = true;

	public EuclideSvgWriter() {
		super();
	}

	public void writeFile(EuclideSheet sheet, File file) throws IOException{
		if(verbose) System.out.println("Save in file " + file.getName());
		
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsolutePath())));
		Locale loc = Locale.US;
		
		// -----------------------------------
		// Write SVG Header
		
		// encoding and so on...
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.print("<svg ");
		writer.println("    xmlns=\"http://www.w3.org/2000/svg\"");
		writer.println("    xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
		
		// size of the sheet
		writer.format(loc, "    width=\"%fmm\" height=\"%fmm\"", 
				sheet.getWidth(), sheet.getHeight());
		
		// bounding box
		Box2D box = sheet.getViewBox();
		writer.format(loc, "    viewBox=\"%f %f %f %f\"\n", 
				box.getMinX(), box.getMinY(), 
				box.getWidth(), box.getHeight());
		
		// End of header
		writer.println(">");
		
		
		
		// -----------------------------------
		// Write SVG Contents
		
		
		for(EuclideLayer layer : sheet.getLayers()){
			if(!layer.isVisible()) continue;
			for(EuclideFigure shape : layer.getShapes()){
				writeShape(writer, shape, sheet);
			}			
		}
		
		
		// Write the end of the file
		writer.println("</svg>");
		writer.println("");
		
		// Close the file
		writer.close();		
	}

//	public void writePoint(PrintWriter writer, Shape2D shape, DrawStyle style){
//		// Open the tag, and write geometry
//		// ---------------------------------
//
//		// open the <g> tag
//		writer.print("<g");
//		
//		// Write the ID (the tag)
//		String tag = shape.getTag();
//		if(tag!=null)
//			writer.print(" id=\"" + tag + "\"");
//		
//		// Write the style
//		
//		// Close the tag
//		writer.println(">");
//		
//		
//		// Write the geometry
//		// ---------------------------------
//		
//		
//		
//	
//		// closes the <g> element
//		// ---------------------------------
//		writer.println("</g>");		
//	}
	
	public void writeShape(PrintWriter writer, EuclideFigure shape, EuclideSheet sheet){
		
		double defaultWidth = .02;
		
		DynamicShape2D dynamic = shape.getGeometry();
		if(!dynamic.isDefined()) return;
		
		Shape2D geometry = dynamic.getShape();
				
		Locale loc = Locale.US;
		
		// Open the tag, and write geometry
		// ---------------------------------

		// open the <g> tag
		writer.print("  <g");
		
		// Write the ID (the tag)
		String tag = shape.getTag();
		if(tag!=null)
			writer.print(" id=\"" + tag + "\"");
		
		// Write the style
		DrawStyle style = shape.getDrawStyle();			
		if(geometry instanceof Point2D || geometry instanceof PointSet2D){
			writer.format(loc, 
					" fill=\"%s\" stroke=\"%s\" stroke-width=\"%s\"", 
					HtmlColors.toString(style.getMarkerFillColor()),
					HtmlColors.toString(style.getMarkerColor()),
					defaultWidth );
			
		}else if (geometry instanceof Curve2D){
			writer.format(loc, 
					" fill=\"none\" stroke=\"%s\" stroke-width=\"%s\"", 
					HtmlColors.toString(style.getLineColor()), defaultWidth);
			
		}else if (geometry instanceof Domain2D){
			writer.format(loc, 
					" fill=\"%s\" stroke=\"%s\" stroke-width=\"%s\"",
					HtmlColors.toString(style.getFillColor()),
					HtmlColors.toString(style.getLineColor()), defaultWidth);			
			
		}else{
			System.err.println("unknown class type " + geometry.getClass());
		}
		
		// Close the tag
		writer.println(">");
		
		
		// Write the geometry
		// ---------------------------------
		
		if(geometry instanceof Point2D){
			Point2D point = (Point2D) geometry;
			writer.format(loc, 
					"    <circle cx=\"%f\" cy=\"%f\" r=\"%f\"/>\n", 
					point.getX(), point.getY(), .05);
		}else if (geometry instanceof PointSet2D){
			
		}else if (geometry instanceof LineSegment2D){
			LineSegment2D line = (LineSegment2D) geometry;
			Point2D p1 = line.getFirstPoint();
			Point2D p2 = line.getLastPoint();
			writer.format(loc, 
					"    <line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\"/>\n", 
					p1.getX(), p1.getY(), p2.getX(), p2.getY());

		}else if (geometry instanceof Circle2D){
			Circle2D circle = (Circle2D) geometry;
			Point2D center = circle.getCenter();
			writer.format(loc, 
					"    <circle cx=\"%f\" cy=\"%f\" r=\"%f\"/>\n", 
					center.getX(), center.getY(), circle.getRadius());

		}else if (geometry instanceof Curve2D){
			
		}else if (geometry instanceof Domain2D){
			
		}else{
		}		
		
	
		// closes the <g> element
		// ---------------------------------
		writer.println("  </g>");
	}
}
