package org.apache.poi.xdgf.xml;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xdgf.usermodel.XDGFDocument;

public class XDGFXMLDocumentPart extends POIXMLDocumentPart {

	protected XDGFDocument _document;
	
	public XDGFXMLDocumentPart(PackagePart part, PackageRelationship rel, XDGFDocument document) {
		super(part, rel);
		_document = document;
	}
	
}
