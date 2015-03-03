package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

public class ArcTo implements GeometryRow {
	
	ArcTo _master = null;
	
	// The x-coordinate of the ending vertex of an arc.
	Double x = null;
	
	// The y-coordinate of the ending vertex of an arc.
	Double y = null;
	
	// The distance from the arc's midpoint to the midpoint of its chord.
	Double a = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public ArcTo(RowType row) {
		
		if (row.isSetDel()) deleted = row.getDel();
		
		for (CellType cell: row.getCellArray()) {
			String cellName = cell.getN();
			
			if (cellName.equals("X")) {
				x = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("Y")) {
				y = XDGFCell.parseDoubleValue(cell);
			} else if (cellName.equals("A")) {
				a = XDGFCell.parseDoubleValue(cell);
			} else {
				throw new POIXMLException("Invalid cell '" + cellName + "' in ArcTo row");
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

	@Override
	public void setupMaster(GeometryRow row) {
		_master = (ArcTo) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		
		if (getDel()) return;
		
		Point2D last = path.getCurrentPoint();
		
		// intentionally shadowing variables here
		double x = getX();
		double y = getY();
		double a = getA();
		
		double x0 = last.getX();
		double y0 = last.getY();
		
		/*// compute the center of the arc
		double cx = (x0 - x) / 2.0;
		double cy = (y0 - y) / 2.0;
		
		double rx = cx - a;
		double ry = cy - a;
		double rw = 2.0*a;
		double rh = 2.0*a;
		
		double startAngle = Math.toDegrees(Math.atan2(y0 - cy, x0 - cx));
		double endAngle = Math.toDegrees(Math.atan2(y - cy, x - cx));
		
		Arc2D arc = new Arc2D.Double(rx, ry, -rw, rh,
			 						 startAngle, endAngle, Arc2D.OPEN);
			 
		path.append(arc, true);
		*/
		
		double xs = Math.signum(x - x0);
		if (xs == 0)
			xs = 1;
		
		double ys = Math.signum(y - y0);
		if (ys == 0)
			ys = 1;
		
		path.lineTo(x0 + a*xs, y0 + a*ys);
		path.lineTo(x, y);
	}
}
