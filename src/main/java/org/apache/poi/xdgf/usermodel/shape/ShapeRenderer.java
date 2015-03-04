package org.apache.poi.xdgf.usermodel.shape;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XDGFText;

/**
 * To use this to render only particular shapes, override it and provide an
 * appropriate implementation of getAcceptor() or accept()
 */
public class ShapeRenderer extends ShapeVisitor {

	protected Graphics2D _graphics;
	
	public ShapeRenderer() {
		_graphics = null;
	}
	
	public ShapeRenderer(Graphics2D g) {
		_graphics = g;
	}
	
	public void setGraphics(Graphics2D g) {
		_graphics = g;
	}
	
	@Override
	public void visit(XDGFShape shape, AffineTransform globalTransform, int level) {
		
		AffineTransform savedTr = _graphics.getTransform();
		_graphics.transform(globalTransform);
		
		drawPath(shape);
		drawText(shape);
		
		// we're done, undo the transforms
		_graphics.setTransform(savedTr);
	}
	
	protected Path2D drawPath(XDGFShape shape) {
		Path2D.Double path = shape.getPath();
		if (path != null) {
			
			// setup the stroke for this line
			
			_graphics.setColor(shape.getLineColor());
			_graphics.setStroke(shape.getStroke());
			_graphics.draw(path);
		}
		
		return path;
	}
	
	protected void drawText(XDGFShape shape) {
		XDGFText text = shape.getText();
		if (text != null) {
			
			if (text.getTextContent().equals("Header"))
				text.getTextBounds();
			
			Font oldFont = _graphics.getFont();
			
			_graphics.setFont(oldFont.deriveFont(shape.getFontSize().floatValue()));
			_graphics.setColor(shape.getFontColor());
			
			text.draw(_graphics);
			_graphics.setFont(oldFont);
		}
	}

}
