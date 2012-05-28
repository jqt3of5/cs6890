/* file : EuclideDGFReader.java
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

package io.dgf;

import gui.util.HtmlColors;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.*;

import math.geom2d.AngleMeasure2D;
import math.geom2d.AngleUnit;
import math.geom2d.Box2D;
import math.geom2d.Shape2D;
import math.geom2d.grid.Grid2D;
import math.geom2d.grid.SquareGrid2D;
import math.geom2d.grid.TriangleGrid2D;
import model.EuclideDoc;
import model.EuclideLayer;
import model.EuclideFigure;
import model.EuclideSheet;
import model.style.DefaultDrawStyle;
import model.style.DrawStyle;
import model.style.Marker;
import model.style.DrawStyle.*;

import org.apache.log4j.Logger;

import dynamic.*;
import dynamic.measures.AngleWrapper2D;
import dynamic.shapes.ShapeWrapper2D;

/**
 * Read a File in format DGF: Dynamic Geometry File.
 * @author dlegland
 */
public class EuclideDGFReader {

	/** Apache log4j Logger */
	private static Logger logger = Logger.getLogger("Euclide");

	EuclideDoc doc;

	Hashtable<String, String> hash;

	Hashtable<String, DynamicObject2D> objects;

	public EuclideDGFReader() {
		super();
	}

	public EuclideDoc readFile(File file) throws IOException {

		// uncomment this to show trace of file reading
		//logger.setLevel(org.apache.log4j.Level.TRACE);

		// Create empty document class
		this.doc = new EuclideDoc();

		// convert tags in the file into set of Strings
		this.hash = parseFile(file);

		// creates a HashTable containing created objects referenced by their tag
		this.objects = new Hashtable<String, DynamicObject2D>();

		String line;

		// ----------
		// Extract meta data

		parseDocumentMetaData();

		// parse doc main style
		DrawStyle docStyle = parseStyle("doc", new DefaultDrawStyle());
		doc.setDrawStyle(docStyle);

		// Extract geometric objects that are not shapes
		parseMeasures();
		parseVectors();
		parseTransforms();
		parsePredicates();

		// Extract each sheet name
		line = hash.get("docSheets");
		if (line==null) {
			logger.error("Unable to find the tag [docSheets]");
			return doc;
		}

		// parses each sheet
		for (String token : getTokens(line)) {
			EuclideSheet sheet = parseSheet(token);
			if (sheet!=null) {
				doc.addSheet(sheet);
			} else
				logger.error("Unable to parse Sheet with tag ["+token+"]");
		}

		doc.setModified(false);
		return doc;
	}

	private void parseDocumentMetaData() {
		// document name
		String docName = hash.get("docName");
		if (docName!=null)
			doc.setName(docName);

		// author name
		String authorName = hash.get("docAuthor");
		if (authorName!=null)
			doc.setAuthorName(authorName);

		// date of last modification
		String dateString = hash.get("docCreation");
		Date creationDate = null;
		if (dateString!=null) {
			try {
				DateFormat format = DateFormat.getDateInstance(
						DateFormat.SHORT, Locale.US);
				creationDate = format.parse(dateString);
			} catch (java.text.ParseException ex) {
				logger.error("Unable to parse docCreation");
			}
		}
		if (creationDate!=null)
			doc.setCreationDate(creationDate);
	}

	private void parseTransforms() {
		logger.trace("Parses transforms");
		String line = hash.get("docTransforms");
		if (line!=null) {
			parseAllTokens(getTokens(line));
		}
	}

	private void parseMeasures() {
		logger.trace("Parses measures");
		String line = hash.get("docMeasures");
		if (line!=null) {
			parseAllTokens(getTokens(line));
		}
	}

	private void parseVectors() {
		logger.trace("Parses vectors");
		String line = hash.get("docVectors");
		if (line!=null) {
			parseAllTokens(getTokens(line));
		}
	}

	private void parsePredicates() {
		logger.trace("Parses predicates");
		String line = hash.get("docPredicates");
		if (line!=null) {
			parseAllTokens(getTokens(line));
		}
	}

	private void parseAllTokens(Collection<String> tokens) {
		for (String token : tokens) {
			// checks if element has been previously parsed
			DynamicObject2D object = objects.get(token);
			if (object!=null)
				continue;

			// Parses the measure (the vector is added to the doc at the end
			// of the parseDynamic function)
			parseDynamic(token);
		}
	}

	/**
	 * Parses an EuclideSheet given by its token.
	 * @return the sheet containing all layers
	 */
	private EuclideSheet parseSheet(String sheetToken) {

		logger.trace("Parses sheet: ["+sheetToken+"]");

		String line;

		// collection of layers
		ArrayList<EuclideLayer> layers = new ArrayList<EuclideLayer>();

		// extract name of sheet
		String sheetName = hash.get(sheetToken.concat("Name"));
		if (sheetName==null)
			sheetName = "";

		// creates the sheet
		EuclideSheet sheet = new EuclideSheet(doc, sheetName);
		sheet.setTag(sheetToken);

		// extract size of sheet page
		line = hash.get(sheetToken.concat("Size"));
		double sheetWidth = 200;
		double sheetHeight = 200;
		if (line!=null) {
			StringTokenizer st = new StringTokenizer(line);
			if (st.hasMoreTokens())
				sheetWidth = Double.parseDouble(st.nextToken());
			if (st.hasMoreTokens())
				sheetHeight = Double.parseDouble(st.nextToken());
		}
		sheet.setDimension(sheetWidth, sheetHeight);

		// extract view box of sheet
		line = hash.get(sheetToken.concat("Viewbox"));
		double xmin = 0;
		double ymin = 0;
		double xmax = 1;
		double ymax = 1;
		if (line!=null) {
			StringTokenizer st = new StringTokenizer(line);
			if (st.hasMoreTokens())
				xmin = Double.parseDouble(st.nextToken());
			if (st.hasMoreTokens())
				xmax = Double.parseDouble(st.nextToken());
			if (st.hasMoreTokens())
				ymin = Double.parseDouble(st.nextToken());
			if (st.hasMoreTokens())
				ymax = Double.parseDouble(st.nextToken());
		}
		sheet.setViewBox(new Box2D(xmin, xmax, ymin, ymax));

		// Extract grid info
		buildSheetGrid(sheet, sheetToken, hash);

		// extract tokens of layers in sheet 
		line = hash.get(sheetToken.concat("Layers"));
		if (line==null)
			return null;

		// parses each sheet
		for (String token : getTokens(line)) {
			EuclideLayer layer = parseLayer(token);
			if (layer!=null)
				sheet.addLayer(layer);
			else
				logger.error("Unable to parse Layer with tag ["+token+"]");
		}

		// select the current layer
		line = hash.get(sheetToken.concat("CurrentLayer"));
		if (line!=null) {
			for (EuclideLayer layer : layers) {
				if (layer.getTag().equals(line)) {
					sheet.setCurrentLayer(layer);
					break;
				}
			}
		}

		// return created sheet
		return sheet;
	}

	private void buildSheetGrid(EuclideSheet sheet, String sheetToken,
			Hashtable<String, String> tokens) {

		String gridType = tokens.get(sheetToken.concat("GridType"));

		if (gridType!=null) {
			gridType = gridType.trim().toLowerCase();

			Grid2D grid = null;
			if (gridType.equals("square")) {
				grid = new SquareGridBuilder().buildGrid(sheetToken, tokens);
			} else if (gridType.equals("triangle")) {
				grid = new TriangleGridBuilder(0).buildGrid(sheetToken, tokens);
			} else if (gridType.equals("triangleupdown")) {
				grid = new TriangleGridBuilder(0).buildGrid(sheetToken, tokens);
			} else if (gridType.equals("triangleleftright")) {
				grid = new TriangleGridBuilder(1).buildGrid(sheetToken, tokens);
			} else {
				throw (new RuntimeException(String.format(
						"Unable to parse grid type: %s", gridType)));
			}
			if (grid!=null)
				sheet.setGrid(grid);

			// is grid visible
			String line = tokens.get(sheetToken.concat("GridVisible"));
			if (line!=null) {
				line = line.trim().toLowerCase();
				sheet.setGridVisible(Boolean.parseBoolean(line));
			}
		}
	}

	/**
	 * Parse a EuclideLayer given by its token.
	 * @return the layer with all the created shapes and dynamic objects
	 */
	private EuclideLayer parseLayer(String layerToken) {
		logger.trace("Parses layer: ["+layerToken+"]");

		// Creates empty layer
		EuclideLayer layer = new EuclideLayer();
		layer.setTag(layerToken);

		// extract name of layer
		String layerName = hash.get(layerToken.concat("Name"));
		if (layerName!=null)
			layer.setName(layerName);

		// extract visibility of layer
		String layerVisible = hash.get(layerToken.concat("Visible"));
		if (layerVisible!=null)
			layer.setVisible(Boolean.parseBoolean(layerVisible));

		// extract edit flag of layer
		String layerEdit = hash.get(layerToken.concat("Editable"));
		if (layerEdit!=null)
			layer.setEditable(Boolean.parseBoolean(layerEdit));

		// extract tokens of shapes in layer 
		String line = hash.get(layerToken.concat("Shapes"));
		if (line==null)
			return layer;

		// parses each layer
		for (String token : getTokens(line)) {
			EuclideFigure item = parseFigure(token);
			if (item!=null) {
				doc.addFigure(item, layer);
			} else {
				logger.error("Unable to parse figure with tag ["+token+"]");
			}
		}

		// return the layer with all loaded elements
		return layer;
	}

	/**
	 * Parse a EuclideLayer given by its token.
	 * @return
	 */
	private EuclideFigure parseFigure(String figureTag) {

		logger.trace("Parses shape: ["+figureTag+"]");

		Object object;

		// check if element has been previously parsed
		object = objects.get(figureTag);
		if (object!=null)
			return (EuclideFigure) object;

		String string;

		// --------------------------------
		// Extract geometry of the element

		string = hash.get(figureTag.concat("Geometry"));
		DynamicShape2D geometry = (DynamicShape2D) parseDynamic(string);

		EuclideFigure item = new EuclideFigure(geometry);

		// --------------------------------
		// extract element draw style

		DrawStyle baseStyle = doc.getDrawStyle();
		DrawStyle style = parseStyle(figureTag, baseStyle);
		item.setDrawStyle(style);

		// setup tag and add to document
		item.setTag(figureTag);

		return item;
	}

	private DrawStyle parseStyle(String tag, DrawStyle baseStyle) {

		String string;

		DefaultDrawStyle style = new DefaultDrawStyle(baseStyle);

		// --------------------------------
		// Extract drawing style of marker

		// marker type
		parseMarkerType(tag, style);

		// marker size
		string = hash.get(tag.concat("MarkerSize"));
		if (string!=null) {
			double size = Double.parseDouble(string);
			style.setMarkerSize(size);
		}

		// marker size unit
		parseMarkerSizeUnit(tag, style);

		// marker color
		string = hash.get(tag.concat("MarkerColor"));
		if (string!=null) {
			Color color = parseColor(string);
			style.setMarkerColor(color);
		}

		// marker fill color
		string = hash.get(tag.concat("MarkerFill"));
		if (string!=null) {
			Color color = parseColor(string);
			style.setMarkerFillColor(color);
		}

		// marker line width
		string = hash.get(tag.concat("MarkerWidth"));
		if (string!=null) {
			double width = Double.parseDouble(string);
			style.setMarkerLineWidth(width);
		}

		// --------------------------------
		// Extract drawing style of stroke

		// line visibility
		string = hash.get(tag.concat("LineVisible"));
		if (string!=null) {
			boolean visible = Boolean.parseBoolean(string);
			style.setLineVisible(visible);
		}

		// line width
		string = hash.get(tag.concat("LineWidth"));
		if (string!=null) {
			double width = Double.parseDouble(string);
			style.setLineWidth(width);
		}

		// line width unit
		parseLineWidthUnit(tag, style);

		// line color
		string = hash.get(tag.concat("LineColor"));
		if (string!=null) {
			Color color = parseColor(string);
			style.setLineColor(color);
		}

		// line cap style
		parseLineEndCap(tag, style);

		// line join style
		parseLineJoin(tag, style);

		// line dash
		string = hash.get(tag.concat("LineDash"));
		if (string!=null) {
			// extract the tokens containing dash values
			String[] tokens = string.split(",");

			// convert to float
			float[] dash = new float[tokens.length];
			for (int i = 0; i<tokens.length; i++)
				dash[i] = Float.parseFloat(tokens[i]);

			// set up line style
			style.setLineDash(dash);
		}

		// line dash phase
		string = hash.get(tag.concat("LinePhase"));
		if (string!=null) {
			float phase = Float.parseFloat(string);
			style.setLineDashPhase(phase);
		}

		// --------------------------------
		// Extract fill style of element

		// marker type
		string = hash.get(tag.concat("FillType"));
		if (string!=null) {
			if (string.toLowerCase().equals("color"))
				style.setFillType(DrawStyle.FillType.COLOR);
		}

		// fill color
		string = hash.get(tag.concat("FillColor"));
		if (string!=null) {
			Color color = parseColor(string);
			style.setFillColor(color);
		}

		// fill transparency
		string = hash.get(tag.concat("FillAlpha"));
		if (string!=null) {
			// alpha is supposed to be stored as integer between 0 and 100.
			// However, for old version of Euclide it was set between 0 and 255.
			// The strategy is to convert values greater that 100. 
			int alpha = Integer.parseInt(string);
			if (alpha>100)
				style.setFillTransparency(alpha/255.0);
			else
				style.setFillTransparency(alpha/100.0);
		}

		return style;
	}

	private void parseMarkerType(String tag, DefaultDrawStyle style) {
		// marker type
		String string = hash.get(tag.concat("MarkerType"));
		if (string==null)
			return;

		if (string.toLowerCase().equals("round"))
			style.setMarker(Marker.CIRCLE);
		if (string.toLowerCase().equals("square"))
			style.setMarker(Marker.SQUARE);
		if (string.toLowerCase().equals("diamond"))
			style.setMarker(Marker.DIAMOND);
		if (string.toLowerCase().equals("plus"))
			style.setMarker(Marker.PLUS);
		if (string.toLowerCase().equals("cross"))
			style.setMarker(Marker.CROSS);
	}

	private void parseMarkerSizeUnit(String tag, DefaultDrawStyle style) {
		// line cap style
		String string = hash.get(tag.concat("MarkerSizeUnit"));
		if (string==null) {
			return;
		}
		
		string = string.toLowerCase();
		for (MarkerSizeUnit unit : MarkerSizeUnit.values()) {
			if (unit.toString().toLowerCase().equals(string)) {
				style.setMarkerSizeUnit(unit);
				return;
			}
		}
	}

	private void parseLineWidthUnit(String tag, DefaultDrawStyle style) {
		// line cap style
		String string = hash.get(tag.concat("LineWidthUnit"));
		if (string==null) {
			return;
		}
		
		string = string.toLowerCase();
		for (LineWidthUnit unit : LineWidthUnit.values()) {
			if (unit.toString().toLowerCase().equals(string)) {
				style.setLineWidthUnit(unit);
				return;
			}
		}
	}

	private void parseLineEndCap(String tag, DefaultDrawStyle style) {
		// line cap style
		// Officiel is 'LineEndCap', but 'LineCap' was used before.
		String string = hash.get(tag.concat("LineEndCap"));
		if (string==null) {
			string = hash.get(tag.concat("LineCap"));
			if (string==null)
				return;
		}
		
		string = string.toLowerCase();
		for (EndCap endCap : EndCap.values()) {
			if (endCap.toString().toLowerCase().equals(string)) {
				style.setLineEndCap(endCap);
				return;
			}
		}
	}

	private void parseLineJoin(String tag, DefaultDrawStyle style) {
		// line join style
		String string = hash.get(tag.concat("LineJoin"));
		if (string==null)
			return;

		string = string.toLowerCase();
		for (LineJoin join : LineJoin.values()) {
			if (join.toString().toLowerCase().equals(string)) {
				style.setLineJoin(join);
				return;
			}
		}
	}

	/**
	 * Parse a Dynamic object given by a token. 
	 * The hashtable given as parameter contains a line corresponding to the
	 * token 'token'. The first token of this line is a class name. 
	 * Other tokens are arguments tags. The method extract class name, tries 
	 * to build arguments (by calling recursively this method with different
	 * tags), then build a new object corresponding to class and arguments.
	 * @return the new created DynamicObject2D corresponding to the token
	 */
	private DynamicObject2D parseDynamic(String tag) {
		logger.trace("Parses construction: ["+tag+"]");

		// check that object has not been previously created
		DynamicObject2D object = objects.get(tag);
		if (object!=null) {
			logger.trace("  Object ["+tag+"] already parsed\n");
			return object;
		}

		// find line containing dynamic object definition
		String line = hash.get(tag);
		if (line==null) {
			logger
					.error("Could not find Construction definition for tag: "
							+tag);
			return null;
		}

		// Extract the different parts of the object definition
		Iterator<?> iterator = parseStringTree(line).iterator();

		// Extract class of object
		String className = (String) iterator.next();
		logger.trace("  Class: "+className);
		Class<?> objectClass = parseClass(className);

		// prepare array of arguments and of argument class
		ArrayList<Object> args = new ArrayList<Object>();
		ArrayList<Class<?>> argClasses = new ArrayList<Class<?>>();

		ConstructionArgument argument;

		// Parse each argument
		while (iterator.hasNext()) {

			Object node = iterator.next();

			if (node instanceof String) {
				argument = parseArgument((String) node);
				args.add(argument.value);
				argClasses.add(argument.argClass);
			} else {
				argument = parseArgumentArray((Collection<?>) node);
				args.add(argument.value);
				argClasses.add(argument.argClass);
			}
		}

		try {
			object = buildConstruction(objectClass, argClasses, args);
		} catch (Exception ex) {
			logger.error("Could not create object ["+tag+"].");
			return null;
		}

		// setup tag of created object
		object.setTag(tag);

		// Try to find the name of the object
		String name = hash.get(tag+"Name");
		if (name!=null)
			if (name.length()>0)
				object.setName(name);

		// add to the set of build objects
		logger.trace("Object ["+tag+"] has been created.\n");
		if (object instanceof math.geom2d.Shape2D) {
			logger.trace("Convert to dynamic shape");
			object = new dynamic.shapes.ShapeWrapper2D(
					(math.geom2d.Shape2D) object);
		}
		objects.put(tag, object);

		// For some classes, it is needed to add the object to the doc
		if (object instanceof DynamicShape2D)
			doc.addDynamicShape((DynamicShape2D) object);
		else if (object instanceof DynamicVector2D)
			doc.addVector((DynamicVector2D) object);
		else if (object instanceof DynamicMeasure2D)
			doc.addMeasure((DynamicMeasure2D) object);
		else if (object instanceof DynamicTransform2D)
			doc.addTransform((DynamicTransform2D) object);
		else if (object instanceof DynamicMeasure2D)
			doc.addPredicate((DynamicPredicate2D) object);

		// return result
		return object;
	}

	private ConstructionArgument parseArgument(String token) {
		logger.trace("  Parses argument: "+token);

		// try to read as boolean 'true'
		if (token.compareToIgnoreCase("true")==0) {
			return new ConstructionArgument(boolean.class, true);
		}

		// try to read as boolean 'false'
		if (token.compareToIgnoreCase("false")==0) {
			return new ConstructionArgument(boolean.class, false);
		}

		// try to read as integer
		try {
			int x = Integer.parseInt(token);
			return new ConstructionArgument(int.class, x);
		} catch (NumberFormatException ex) {
			// do nothing and try to read in another format
		}

		// try to read as double
		try {
			double x = Double.parseDouble(token);
			return new ConstructionArgument(double.class, x);
		} catch (NumberFormatException ex) {
			// do nothing and try to read in another format
		}

		// try to read as string (if contains ")
		if (token.contains("\"")) {
			int pos1 = token.indexOf("\"");
			int pos2 = token.indexOf("\"", pos1+1);
			token = token.substring(pos1+1, pos2);

			return new ConstructionArgument(String.class, token);
		}

		// try to read as dynamic shape2D
		if (hash.containsKey(token)) {
			DynamicObject2D dyn = parseDynamic(token);
			return new ConstructionArgument(getDynamicBaseClass(dyn), dyn);
		}

		logger.error("Unable to parse argument: "+token);
		return null;
	}

	@SuppressWarnings("unchecked")
	private ConstructionArgument parseArgumentArray(Collection<?> nodeArray) {
		logger.trace("Parses array");

		int n = nodeArray.size();
		
		// create the array of DynamicObject2D
		int j=0;
		DynamicObject2D dyn;
		DynamicObject2D[] array = new DynamicObject2D[n];
		for(Object node : nodeArray) {
			dyn = parseDynamic((String) node);
			array[j++] = dyn;
		}
		
		Class<?> arrayClass = getArrayClass(dynamic.DynamicShape2D.class);
	
		// Unchecked class cast
		Class<? extends DynamicObject2D>[] classes = 
			new Class[]{
			DynamicShape2D.class, 
			DynamicMeasure2D.class, 
			DynamicTransform2D.class, 
			DynamicVector2D.class, 
			DynamicPredicate2D.class, 
			};
		
		// try to specialize the class of the array
		if (array.length>0) {
			dyn = array[0];
			for (int i=0; i<classes.length; i++){
				Class<? extends DynamicObject2D> theClass = classes[i];
				if(!theClass.isInstance(dyn))
					continue;
				array = convertDynamicArray(array, theClass);
				arrayClass = getArrayClass(theClass);
				break;
			}
		}

		// set up current argument and current argument class.
		return new ConstructionArgument(arrayClass, array);
	}

	private Class<? extends DynamicObject2D> getDynamicBaseClass(
			DynamicObject2D dyn) {
		if (dyn instanceof dynamic.DynamicShape2D)
			return dynamic.DynamicShape2D.class;
		if (dyn instanceof dynamic.DynamicMeasure2D)
			return dynamic.DynamicMeasure2D.class;
		if (dyn instanceof dynamic.DynamicTransform2D)
			return dynamic.DynamicTransform2D.class;
		if (dyn instanceof dynamic.DynamicVector2D)
			return dynamic.DynamicVector2D.class;
		if (dyn instanceof dynamic.DynamicPredicate2D)
			return dynamic.DynamicPredicate2D.class;
		if (dyn instanceof dynamic.DynamicLabel2D)
			return dynamic.DynamicLabel2D.class;

		logger.error("Unable to determine the parent class for: "
				+dyn.getClass().getSimpleName());
		return null;
	}

	private Class<?> getArrayClass(Class<?> baseClass) {
		return Array.newInstance(baseClass, 0).getClass();
	}
	
	/**
	 * Converts an aray of dynamic objects to an array of the class given
	 * as argument. All objects in the array must be of class T.
	 */
	@SuppressWarnings("unchecked")
	private <T extends DynamicObject2D> T[] convertDynamicArray(
			DynamicObject2D[] array, Class<T> newClass) {
		int n = array.length;
		// Unchecked exception
		T[] newArray = (T[]) Array.newInstance(newClass, n);
		for (int i = 0; i<n; i++) {
			// Unchecked exception
			newArray[i] = (T) array[i];
		}
		return newArray;
	}
	
	private DynamicObject2D buildConstruction(Class<?> objectClass,
			ArrayList<Class<?>> argClasses, ArrayList<Object> args)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		// convert vector to arrays
		Object[] tabArgs = new Object[args.size()];
		Class<?>[] tabClasses = new Class<?>[args.size()];
		for (int i = 0; i<args.size(); i++) {
			tabArgs[i] = args.get(i);
			tabClasses[i] = argClasses.get(i);
		}

		// special cases
		if (AngleWrapper2D.class.isAssignableFrom(objectClass)) {
			logger.trace("Angle wrapper found");
			double angle = (Double) (tabArgs[0]);
			tabArgs[0] = new AngleMeasure2D(angle, AngleUnit.DEGREE);
			tabClasses[0] = AngleMeasure2D.class;
		}

		Constructor<?>[] constructors = objectClass.getConstructors();
		Constructor<?> constructor = null;

		// compare each constructor of the class with the given args
		// to find the right one.
		for (int i = 0; i<constructors.length; i++) {
			Class<?>[] consClasses = constructors[i].getParameterTypes();

			// check if same number of arguments
			if (consClasses.length!=tabClasses.length)
				continue;

			// compare each classes individually, and try to find a parent
			// class which corresponds
			int j;
			for (j = 0; j<consClasses.length; j++)
				if (!consClasses[j].isAssignableFrom(tabClasses[j]))
					break;

			// if each parameter is ok, the constructor is ok.
			if (j==consClasses.length) {
				constructor = constructors[i];
				break;
			}
		}

		if (constructor==null) {
			logger.error("Could not find constructor for object of class ["
							+objectClass.getSimpleName()+"].");
			return null;
		}

		// create geometry with found constructor
		Object obj = constructor.newInstance(tabArgs);

		// Special processing for Shape2D directly written out
		DynamicObject2D object;
		if (obj instanceof Shape2D)
			object = new ShapeWrapper2D((Shape2D) obj);
		else
			object = (DynamicObject2D) obj;

		return object;
	}

	/**
	 * Converts each line containing a "=" into an hashtable entry, with first
	 * part (before the '=') as key, and second part (after '=') as value.
	 */
	public final static Hashtable<String, String> parseFile(File file)
			throws IOException {
		//TODO: maybe there is a Java class to do the processing ?
		Hashtable<String, String> hash = new Hashtable<String, String>();

		BufferedReader reader;
		String line, name, cmd;
		int pos;

		// open a buffered text reader on the file
		reader = new BufferedReader(new FileReader(file));

		// read each line
		while ((line = reader.readLine())!=null) {
			// extract position of '=' character
			pos = line.indexOf('=');
			if (pos<1)
				continue;

			// extract substring corresponding to the key, and removes spaces
			name = line.substring(0, pos).replaceAll(" ", "");

			// extract substring corresponding to the line,
			// and remove spaces before and after
			cmd = line.substring(pos+1).trim();

			// add new entry into the table
			hash.put(name, cmd);
		}

		// close the file
		reader.close();

		return hash;
	}

	/**
	 * Given a class name, try to create a Class, by looking in a set of
	 * standard directories. If no class was found, return null.
	 */
	private static Class<?> parseClass(String className) {
		String[] packages = { "java.lang.", "math.geom2d.",
				"math.geom2d.curve.", "math.geom2d.conic.",
				"math.geom2d.line.", "math.geom2d.point.",
				"math.geom2d.polygon.", "math.geom2d.spline.",
				"math.geom2d.transform.", "dynamic.", "dynamic.labels.",
				"dynamic.measures.", "dynamic.predicates.", "dynamic.shapes.",
				"dynamic.transforms.", "dynamic.vectors.", "." };

		// try to load the given class, by adding each known package
		for (int i = 0; i<packages.length; i++) {
			try {
				return Class.forName(packages[i]+className);
			} catch (Exception ex) {
			}
		}

		// if no class was found, return null
		return null;
	}

	/**
	 * Parse a Color object from a string. Color can be represented as a RGB
	 * triplet, either between 0 and 1 or between 0 and 255, or as a string
	 * containing color key, such as "red", "green"...
	 * @param s the string to parse
	 * @return a java.awt.Color object
	 */
	public static Color parseColor(String s) {
		return HtmlColors.parseColor(s);
	}

	/**
	 * Take the given string and chop it up into a series
	 * of strings on whitespace boundaries.  This is useful
	 * for trying to get an array of strings out of the
	 * resource file.
	 */
	protected final static Collection<String> getTokens(String input) {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer t = new StringTokenizer(input);

		while (t.hasMoreTokens())
			list.add(t.nextToken());

		return list;
	}

	/**
	 * Returns a single string without trailing spaces and to lower case.
	 */
	protected final static String parseString(String token,
			Hashtable<String, String> hash) {
		String res = hash.get(token);
		if (res!=null)
			return res.trim().toLowerCase();
		return null;
	}

	/**
	 * Transform a string into a tree of string. Each node of the tree is
	 * either a String, or another node. Each leaf of the tree is a String.<p>
	 * Example:<p>
	 * <code> string1 {string2 string3 {string4 string5}} {string6 string7} </code>
	 * gives:
	 * - string1
	 * - array
	 *     - string2
	 *     - string3
	 *     - array
	 *         - string4
	 *         - string5
	 * - array
	 *     - string6
	 *     - string7
	 * 
	 * @param string
	 * @return
	 */
	private static Collection<?> parseStringTree(String string) {
		String token;
		ArrayList<Object> list = new ArrayList<Object>();

		StringTokenizer st = new StringTokenizer(string);
		while (st.hasMoreTokens()) {
			token = st.nextToken();

			if (token.contains("{")) {
				// isolate from after "{" until the end of the string
				String rest = token+st.nextToken("");
				rest = rest.substring(rest.indexOf("{")+1);

				// find the "}" corresponding to the opening one
				int count = 1;
				for (int i = 0; i<rest.length(); i++) {
					if (rest.charAt(i)=='{')
						count++;
					if (rest.charAt(i)=='}')
						count--;

					if (count==0) {
						// we have found corresponding closing bracket
						// parse what we have found inside
						list.add(parseStringTree(rest.substring(0, i)));

						// update String tokenizer to parse the rest of the string
						st = new StringTokenizer(rest.substring(i+1));
						break;
					}
				}

			} else if (token.contains("\"")) {
				// Consider the whole remaining line
				String rest = token;
				if (st.hasMoreTokens())
					rest = rest+st.nextToken("");

				// isolate from after "\"" until the end of the string
				rest = rest.substring(rest.indexOf("\""));

				// find index of closing "
				int ind = rest.substring(1).indexOf("\"");

				// extract string content
				if (ind>=0) {
					token = rest.substring(0, ind+2);
					rest = rest.substring(ind+2);
				} else {
					token = rest = "\"";
					rest = "";
				}

				// add current token
				list.add(token);

				// initialize new string tokenizer
				st = new StringTokenizer(rest);
			} else {
				// The token is a simple string
				list.add(token);
			}
		}

		return list;
	}

}

class ConstructionArgument {

	Class<?> argClass;
	Object value;

	public ConstructionArgument(Class<?> argClass, Object value) {
		this.argClass = argClass;
		this.value = value;
	}
}

interface GridBuilder {

	public Grid2D buildGrid(String sheetToken, Hashtable<String, String> tokens);
}

class SquareGridBuilder implements GridBuilder {

	public SquareGridBuilder() {
	}

	public Grid2D buildGrid(String sheetToken, Hashtable<String, String> tokens) {
		// default grid size
		double sx = 1;
		double sy = 1;

		// square grid: need 2 sizes
		String line = tokens.get(sheetToken.concat("GridSize"));
		if (line!=null) {
			StringTokenizer st = new StringTokenizer(line);
			if (st.hasMoreTokens())
				sx = Double.parseDouble(st.nextToken());
			if (st.hasMoreTokens())
				sy = Double.parseDouble(st.nextToken());
		}
		return new SquareGrid2D(sx, sy);
	}
}

class TriangleGridBuilder implements GridBuilder {

	// grid orientation, in radians
	double theta = 0;
		
	public TriangleGridBuilder() {
	}

	/**
	 * If type is 0, then theta is 0, if type is 1, then theta is PI/2.
	 * @param type
	 */
	public TriangleGridBuilder(int type) {
		if (type<0 || type>1) 
			throw new IllegalArgumentException(String.format(
					"Type should be 0 or 1, it is %d", type));
			
		this.theta = Math.PI*.5*type;
	}

	public Grid2D buildGrid(String sheetToken, Hashtable<String, String> tokens) {
		// triangle grid: need one size
		String line = tokens.get(sheetToken.concat("GridSize"));
		double sx = 1;
		if (line!=null) {
			StringTokenizer st = new StringTokenizer(line);
			if (st.hasMoreTokens())
				sx = Double.parseDouble(st.nextToken());
		}
		return new TriangleGrid2D(0, 0, sx, this.theta);
	}
}
