package org.apache.poi.xdgf.usermodel.section.geometry;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

/**
 * Contains the x- and y-coordinates of the endpoint of a quadratic Bézier curve
 * relative to the shape’s width and height and the x- and y-coordinates of the
 * control point of the curve relative shape’s width and height.
 */
public class RelQuadBezTo implements GeometryRow {
	
	RelQuadBezTo _master = null;
	
	// The x-coordinate of the ending vertex of a quadratic Bézier curve relative to the width of the shape.
	Double x = null;
	
	// The y-coordinate of the ending vertex of a quadratic Bézier curve relative to the height of the shape.
	Double y = null;
	
	// The x-coordinate of the curve’s control point relative to the shape’s width; a point on the arc. The control point is best located about halfway between the beginning and ending vertices of the arc.
	Double a = null;
	
	// The y-coordinate of a curve’s control point relative to the shape’s height.
	Double b = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public RelQuadBezTo(RowType row) {

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
			} else {
				throw new POIXMLException("Invalid cell '" + cellName + "' in RelQuadBezTo row");
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
	
	@Override
	public void setupMaster(GeometryRow row) {
		_master = (RelQuadBezTo) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		
		if (getDel()) return;
		
		double w = parent.getWidth();
		double h = parent.getHeight();
		
		path.quadTo(getA()*w, getB()*h, getX()*w, getY()*h);
	}
}
