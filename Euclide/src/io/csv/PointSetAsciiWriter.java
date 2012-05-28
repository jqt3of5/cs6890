
/**
 * 
 */

package io.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import math.geom2d.Point2D;
import math.geom2d.point.PointSet2D;

/**
 * Save a point set in a comma-separated value format
 * @author dlegland
 *
 */
public class PointSetAsciiWriter {
	/** used for debugging */
	private static boolean verbose = true;

	public PointSetAsciiWriter() {
		super();
	}

	public void writeFile(PointSet2D points, File file) throws IOException{
		if(verbose) System.out.println("Export point set in csv file: " + 
				file.getName());
		
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new FileWriter(file.getAbsolutePath())));
		Locale loc = Locale.US;
		
		for(Point2D point : points){
			writer.format(loc, "%s,%s\n", 
					Double.toString(point.getX()), 
					Double.toString(point.getY()));
		}
		
		// Close the file
		writer.close();		
	}
}
