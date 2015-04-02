/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
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
	public void addToPath(Path2D.Double path, XDGFShape parent) {
		
		if (getDel()) return;
		
		Point2D last = path.getCurrentPoint();
		
		// intentionally shadowing variables here
		double x = getX();
		double y = getY();
		double a = getA();
		
		if (a == 0) {
			path.lineTo(x, y);
			return;
		}
		
		double x0 = last.getX();
		double y0 = last.getY();
		
		double chordLength = Math.hypot(y - y0, x - x0);
		double radius = (4 * a * a + chordLength * chordLength) / (8 * Math.abs(a));
		
		// center point
		double cx = x0 + (x - x0) / 2.0;
		double cy = y0 + (y - y0) / 2.0;
		
		double rotate = Math.atan2(y - cy, x - cx);
		
		Arc2D arc = new Arc2D.Double(x0, y0 - radius,
									 chordLength, 2*radius,
									 180, x0 < x ? 180 : -180,
									 Arc2D.OPEN);
		
		path.append(AffineTransform.getRotateInstance(rotate, x0, y0).createTransformedShape(arc), true);
	}
}
