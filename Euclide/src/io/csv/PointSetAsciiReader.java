/* file : PointSetAsciiReader.java
 * 
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 8 mai 2007
 *
 */
package io.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import math.geom2d.Point2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;

/**
 * Import points from a csv file. Each line contains a point, 
 * @author dlegland
 */
public class PointSetAsciiReader {

	public PointSetAsciiReader() {
		super();
	}

	public PointSet2D readFile(File file) throws IOException{
		BufferedReader reader;
		String line;
		double x, y;
		
		PointSet2D pointSet = new PointArray2D();

		// open a buffered text reader on the file
		reader = new BufferedReader(new FileReader(file));

		// read each line
		while((line=reader.readLine())!=null){
			if(line.isEmpty())
				continue;
			
			StringTokenizer st = new StringTokenizer(line, ", ");
			x = Double.valueOf(st.nextToken());
			y = Double.valueOf(st.nextToken());
			pointSet.addPoint(new Point2D(x, y));
		}
		return pointSet;
	}

}
