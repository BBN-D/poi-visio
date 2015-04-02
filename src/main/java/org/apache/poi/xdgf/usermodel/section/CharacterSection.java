/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFSheet;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.microsoft.schemas.office.visio.x2012.main.RowType;
import com.microsoft.schemas.office.visio.x2012.main.SectionType;

public class CharacterSection extends XDGFSection {
	
	Double _fontSize = null;
	Color _fontColor = null;
	
	Map<String, XDGFCell> _characterCells = new HashMap<>();
	
	public CharacterSection(SectionType section, XDGFSheet containingSheet) {
		super(section, containingSheet);
		
		// there aren't cells for this, just a single row
		RowType row = section.getRowArray(0);
		
		for (CellType cell: row.getCellArray()) {
			_characterCells.put(cell.getN(), new XDGFCell(cell));
		}
		
		if (row != null) {
			_fontSize = XDGFCell.maybeGetDouble(_characterCells, "Size");
			
			String tmpColor = XDGFCell.maybeGetString(_characterCells, "Color");
			if (tmpColor != null)
				_fontColor = Color.decode(tmpColor);
		}
	}
	
	public Double getFontSize() {
		return _fontSize;
	}
	
	public Color getFontColor() {
		return _fontColor;
	}

	@Override
	public void setupMaster(XDGFSection section) {
		
	}
	
}

