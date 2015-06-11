/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.poi.POIXMLException;
import org.apache.poi.xdgf.geom.Dimension2dDouble;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFDocument;
import org.apache.poi.xdgf.usermodel.XDGFPage;
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
				b = XDGFCell.parseDoubleValue(cell);
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
	
	// returns this object as a line that extends between the boundaries of
	// the document
	public Path2D.Double getPath() {
		Path2D.Double path = new Path2D.Double();
		
		// this is a bit of a hack, but it works
		double max_val = 100000;
		
		// compute slope..
		double x0 = getX();
		double y0 = getY();
		double x1 = getA(); // second x
		double y1 = getB(); // second y
		
		if (x0 == x1) {
			path.moveTo(x0, -max_val);
			path.lineTo(x0, max_val);
		} else if (y0 == y1) {
			path.moveTo(-max_val, y0);
			path.lineTo(max_val, y0);
		} else {

			// normal case: compute slope/intercept
			double m = (y1 - y0) / (x1 - x0);
			double c = y0 - m*x0;
			
			// y = mx + c
			
			path.moveTo(max_val, m*max_val + c);
			path.lineTo(max_val, (max_val - c)/m);
		}
		
		return path;
	}
}
