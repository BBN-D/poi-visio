/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel;

import org.apache.poi.util.Internal;

import com.microsoft.schemas.office.visio.x2012.main.MasterType;

/**
 * Provides the API to work with an underlying master
 */
public class XDGFMaster {

	private MasterType _master;
	private XDGFMasterContents _content;
	XDGFSheet _pageSheet = null;
	
	public XDGFMaster(MasterType master, XDGFMasterContents content, XDGFDocument document) {
		_master = master;
		_content = content;
		content.setMaster(this);
		
		if (master.isSetPageSheet())
			_pageSheet = new XDGFPageSheet(master.getPageSheet(), document);
	}
	
	@Internal
	MasterType getXmlObject() {
		return _master;
	}
	
	@Override
	public String toString() {
		return "<Master ID=\"" + getID() + "\" " + _content + ">";
	}
	
	public long getID() {
		return _master.getID();
	}
	
	public String getName() {
		return _master.getName();
	}
	
	public XDGFMasterContents getContent() {
		return _content;
	}
	
	public XDGFSheet getPageSheet() {
		return _pageSheet;
	}
	
}
