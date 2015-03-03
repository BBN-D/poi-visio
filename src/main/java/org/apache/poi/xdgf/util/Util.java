package org.apache.poi.xdgf.util;

public class Util {
	
	public static int countLines(String str) {
	    int lines = 1;
	    int pos = 0;
	    while ((pos = str.indexOf("\n", pos) + 1) != 0) {
	        lines++;
	    }
	    return lines;
	}
	
	// this probably isn't 100% correct, so don't use it in security-sensitive
	// applications!
	// from: http://www.rgagnon.com/javadetails/java-0662.html
	public static String sanitizeFilename(String name) {
		return name.replaceAll("[:\\\\/*\"?|<>]", "_");
	}
}
