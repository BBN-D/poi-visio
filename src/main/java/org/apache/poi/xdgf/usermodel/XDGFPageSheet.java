/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;

public class XDGFPageSheet extends XDGFSheet {

	PageSheetType _pageSheet;
	
	public XDGFPageSheet(PageSheetType sheet, XDGFDocument document) {
		super(sheet, document);
		_pageSheet = sheet;
	}

	@Override
	PageSheetType getXmlObject() {
		return _pageSheet;
	}

}
