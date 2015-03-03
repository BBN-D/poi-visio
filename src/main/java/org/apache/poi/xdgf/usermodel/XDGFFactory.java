/*
 * Copied from XWPFFactory.java, POI source code, Apache license
 */

package org.apache.poi.xdgf.usermodel;

import java.lang.reflect.Constructor;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLFactory;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class XDGFFactory extends POIXMLFactory {

    private static final POILogger logger = POILogFactory.getLogger(XDGFFactory.class);
    
	private XDGFDocument _document;
	
	public XDGFFactory(XDGFDocument document){
		_document = document;
    }
	
	@Override
	public POIXMLDocumentPart createDocumentPart(POIXMLDocumentPart parent,
			PackageRelationship rel, PackagePart part) {
		POIXMLRelation descriptor = XDGFRelation.getInstance(rel.getRelationshipType());
        if(descriptor == null || descriptor.getRelationClass() == null){
            logger.log(POILogger.DEBUG, "using default POIXMLDocumentPart for " + rel.getRelationshipType());
            return new POIXMLDocumentPart(part, rel);
        }

        try {
            Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
            try {
                Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(POIXMLDocumentPart.class, PackagePart.class, PackageRelationship.class, XDGFDocument.class);
                return constructor.newInstance(parent, part, rel, _document);
            } catch (NoSuchMethodException e) {
                Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(PackagePart.class, PackageRelationship.class, XDGFDocument.class);
                return constructor.newInstance(part, rel, _document);
            }
        } catch (Exception e){
            throw new POIXMLException(e);
        }
	}

	@Override
	public POIXMLDocumentPart newDocumentPart(POIXMLRelation descriptor) {
        try {
            Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
            Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e){
            throw new POIXMLException(e);
        }
	}

}
