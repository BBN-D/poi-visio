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
 * Contains x- and y-coordinates for a spline's control point and a spline's knot.
 */
public class SplineKnot implements GeometryRow {
	
	SplineKnot _master = null;
	
	// The x-coordinate of a control point.
	Double x = null;
	
	// The y-coordinate of a control point.
	Double y = null;
	
	// One of the spline's knots (other than the last one or the first two).
	Double a = null;
	
	Boolean deleted = null;
	
	// TODO: support formulas
	
	public SplineKnot(RowType row) {

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
				throw new POIXMLException("Invalid cell '" + cellName + "' in SplineKnot row");
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
		_master = (SplineKnot) row;
	}

	@Override
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		throw new POIXMLException("Error: Use SplineRenderer!");
	}
	
	@Override
	public String toString() {
		return "{SplineKnot x=" + getX() + " y=" + getY() + " a=" + getA() + "}";
	}
}
