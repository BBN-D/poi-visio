/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 *
 * Partially derived from examples in the POI source code, Apache license
 */

package org.apache.poi.xdgf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.PackageHelper;
import org.apache.xmlbeans.XmlException;

import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1;
import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;

public class XmlVisioDocument extends POIXMLDocument {

	public static String CORE_DOCUMENT = "http://schemas.microsoft.com/visio/2010/relationships/document";
	
	XDGFPages _pages;
	XDGFMasters _masters;
	XDGFDocument _document;
	
	
	public XmlVisioDocument(OPCPackage pkg) throws IOException {
		super(pkg, CORE_DOCUMENT);
		
		VisioDocumentType document;
		
		try {
			document = VisioDocumentDocument1.Factory.parse(getPackagePart().getInputStream()).getVisioDocument();	
		} catch (XmlException | IOException e) {
			throw new POIXMLException(e);
		}
		
		_document = new XDGFDocument(document);
		
		//build a tree of POIXMLDocumentParts, this document being the root
        load(new XDGFFactory(_document));
	}
	
	public XmlVisioDocument(InputStream is) throws IOException {
		this(PackageHelper.open(is));
	}
	
	
	@Override
    protected void onDocumentRead() throws IOException {
		
		// by the time this gets called, all other document parts should
		// have been loaded, so it's safe to build the document structure
		
		// note that in other onDocumentRead(), relations/etc may not have
		// loaded yet, so it's not quite safe
		
		for (POIXMLDocumentPart part: getRelations()) {
			
			// organize the document pieces
			if (part instanceof XDGFPages)
				_pages = (XDGFPages)part;
			
			else if (part instanceof XDGFMasters)
				_masters = (XDGFMasters)part;
		}
		
		if (_masters != null)
			_masters.onDocumentRead();
		
		_pages.onDocumentRead();
	}

	

	@Override
	public List<PackagePart> getAllEmbedds() throws OpenXML4JException {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	//
	// Useful public API goes here
	//

	public Collection<XDGFPage> getPages() {
		return _pages.getPageList();
	}
	

	public XDGFStyleSheet getStyleById(long id) {
		return _document.getStyleById(id);
	}
	
}
