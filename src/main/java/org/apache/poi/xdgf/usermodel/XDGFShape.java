package org.apache.poi.xdgf.usermodel;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.POIXMLException;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.poi.xdgf.usermodel.section.CombinedIterable;
import org.apache.poi.xdgf.usermodel.section.GeometrySection;
import org.apache.poi.xdgf.usermodel.section.XDGFSection;
import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;

import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import com.microsoft.schemas.office.visio.x2012.main.TextType;

/**
 * A shape is a collection of Geometry Visualization, Format, Text, Images,
 * and Shape Data in a Drawing Page.
 */
public class XDGFShape extends XDGFSheet {

	XDGFBaseContents _parentPage;
	XDGFShape _parent;			// only non-null if a subshape
	
	XDGFMaster _master = null;
	XDGFShape _masterShape = null;
	
	XDGFText _text = null;
	
	// subshapes if they exist
	List<XDGFShape> _shapes = null;
	
	// properties specific to shapes
	
	// center of rotation relative to origin of parent
	Double _pinX = null;
	Double _pinY = null;
	
	Double _width = null;
	Double _height = null;
	
	// center of rotation relative to self
	Double _locPinX = null;
	Double _locPinY = null;
	
	// start x coordinate, relative to parent
	// -> one dimensional shapes only
	Double _beginX = null;
	Double _beginY = null;
	
	// end x coordinate, relative to parent
	// -> one dimensional shapes only
	Double _endX = null;
	Double _endY = null;
	
	Double _angle = null;
	Double _rotationXAngle = null;
	Double _rotationYAngle = null;
	Double _rotationZAngle = null;
	
	// end x coordinate, relative to parent
	Boolean _flipX = null;
	Boolean _flipY = null;
	
	// center of text relative to this shape
	Double _txtPinX = null;
	Double _txtPinY = null;
	
	// center of text relative to text block
	Double _txtLocPinX = null;
	Double _txtLocPinY = null;
	
	Double _txtAngle = null;
	
	Double _txtWidth = null;
	Double _txtHeight = null;
	
	public XDGFShape(ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document) {
		this(null, shapeSheet, parentPage, document);
	}
	
	public XDGFShape(XDGFShape parent, ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document) {
		
		super(shapeSheet, document);
		
		_parent = parent;
		_parentPage = parentPage;
		
		TextType text = shapeSheet.getText();
		if (text != null)
			_text = new XDGFText(text, this);
		
		if (shapeSheet.isSetShapes()) {
			_shapes = new ArrayList<XDGFShape>();
			for (ShapeSheetType shape: shapeSheet.getShapes().getShapeArray())
				_shapes.add(new XDGFShape(this, shape, parentPage, document));
		}
		
		readProperties();
	}

	@Override
	public String toString() {
		if (_parentPage instanceof XDGFMasterContents)
			return _parentPage + ": <Shape ID=\"" + getID() + "\">";
		else
			return "<Shape ID=\"" + getID() + "\">";
	}
	
	protected void readProperties() {
		
		_pinX = XDGFCell.maybeGetDouble(_cells, "PinX");
		_pinY = XDGFCell.maybeGetDouble(_cells, "PinY");
		_width = XDGFCell.maybeGetDouble(_cells, "Width");
		_height = XDGFCell.maybeGetDouble(_cells, "Height");
		_locPinX = XDGFCell.maybeGetDouble(_cells, "LocPinX");
		_locPinY = XDGFCell.maybeGetDouble(_cells, "LocPinY");
		_beginX = XDGFCell.maybeGetDouble(_cells, "BeginX");
		_beginY = XDGFCell.maybeGetDouble(_cells, "BeginY");
		_endX = XDGFCell.maybeGetDouble(_cells, "EndX");
		_endY = XDGFCell.maybeGetDouble(_cells, "EndY");
		
		_angle = XDGFCell.maybeGetDouble(_cells, "Angle");
		_rotationXAngle = XDGFCell.maybeGetDouble(_cells, "RotationXAngle");
		_rotationYAngle = XDGFCell.maybeGetDouble(_cells, "RotationYAngle");
		_rotationZAngle = XDGFCell.maybeGetDouble(_cells, "RotationZAngle");
		
		_flipX = XDGFCell.maybeGetBoolean(_cells, "FlipX");
		_flipY = XDGFCell.maybeGetBoolean(_cells, "FlipY");
		
		_txtPinX = XDGFCell.maybeGetDouble(_cells, "TxtPinX");
		_txtPinY = XDGFCell.maybeGetDouble(_cells, "TxtPinY");
		_txtLocPinX = XDGFCell.maybeGetDouble(_cells, "TxtLocPinX");
		_txtLocPinY = XDGFCell.maybeGetDouble(_cells, "TxtLocPinY");
		_txtWidth = XDGFCell.maybeGetDouble(_cells, "TxtWidth");
		_txtHeight = XDGFCell.maybeGetDouble(_cells, "TxtHeight");
		
		_txtAngle = XDGFCell.maybeGetDouble(_cells, "TxtAngle");
		
	}
	
	/**
	 * Setup top level shapes
	 * 
	 * Shapes that have a 'Master' attribute refer to a specific master in 
	 * the page, whereas shapes with a 'MasterShape' attribute refer to a 
	 * subshape of a Master.
	 * 
	 * 
	 */
	protected void setupMaster(XDGFPageContents pageContents, XDGFMasterContents master) {
		
		ShapeSheetType obj = getXmlObject();
		
		if (obj.isSetMaster()) {
			_master = pageContents.getMasterById(obj.getMaster());
			if (_master == null)
				throw XDGFException.error("refers to non-existant master " + obj.getMaster(),
										  this);
			
			_masterShape = _master.getContent().getMasterShape();
			if (_masterShape == null)
				throw XDGFException.error("Could not retrieve master shape from " + _master, this);
			
		} else if (obj.isSetMasterShape()) {
			_masterShape = master.getShapeById(obj.getMasterShape());
			if (_masterShape == null)
				throw XDGFException.error("refers to non-existant master shape " + obj.getMasterShape(),
						  				  this);
			
		}
		
		setupSectionMasters();
		
		if (_shapes != null) {
			for (XDGFShape shape: _shapes) {
				shape.setupMaster(pageContents,
								  _master == null ? master : _master.getContent());
			}
		}
	}
	
	protected void setupSectionMasters() {
		
		if (_masterShape == null)
			return;
		
		try {
			for (Entry<String, XDGFSection> section: _sections.entrySet()) {
				XDGFSection master = _masterShape.getSection(section.getKey());
				if (master != null)
					section.getValue().setupMaster(master);
			}
			
			for (Entry<Long, GeometrySection> section: _geometry.entrySet()) {
				GeometrySection master = _masterShape.getGeometryByIdx(section.getKey());
				if (master != null)
					section.getValue().setupMaster(master);
			}
		} catch (POIXMLException e) {
			throw XDGFException.wrap(this.toString(), e);
		}
	}
	
	@Internal
	public ShapeSheetType getXmlObject() {
		return (ShapeSheetType)_sheet;
	}
	
	public long getID() {
		return getXmlObject().getID();
	}
	
	public String getType() {
		return getXmlObject().getType();
	}
	
	public String getTextAsString() {
		XDGFText text = getText();
		if (text == null)
			return "";
		
		return text.getTextContent();
	}
	
	public boolean hasText() {
		return _text != null || (_masterShape != null && _masterShape._text != null);
	}

	@Override
	public XDGFCell getCell(String cellName) {
		XDGFCell _cell = super.getCell(cellName);
		
		// if not found, ask the master
		if (_cell == null && _masterShape != null) {
			_cell = _masterShape.getCell(cellName);
		}
		
		return _cell;
	}

	public GeometrySection getGeometryByIdx(long idx) {
		return _geometry.get(idx);
	}
	
	// only available if this is a shape group
	// -> May be null
	public List<XDGFShape> getShapes() {
		return _shapes;
	}

	// unique to this shape on the page?
	public String getName() {
		String name = getXmlObject().getName();
		if (name == null)
			return "";
		return name;
	}
	
	// unique to this shape on the page?
	public String getShapeType() {
		String type = getXmlObject().getType();
		if (type == null)
			return "";
		return type;
	}
	
	// name of the symbol that this was derived from
	public String getSymbolName() {
		
		if (_master == null)
			return "";
		
		String name = _master.getName();
		if (name == null)
			return "";
		
		return name;
	}
	
	public XDGFShape getMasterShape() {
		return _masterShape;
	}
	
	// returns the parent shape of this shape, if its in a subshape
	public XDGFShape getParentShape() {
		return _parent;
	}
	
	public XDGFShape getTopmostParentShape() {
		XDGFShape top = null;
		if (_parent != null) {
			top = _parent.getTopmostParentShape();
			if (top == null)
				top = _parent;	
		}
					
		return top;
	}
	
	public boolean hasMaster() {
		return _master != null;
	}
	
	public boolean hasMasterShape() {
		return _masterShape != null;
	}
	
	public boolean hasParent() {
		return _parent != null;
	}
	
	public boolean isTopmost() {
		return _parent == null;
	}

	public boolean isShape1D() {
		return getBeginX() != null;
	}
	
	public XDGFText getText() {
		if (_text == null && _masterShape != null)
			return _masterShape.getText();
		
		return _text;
	}

	public Double getPinX() {
		if (_pinX == null && _masterShape != null)
			return _masterShape.getPinX();
		
		if (_pinX == null)
			throw XDGFException.error("PinX not set!", this);
		
		return _pinX;
	}
	
	public Double getPinY() {
		if (_pinY == null && _masterShape != null)
			return _masterShape.getPinY();
		
		if (_pinY == null)
			throw XDGFException.error("PinY not specified!", this);
			
		return _pinY;
	}
	
	public Double getWidth() {
		if (_width == null && _masterShape != null)
			return _masterShape.getWidth();
		
		if (_width == null)
			throw XDGFException.error("Width not specified!", this);
		
		return _width;
	}
	
	public Double getHeight() {
		if (_height == null && _masterShape != null)
			return _masterShape.getHeight();
		
		if (_height == null)
			throw XDGFException.error("Height not specified!", this);
		
		return _height;
	}
	
	public Double getLocPinX() {
		if (_locPinX == null && _masterShape != null)
			return _masterShape.getLocPinX();
		
		if (_locPinX == null)
			throw XDGFException.error("LocPinX not specified!", this);
		
		return _locPinX;
	}
	
	public Double getLocPinY() {
		if (_locPinY == null && _masterShape != null)
			return _masterShape.getLocPinY();
		
		if (_locPinY == null)
			throw XDGFException.error("LocPinY not specified!", this);
		
		return _locPinY;
	}
	
	public Double getBeginX() {
		if (_beginX == null && _masterShape != null)
			return _masterShape.getBeginX();
		
		return _beginX;
	}
	
	public Double getBeginY() {
		if (_beginY == null && _masterShape != null)
			return _masterShape.getBeginY();
		
		return _beginY;
	}
	
	public Double getEndX() {
		if (_endX == null && _masterShape != null)
			return _masterShape.getEndX();
		
		return _endX;
	}
	
	public Double getEndY() {
		if (_endY == null && _masterShape != null)
			return _masterShape.getEndY();
		
		return _endY;
	}
	
	public Double getAngle() {
		if (_angle == null && _masterShape != null)
			return _masterShape.getAngle();
		
		return _angle;
	}
	
	public Boolean getFlipX() {
		if (_flipX == null && _masterShape != null)
			return _masterShape.getFlipX();
		
		return _flipX;
	}
	
	public Boolean getFlipY() {
		if (_flipY == null && _masterShape != null)
			return _masterShape.getFlipY();
		
		return _flipY;
	}
	
    public Double getTxtPinX() {
    	if (_txtPinX == null &&
        	_masterShape != null && _masterShape._txtPinX != null)
            return _masterShape._txtPinX;
        
        if (_txtPinX == null)
        	return getWidth()*0.5;
        
        return _txtPinX;
    }


    public Double getTxtPinY() {
    	if (_txtLocPinY == null &&
        	_masterShape != null && _masterShape._txtLocPinY != null)
            return _masterShape._txtLocPinY;
        
        if (_txtPinY == null)
        	return getHeight()*0.5;
        
        return _txtPinY;
    }


    public Double getTxtLocPinX() {
        if (_txtLocPinX == null &&
        	_masterShape != null && _masterShape._txtLocPinX != null)
            return _masterShape._txtLocPinX;
        
        if (_txtLocPinX == null)
        	return getTxtWidth()*0.5;
        
        return _txtLocPinX;
    }


    public Double getTxtLocPinY() {
        if (_txtLocPinY == null &&
        	_masterShape != null && _masterShape._txtLocPinY != null)
            return _masterShape._txtLocPinY;
        
        if (_txtLocPinY == null)
        	return getTxtHeight()*0.5;
        
        return _txtLocPinY;
    }
    
    public Double getTxtAngle() {
		if (_txtAngle == null && _masterShape != null)
			return _masterShape.getTxtAngle();
		
		return _txtAngle;
	}
    
    public Double getTxtWidth() {
    	if (_txtWidth == null &&
        	_masterShape != null && _masterShape._txtWidth != null)
            return _masterShape._txtWidth;
        
        if (_txtWidth == null)
            return getWidth();
        
        return _txtWidth;
    }


    public Double getTxtHeight() {
        if (_txtHeight == null &&
        	_masterShape != null && _masterShape._txtHeight != null)
            return _masterShape._txtHeight;
        
        if (_txtHeight == null)
            return getHeight();
        
        return _txtHeight;
    }
    
    @Override
	public Double getLineWeight() {
		
    	Double lineWeight = super.getLineWeight();
		if (lineWeight != null)
			return lineWeight;
		
		// get from master
		if (_masterShape != null) {
			return _masterShape.getLineWeight();
		}
		
		// get default
		XDGFStyleSheet style = _document.getDefaultLineStyle();
		if (style != null)
			return style.getLineWeight();
					
		return null;
	}
	
	public Double getFontSize() {
		
		Double fontSize = super.getFontSize();
		if (fontSize != null)
			return fontSize;
		
		// get from master
		if (_masterShape != null) {
			return _masterShape.getFontSize();
		}
		
		// get default
		XDGFStyleSheet style = _document.getDefaultTextStyle();
		if (style != null)
			return style.getFontSize();
					
		return null;
		
	}
	
	
	//
	// Geometry
	//
	
	public Iterable<GeometrySection> getGeometrySections() {
		return new CombinedIterable<>(_geometry,
									  _masterShape != null ? _masterShape._geometry : null);
	}
	

	// returns a rectangle in local coordinates
	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(0, 0, getWidth(),
											getHeight());
	}
	
	// returns bounds as a path in local coordinates
	// -> useful if you need to transform to global coordinates
	public Path2D.Double getBoundsAsPath() {
		
		Double w = getWidth();
		Double h = getHeight();
		
		Path2D.Double bounds = new Path2D.Double();
		bounds.moveTo(0, 0);
		bounds.lineTo(w, 0);
		bounds.lineTo(w, h);
		bounds.lineTo(0, h);
		bounds.lineTo(0, 0);
		
		return bounds;
	}
	
	// returns the path in local coordinates
	public Path2D.Double getPath() {
		for (GeometrySection geoSection: getGeometrySections()) {
			if (geoSection.getNoShow() == true)
				continue;
			
			return geoSection.getPath(this);
		}
		
		return null;
	}
	
	/*
	 * This is strictly true, however shapes that are like this are
	 * typically groups of shapes
	 * 
	public boolean isVisible() {
		for (GeometrySection geoSection: getGeometrySections()) {
			if (geoSection.getNoShow() == false)
				return true;
		}
		return false;
	}
	*/
	
	/**
	 * Returns a transform that can translate shape-local coordinates
	 * to the coordinates of its parent shape
	 */
	protected AffineTransform getParentTransform() {
		// TODO: There's probably a better way to do this
		AffineTransform tr = new AffineTransform();
		
		Double locX = getLocPinX();
		Double locY = getLocPinY();
		Boolean flipX = getFlipX();
		Boolean flipY = getFlipY();
		Double angle = getAngle();
		
		tr.translate(-locX, -locY);
				
		tr.translate(getPinX(), getPinY());

		// rotate about the origin
		if (angle != null && Math.abs(angle) > 0.001) {
			tr.rotate(angle, locX, locY);
		}
		
		// flip if necessary
		
		if (flipX != null && flipX) {
			tr.scale(-1, 1);
			tr.translate(-getWidth(), 0);
		}
		
		if (flipY != null && flipY) {
			tr.scale(1, -1);
			tr.translate(0, -getHeight());
		}
		
		return tr;
	}

	

    /**
     * The visitor will first visit this shape, then it's children
     * 
     * This is useful because exceptions will be marked with the shapes as it
     * propagates up the shape hierarchy.
     */
	public void visitShapes(ShapeVisitor visitor, AffineTransform tr, int level) {
	
		tr = (AffineTransform)tr.clone();
		tr.concatenate(getParentTransform());
		
		try {
			if (visitor.accept(this))
				visitor.visit(this, tr, level);
			
			if (_shapes != null) {
				for (XDGFShape shape: _shapes) {
					shape.visitShapes(visitor, tr, level + 1);
				}
			}
			
		} catch (POIXMLException e) {
			throw XDGFException.wrap(this.toString(), e);
		}
	}
}
