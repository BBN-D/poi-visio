package org.apache.poi.xdgf.usermodel.section.geometry;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

/**
 * Contains x- and y-coordinates for a spline's second control point, its second
 * knot, its first knot, the last knot, and the degree of the spline.
 */
public class SplineStart implements GeometryRow {
	
	SplineStart _master = null;
	
	// The x-coordinate of a spline's second control point.
	Double x = null;
	
	// The y-coordinate of a spline's second control point.
	Double y = null;
	
	// The second knot of the spline.
	Double a = null;
	
	// The first knot of a spline.
	Double b = null;
	
	// The last knot of a spline.
	Double c = null;
	
	// The degree of a spline (an integer from 1 to 25).
	Integer d = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public SplineStart(RowType row) {

		if (row.isSetDel()) deleted = row.getDel();
		
		for (CellType cell: row.getCellArray()) {
			String cellName = cell.getN();
			
			if (cellName.equals("X")) {
				x = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("Y")) {
				y = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("A")) {
				a = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("B")) {
				b = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("C")) {
				c = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("D")) {
				d = XDGFCell.parseIntegerValue(cell);
			} else {
				throw new POIXMLException("Invalid cell '" + cellName + "' in SplineStart row");
			}
		}
	}
	
	public boolean getDel() {
		if (deleted != null)
			return deleted;
		
		if (_master != null)
			return _master.getDel();
			
		return false;
	}
	
	public Double getX() {
		return x == null ? _master.x : x;
	}
	
	public Double getY() {
		return y == null ? _master.y : y;
	}
	
	public Double getA() {
		return a == null ? _master.a : a;
	}
	
	public Double getB() {
		return b == null ? _master.b : b;
	}
	
	public Double getC() {
		return c == null ? _master.c : c;
	}
	
	public Integer getD() {
		return d == null ? _master.d : d;
	}
	
	@Override
	public void setupMaster(GeometryRow row) {
		_master = (SplineStart) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		throw new POIXMLException("Error: Use SplineRenderer!");
	}
	
	@Override
	public String toString() {
		return "{SplineStart x=" + getX() + " y=" + getY() +
				" a=" + getA() + " b=" + getB() +
				" c=" + getC() + " d=" + getD() +
				"}";
	}
}
