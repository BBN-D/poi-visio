package org.apache.poi.xdgf.usermodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.util.Internal;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
import org.apache.xmlbeans.XmlException;

import com.microsoft.schemas.office.visio.x2012.main.PageType;
import com.microsoft.schemas.office.visio.x2012.main.PagesDocument;
import com.microsoft.schemas.office.visio.x2012.main.PagesType;


/**
 * Contains a list of Page objects (not page content!)
 */
public class XDGFPages extends XDGFXMLDocumentPart {

	PagesType _pagesObject;
	
	// ordered by page number
	List<XDGFPage> _pages = new ArrayList<>();
	
	public XDGFPages(PackagePart part, PackageRelationship rel, XDGFDocument document) {
		super(part, rel, document);
	}
	
	@Internal
	PagesType getXmlObject() {
		return _pagesObject;
	}
	
	@Override
	protected void onDocumentRead() {
		try {
			try {
				_pagesObject = PagesDocument.Factory.parse(getPackagePart().getInputStream()).getPages();
			} catch (XmlException | IOException e) {
				throw new POIXMLException(e);
			}
			
			// this iteration is ordered by page number
			for (PageType pageSettings: _pagesObject.getPageArray()) {
				
				String relId = pageSettings.getRel().getId();
				
				POIXMLDocumentPart pageContentsPart = getRelationById(relId);
				if (pageContentsPart == null)
					throw new POIXMLException("PageSettings relationship for " + relId + " not found");
				
				if (!(pageContentsPart instanceof XDGFPageContents))
					throw new POIXMLException("Unexpected pages relationship for " + relId + ": " + pageContentsPart);
				
				XDGFPageContents contents = (XDGFPageContents)pageContentsPart;
				XDGFPage page = new XDGFPage(pageSettings, contents, _document, this);
				
				contents.onDocumentRead();
				
				_pages.add(page);
			}
			
		} catch (POIXMLException e) {
			throw XDGFException.wrap(this, e);
		}
	}
	
	// ordered by page number
	public List<XDGFPage> getPageList() {
		return Collections.unmodifiableList(_pages);
	}
}
