/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.exceptions;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;

public class XDGFException {

	/**
	 * Creates an error message to be thrown
	 */
	public static POIXMLException error(String message, Object o) {
		return new POIXMLException(o.toString() + ": " + message);
	}
	
	public static POIXMLException error(String message, Object o, Throwable t) {
		return new POIXMLException(o.toString() + ": " + message, t);
	}
	
	//
	// Use these to wrap error messages coming up so that we have at least
	// some idea where the error was located
	//

	public static POIXMLException wrap(POIXMLDocumentPart part, POIXMLException e) {
		return new POIXMLException(part.getPackagePart().getPartName().toString() + ": " + e.getMessage(),
								  e.getCause() == null ? e : e.getCause());
	}
	
	public static POIXMLException wrap(String where, POIXMLException e) {
		return new POIXMLException(where + ": " + e.getMessage(),
								  e.getCause() == null ? e : e.getCause());
	}
}
