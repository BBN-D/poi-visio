package org.apache.poi.xdgf.usermodel.section.geometry;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

public class RelEllipticalArcTo implements GeometryRow {
	
	RelEllipticalArcTo _master = null;
	
	// The x-coordinate of the ending vertex on an arc relative to the width of
	// the shape.
	Double x = null;

	// The y-coordinate of the ending vertex on an arc relative to the height of
	// the shape.
	Double y = null;

	// The x-coordinate of the arc's control point relative to the shape’s
	// width; a point on the arc.
	Double a = null;

	// The y-coordinate of an arc's control point relative to the shape’s width.
	Double b = null;

	// The angle of an arc's major axis relative to the x-axis of its parent.
	Double c = null;

	// The ratio of an arc's major axis to its minor axis. Despite the usual
	// meaning of these words, the "major" axis does not have to be greater than
	// the "minor" axis, so this ratio does not have to be greater than 1.
	Double d = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public RelEllipticalArcTo(RowType row) {

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
				d = XDGFCell.parseDoubleValue(cell);
			} else {
				throw new POIXMLException("Invalid cell '" + cellName + "' in RelEllipticalArcTo row");
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
	
	public Double getD() {
		return d == null ? _master.d : d;
	}
	
	@Override
	public void setupMaster(GeometryRow row) {
		_master = (RelEllipticalArcTo) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		
		if (getDel()) return;
		
		double w = parent.getWidth();
		double h = parent.getHeight();
		
		// intentionally shadowing variables here
		double x = getX()*w;
		double y = getY()*h;
		double a = getA()*w;
		double b = getB()*h;
		double c = getC();
		double d = getD();
		
		EllipticalArcTo.createEllipticalArc(x, y, a, b, c, d, path);
		
	}
}
