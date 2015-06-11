/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
