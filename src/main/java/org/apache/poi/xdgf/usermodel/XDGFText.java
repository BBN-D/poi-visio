package org.apache.poi.xdgf.usermodel;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import org.apache.poi.util.Internal;

import com.microsoft.schemas.office.visio.x2012.main.TextType;
import com.microsoft.schemas.office.visio.x2012.main.impl.TextTypeImpl;

public class XDGFText {

	TextType _text;
	XDGFShape _parent;
	
	public XDGFText(TextType text, XDGFShape parent) {
		_text = text;
		_parent = parent;
	}
	
	@Internal
	TextType getXmlObject() {
		return _text;
	}
	
	public String getTextContent() {
		// casting here is wrong, but there's no other way of getting the value,
		// as it doesn't seem to be exposed by complex types (even though this
		// is a mixed type)
		return ((TextTypeImpl)_text).getStringValue();
	}
	
	// these are in the shape coordinate system
	// -> See https://msdn.microsoft.com/en-us/library/hh644132(v=office.12).aspx
	public Rectangle2D.Double getTextBounds() {
		
		double txtPinX = _parent.getTxtPinX();
		double txtPinY = _parent.getTxtPinY();
		
		double txtLocPinX = _parent.getTxtLocPinX();
		double txtLocPinY = _parent.getTxtLocPinY();
		
		double txtWidth = _parent.getTxtWidth();
		double txtHeight = _parent.getTxtHeight();
		
		double x = txtPinX - txtLocPinX;
		double y = txtPinY - txtLocPinY;
		
		return new Rectangle2D.Double(x, y, txtWidth, txtHeight);
	}
	
	// returns bounds as a path in local coordinates
	// -> useful if you need to transform to global coordinates
	public Path2D.Double getBoundsAsPath() {
		
		Rectangle2D.Double rect = getTextBounds();
		Double w = rect.getWidth();
		Double h = rect.getHeight();
		
		Path2D.Double bounds = new Path2D.Double();
		bounds.moveTo(0, 0);
		bounds.lineTo(w, 0);
		bounds.lineTo(w, h);
		bounds.lineTo(0, h);
		bounds.lineTo(0, 0);
		
		return bounds;
	}
	
	// assumes graphics is set properly to draw in the right style
	public void draw(Graphics2D graphics) {

		String textContent = getTextContent();
		if (textContent.length() == 0)
			return;
		
		Rectangle2D.Double bounds = getTextBounds();
		
		String[] lines = textContent.trim().split("\n");
		FontRenderContext frc = graphics.getFontRenderContext();
		Font font = graphics.getFont();
		
		AffineTransform oldTr = graphics.getTransform();
		
		// visio is in flipped coordinates, so translate the text to be in the right place
		if (!_parent.getFlipY()) {
			graphics.translate(bounds.x, bounds.y);
			graphics.scale(1, -1);
			graphics.translate(0, -bounds.height + graphics.getFontMetrics().getMaxCharBounds(graphics).getHeight());
		}
		
		if (_parent.getFlipX()) {
			graphics.scale(-1, 1);
			graphics.translate(-bounds.width, 0);
		}
		
		Double txtAngle = _parent.getTxtAngle();
		if (txtAngle != null && Math.abs(txtAngle) > 0.01)
			graphics.rotate(txtAngle);
			
		float nextY = 0;
		for (String line: lines) {
			
			if (line.length() == 0)
				continue;
			
			TextLayout layout = new TextLayout(line, font, frc);
			
			if (layout.isLeftToRight())
				layout.draw(graphics, 0, nextY);
			else
				layout.draw(graphics, (float)(bounds.width - layout.getAdvance()), nextY);
			
			nextY += layout.getAscent() + layout.getDescent() + layout.getLeading();
		}
		
		graphics.setTransform(oldTr);
	}
}
