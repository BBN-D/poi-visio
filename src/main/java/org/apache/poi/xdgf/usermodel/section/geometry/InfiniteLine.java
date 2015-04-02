/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section.geometry;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFShape;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;

/**
 * Contains the x- and y-coordinates of two points on an infinite line.
 */
public class InfiniteLine implements GeometryRow {
	
	InfiniteLine _master = null;
	
	// An x-coordinate of a point on the infinite line; paired with y-coordinate represented by the Y cell.
	Double x = null;
	
	// A y-coordinate of a point on the infinite line; paired with x-coordinate represented by the X cell.
	Double y = null;
	
	// An x-coordinate of a point on the infinite line; paired with y-coordinate represented by the B cell.
	Double a = null;
	
	// A y-coordinate of a point on an infinite line; paired with x-coordinate represented by the A cell.
	Double b = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public InfiniteLine(RowType row) {

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
				a = XDGFCell.parseDoubleValue(cell);
			} else {
				throw new POIXMLException("Invalid cell '" + cellName + "' in InfiniteLine row");
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
		_master = (InfiniteLine) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		
		if (getDel()) return;
		
		throw new POIXMLException("InfiniteLine elements cannot be part of a path");
	}
}
