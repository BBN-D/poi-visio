package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Path2D;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

public class Ellipse implements GeometryRow {
	
	Ellipse _master = null;
	
	Double x = null;
	Double y = null;
	Double a = null;
	Double b = null;
	Double c = null;
	Double d = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public Ellipse(RowType row) {
		
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
				throw new POIXMLException("Invalid cell '" + cellName + "' in Ellipse row");
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
		_master = (Ellipse) row;
	}

	public Path2D.Double getPath() {
		
		if (getDel()) return null;
		
		//throw new POIXMLException("Ellipse elements not implemented yet");
		
		//Ellipse2D.Double ellipse = new
		return null;
		
		//Path2D.Double path = new 
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		throw new POIXMLException("Ellipse elements cannot be part of a path");
	}
}
