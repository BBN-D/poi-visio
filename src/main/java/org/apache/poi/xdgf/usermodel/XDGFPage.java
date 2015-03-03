package org.apache.poi.xdgf.usermodel;

import org.apache.poi.POIXMLException;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.geom.Dimension2dDouble;

import com.microsoft.schemas.office.visio.x2012.main.PageType;

/**
 * Provides the API to work with an underlying page
 */
public class XDGFPage {

	PageType _page;
	XDGFPageContents _content;
	XDGFPages _pages;
	XDGFSheet _pageSheet = null;
	
	public XDGFPage(PageType page, XDGFPageContents content, XDGFDocument document, XDGFPages pages) {
		_page = page;
		_content = content;
		_pages = pages;
		content.setPage(this);
		
		if (page.isSetPageSheet())
			_pageSheet = new XDGFPageSheet(page.getPageSheet(),document);
	}
	
	@Internal
	PageType getXmlObject() {
		return _page;
	}
	
	public long getID() {
		return _page.getID();
	}
	
	public String getName() {
		return _page.getName();
	}
	
	public XDGFPageContents getContent() {
		return _content;
	}
	
	public XDGFSheet getPageSheet() {
		return _pageSheet;
	}
	
	public long getPageNumber() {
		return _pages.getPageList().indexOf(this) + 1;
	}
	
	public Dimension2dDouble getPageSize() {
		XDGFCell w = _pageSheet.getCell("PageWidth");
		XDGFCell h = _pageSheet.getCell("PageHeight");
		
		if (w == null || h == null)
			throw new POIXMLException("Cannot determine page size");
		
		return new Dimension2dDouble(Double.parseDouble(w.getValue()),
									 Double.parseDouble(h.getValue()));
	}
}
