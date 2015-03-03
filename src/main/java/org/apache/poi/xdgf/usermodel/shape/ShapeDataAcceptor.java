package org.apache.poi.xdgf.usermodel.shape;

import org.apache.poi.xdgf.usermodel.XDGFShape;

/**
 * This acceptor only allows traversal to shapes that have useful data
 * associated with them, and tries to elide details that aren't useful
 * when analyzing the content of a document.
 * 
 * Useful is subjective of course, and is defined as any of:
 * 
 * - Has non-empty text
 * - Is a 1d shape, such as a line
 * - User specified shapes
 * - The outline of stencil objects
 * - TODO
 */
public class ShapeDataAcceptor implements ShapeVisitorAcceptor {

	@Override
	public boolean accept(XDGFShape shape) {
		
		// text is interesting
		if (shape.hasText() && shape.getTextAsString().length() != 0)
			return true;
		
		// 1d shapes are interesting, they create connections
		if (shape.isShape1D())
			return true;
		
		// User specified shapes are interesting
		if (!shape.hasMaster() && !shape.hasMasterShape())
			return true;
		
		if (shape.hasMaster() && !shape.hasMasterShape())
			return true;
		
		// include stencil content, but try to elide stencil interiors
		//if (shape.getXmlObject().isSetMaster())
		//	return true;
		
		if (shape.hasMasterShape() && shape.getMasterShape().isTopmost())
			return true;
		
		return false;
	}
	
	
}
