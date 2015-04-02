/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section;

import org.apache.poi.xdgf.usermodel.XDGFSheet;

import com.microsoft.schemas.office.visio.x2012.main.SectionType;

public class GenericSection extends XDGFSection {

	public GenericSection(SectionType section, XDGFSheet containingSheet) {
		super(section, containingSheet);
	}

	@Override
	public void setupMaster(XDGFSection section) {
	}
}
