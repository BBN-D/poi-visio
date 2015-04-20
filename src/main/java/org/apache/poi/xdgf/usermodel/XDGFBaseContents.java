/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;

import com.microsoft.schemas.office.visio.x2012.main.ConnectType;
import com.microsoft.schemas.office.visio.x2012.main.PageContentsType;
import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;

/**
 * Container of shapes for a page in a visio document
 */
public class XDGFBaseContents extends XDGFXMLDocumentPart {

	protected PageContentsType _pageContents;
	
	// shapes without parents
	protected List<XDGFShape> _toplevelShapes = new ArrayList<>();
	protected Map<Long, XDGFShape> _shapes = new HashMap<>();
	protected List<XDGFConnection> _connections = new ArrayList<>();
	
	public XDGFBaseContents(PackagePart part, PackageRelationship rel, XDGFDocument document) {
		super(part, rel, document);
	}
	
	@Internal
	public PageContentsType getXmlObject() {
		return _pageContents;
	}
	
	
	@Override
	protected void onDocumentRead() {
		
		if (_pageContents.isSetShapes()) {
			for (ShapeSheetType shapeSheet: _pageContents.getShapes().getShapeArray()) {
				XDGFShape shape = new XDGFShape(shapeSheet, this, _document);
				_toplevelShapes.add(shape);
				addToShapeIndex(shape);
			}
		}
		
		if (_pageContents.isSetConnects()) {
			for (ConnectType connect: _pageContents.getConnects().getConnectArray()) {
				
				XDGFShape from = _shapes.get(connect.getFromSheet());
				XDGFShape to = _shapes.get(connect.getToSheet());
				
				if (from == null)
					throw new POIXMLException(this.toString() + "; Connect; Invalid from id: " + connect.getFromSheet());
				
				if (to == null)
					throw new POIXMLException(this.toString() + "; Connect; Invalid to id: " + connect.getToSheet());
				
				_connections.add(new XDGFConnection(connect, from, to));
			}
		}
	}
	
	protected void addToShapeIndex(XDGFShape shape) {
		_shapes.put(shape.getID(), shape);
		
		List<XDGFShape> shapes = shape.getShapes();
		if (shapes == null)
			return;
		
		for (XDGFShape subshape: shapes)
			addToShapeIndex(subshape);
	}
	
	//
	// API
	//
	
	/**
	 * 
	 * @param graphics
	 */
	public void draw(Graphics2D graphics) {
		visitShapes(new ShapeRenderer(graphics));
	}
	
	
	public XDGFShape getShapeById(long id) {
		return _shapes.get(id);
	}
	
	public Map<Long, XDGFShape> getShapesMap() {
		return Collections.unmodifiableMap(_shapes);
	}
	
	public Collection<XDGFShape> getShapes() {
		return _shapes.values();
	}
	
	public List<XDGFShape> getTopLevelShapes() {
		return Collections.unmodifiableList(_toplevelShapes);
	}
	
	// get connections
	public List<XDGFConnection> getConnections() {
		return Collections.unmodifiableList(_connections);
	}
	
	@Override
	public String toString() {
		return getPackagePart().getPartName().toString();
	}
	

	/**
	 * Provides iteration over the shapes using the visitor pattern, and provides
	 * an easy way to convert shape coordinates into global coordinates
	 */
	public void visitShapes(ShapeVisitor visitor) {
		try {
			for (XDGFShape shape: _toplevelShapes) {
				shape.visitShapes(visitor, new AffineTransform(), 0);
			}
		} catch (POIXMLException e) {
			throw XDGFException.wrap(this, e);
		}
	}
	
}
