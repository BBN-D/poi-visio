package org.apache.poi.xdgf.usermodel;

import java.util.Map;

import org.apache.poi.POIXMLException;
import org.apache.poi.util.Internal;

import com.microsoft.schemas.office.visio.x2012.main.CellType;

/**
 * There are a lot of different cell types. Cell is really just an attribute
 * of the thing that it's attached to. Will probably refactor this once I
 * figure out a better way to use them
 * 
 * The various attributes of a Cell are constrained, and are better defined
 * in the XSD 1.1 visio schema 
 */
public class XDGFCell {

	public static Boolean maybeGetBoolean(Map<String, XDGFCell> cells, String name) {
		XDGFCell cell = cells.get(name);
		if (cell == null)
			return null;
			
		if (cell.getValue().equals("0"))
			return false;
		if (cell.getValue().equals("1"))
			return true;
		
		throw new POIXMLException("Invalid boolean value for '" + cell.getName() + "'");
	}
	
	public static Double maybeGetDouble(Map<String, XDGFCell> cells, String name) {
		XDGFCell cell = cells.get(name);
		if (cell != null)
			return parseDoubleValue(cell._cell);
		return null;
	}
	
	public static Double parseDoubleValue(CellType cell) {
		try {
			return Double.parseDouble(cell.getV());
		} catch (NumberFormatException e) {
			if (cell.getV().equals("Themed"))
				return null;
			throw new POIXMLException("Invalid float value for '" + cell.getN() + "': " + e);
		}
	}
	
	public static Integer parseIntegerValue(CellType cell) {
		try {
			return Integer.parseInt(cell.getV());
		} catch (NumberFormatException e) {
			if (cell.getV().equals("Themed"))
				return null;
			throw new POIXMLException("Invalid integer value for '" + cell.getN() + "': " + e);
		}
	}
	
	// returns a length, converts it to inches?
	public static Double parseVLength(CellType cell) {
		try {
			return Double.parseDouble(cell.getV());
		} catch (NumberFormatException e) {
			if (cell.getV().equals("Themed"))
				return null;
			throw new POIXMLException("Invalid float value for '" + cell.getN() + "': " + e);
		}
	}
	
	
	CellType _cell;
	
	public XDGFCell(CellType cell) {
		_cell = cell;
	}
	
	@Internal
	CellType getXmlObject() {
		return _cell;
	}
	
	/**
	 * Represents the name of the ShapeSheet cell.
	 */
	public String getName() {
		return _cell.getN();
	}
	
	/**
	 * Represents the value of the cell.
	 */
	public String getValue() {
		return _cell.getV();
	}
	
	/**
	 * Represents the element’s formula. This attribute can contain one of the following strings:
	 * - ‘(some formula)’ if the formula exists locally
	 * - No Formula if the formula is locally deleted or blocked
	 * - Inh if the formula is inherited.
	 */
	public String getFormula() {
		return _cell.getF();
	}
	
	/*
	 * Indicates that the formula evaluates to an error. The value of E is the
	 * current value (an error message string); the value of the V attribute is
	 * the last valid value.
	 */
	public String getError() {
		return _cell.getE();
	}
}
