/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel;

import org.apache.poi.util.Internal;

import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;

public class XDGFStyleSheet extends XDGFSheet {
	
	public XDGFStyleSheet(StyleSheetType styleSheet, XDGFDocument document) {
		super(styleSheet, document);
	}
	
	@Internal
	public StyleSheetType getXmlObject() {
		return (StyleSheetType)_sheet;
	}
	
	
}
