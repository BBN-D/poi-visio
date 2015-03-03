package org.apache.poi.xdgf.util;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;

public class HierarchyPrinter {

	public static void printHierarchy(XDGFPage page, File outDir) throws FileNotFoundException {
		
		File pageFile = new File(outDir, "page" + page.getPageNumber() + "-" + Util.sanitizeFilename(page.getName()) + ".txt");
		
		OutputStream os = new FileOutputStream(pageFile);
		PrintStream pos = new PrintStream(os);
		
		printHierarchy(page, pos);
		
		pos.close();
	}
	
	public static void printHierarchy(XDGFPage page, final PrintStream os) {

		page.getContent().visitShapes(new ShapeVisitor() {
			
			@Override
			public void visit(XDGFShape shape, AffineTransform globalTransform, int level) {
				for (int i = 0; i < level; i++) {
					os.append("  ");
				}
				// TODO: write text?
				os.println(shape.toString() + " [" + shape.getShapeType() + ", " + shape.getSymbolName() + "] " + shape.getMasterShape() + " " + shape.getTextAsString().trim());
			}
		});	
	}
	
	public static void printHierarchy(XmlVisioDocument document, String outDirname) throws FileNotFoundException {
		
		File outDir = new File(outDirname);
		
		for (XDGFPage page: document.getPages()) {
			printHierarchy(page, outDir);
		}
	}
	
	
	public static void main(String [] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: in.vsdx outdir");
			System.exit(1);
		}
		
		String inFilename = args[0];
		String outDir = args[1];
		
		XmlVisioDocument doc = new XmlVisioDocument(new FileInputStream(inFilename));
		printHierarchy(doc, outDir);
	}
}
