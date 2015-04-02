/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section.geometry;

import java.awt.geom.Path2D;

import org.apache.poi.xdgf.usermodel.XDGFShape;


public interface GeometryRow {
	
	public void setupMaster(GeometryRow row);

	public void addToPath(Path2D.Double path, XDGFShape parent);
	
}
