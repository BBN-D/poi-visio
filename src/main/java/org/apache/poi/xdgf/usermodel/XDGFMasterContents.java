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
 */
package org.apache.poi.xdgf.usermodel;

import java.io.IOException;

import org.apache.poi.POIXMLException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xdgf.exceptions.XDGFException;
import org.apache.xmlbeans.XmlException;

import com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument;

public class XDGFMasterContents extends XDGFBaseContents {

	private XDGFMaster _master;
	
	public XDGFMasterContents(PackagePart part, PackageRelationship rel, XDGFDocument document) {
		super(part, rel, document);
	}
	
	@Override
	protected void onDocumentRead() {

		try {
		
			try {
				_pageContents = MasterContentsDocument.Factory.parse(getPackagePart().getInputStream()).getMasterContents();
			} catch (XmlException | IOException e) {
				throw new POIXMLException(e);
			}
			
			super.onDocumentRead();
			
		} catch (POIXMLException e) {
			throw XDGFException.wrap(this, e);
		}
	}

	public XDGFMaster getMaster() {
		return _master;
	}

	protected void setMaster(XDGFMaster master) {
		_master = master;
	}

}
