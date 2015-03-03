package org.apache.poi.xdgf.usermodel.section.geometry;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

public class EllipticalArcTo implements GeometryRow {
	
	EllipticalArcTo _master = null;
	
	// The x-coordinate of the ending vertex on an arc.
	Double x = null;
	
	// The y-coordinate of the ending vertex on an arc.
	Double y = null;
	
	// The x-coordinate of the arc's control point; a point on the arc. The
	// control point is best located about halfway between the beginning and
	// ending vertices of the arc. Otherwise, the arc may grow to an extreme
	// size in order to pass through the control point, with unpredictable
	// results.
	Double a = null;
	
	// The y-coordinate of an arc's control point.
	Double b = null;
	
	// The angle of an arc's major axis relative to the x-axis of its parent shape.
	Double c = null;
	
	// The ratio of an arc's major axis to its minor axis. Despite the usual
	// meaning of these words, the "major" axis does not have to be greater than
	// the "minor" axis, so this ratio does not have to be greater than 1.
	// Setting this cell to a value less than or equal to 0 or greater than 1000
	// can lead to unpredictable results.
	Double d = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public EllipticalArcTo(RowType row) {

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
				throw new POIXMLException("Invalid cell '" + cellName + "' in EllipticalArcTo row");
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
		_master = (EllipticalArcTo) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
	
		if (getDel()) return;
		
		// TODO: This is totally wrong. But.. maybe it's close enough?
		path.lineTo(getA(), getB());
		path.lineTo(getX(), getY());
		
		//path.quadTo(getA(), getB(), getX(), getY());
		/*
		
		Point2D last = path.getCurrentPoint();
		
		// intentionally shadowing variables here
		double x = getX();
		double y = getY();
		double a = getA();
		double b = getB();
		double c = getC();
		double d = getD();
		
		double x0 = last.getX();
		double y0 = last.getY();
		
		// determine the major axis
		
		
		// compute the center of the arc
		// a, b is vaguely the center of the arc
		//double cx = (x0 - x) / 2.0;
		//double cy = (y0 - y) / 2.0;
		
		double rx = Math.max(Math.max(x0, a), x);
		double ry = Math.max(Math.max(y0, b), y);
		double rw = x0 - x;
		double rh = y0 - y;
		
		double startAngle = (180.0/Math.PI*Math.atan2(y0 - b, x0 - a));
		double endAngle = (180.0/Math.PI*Math.atan2(y - b, x - a));
		
		Arc2D arc = new Arc2D.Double(rx, ry, rw, rh,
			 						 startAngle, endAngle, Arc2D.OPEN);
		
		path.append(arc, true);
		
		*/
	}
}
