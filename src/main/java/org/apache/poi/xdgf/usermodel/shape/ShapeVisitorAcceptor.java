package org.apache.poi.xdgf.usermodel.shape;

import org.apache.poi.xdgf.usermodel.XDGFShape;

public interface ShapeVisitorAcceptor {

	public boolean accept(XDGFShape shape);
	
}
