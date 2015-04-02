/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.POIXMLException;
import org.apache.poi.util.Internal;

import com.microsoft.schemas.office.visio.x2012.main.DocumentSettingsType;
import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;
import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;

/**
 * Represents the root document: /visio/document.xml
 */
public class XDGFDocument {

	protected VisioDocumentType _document;

	Map<Long, XDGFStyleSheet> _styleSheets = new HashMap<>();
	
	// defaults
	long _defaultFillStyle = 0;
	long _defaultGuideStyle = 0;
	long _defaultLineStyle = 0;
	long _defaultTextStyle = 0;
	
	
	public XDGFDocument(VisioDocumentType document) {
		
		_document = document;
		
		if (!_document.isSetDocumentSettings())
			throw new POIXMLException("Document settings not found");
		
		DocumentSettingsType docSettings = _document.getDocumentSettings();
		
		if (docSettings.isSetDefaultFillStyle())
			_defaultFillStyle = docSettings.getDefaultFillStyle();
		
		if (docSettings.isSetDefaultGuideStyle())
			_defaultGuideStyle = docSettings.getDefaultGuideStyle();
		
		if (docSettings.isSetDefaultLineStyle())
			_defaultLineStyle = docSettings.getDefaultLineStyle();
		
		if (docSettings.isSetDefaultTextStyle())
			_defaultTextStyle = docSettings.getDefaultTextStyle();
		
		if (_document.isSetStyleSheets()) {
			
			for (StyleSheetType styleSheet: _document.getStyleSheets().getStyleSheetArray()) { 
				_styleSheets.put(styleSheet.getID(), new XDGFStyleSheet(styleSheet, this));
			}
		}
	}
	

	@Internal
	public VisioDocumentType getXmlObject() {
		return _document;
	}
	

	public XDGFStyleSheet getStyleById(long id) {
		return _styleSheets.get(id);
	}


	public XDGFStyleSheet getDefaultFillStyle() {
		XDGFStyleSheet style = getStyleById(_defaultFillStyle);
		if (style == null)
			throw new POIXMLException("No default fill style found!");
		return style;
	}
	
	public XDGFStyleSheet getDefaultGuideStyle() {
		XDGFStyleSheet style = getStyleById(_defaultGuideStyle);
		if (style == null)
			throw new POIXMLException("No default guide style found!");
		return style;
	}
	
	public XDGFStyleSheet getDefaultLineStyle() {
		XDGFStyleSheet style = getStyleById(_defaultLineStyle);
		if (style == null)
			throw new POIXMLException("No default line style found!");
		return style;
	}
	
	public XDGFStyleSheet getDefaultTextStyle() {
		XDGFStyleSheet style = getStyleById(_defaultTextStyle);
		if (style == null)
			throw new POIXMLException("No default text style found!");
		return style;
	}

	
}
