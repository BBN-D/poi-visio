package org.apache.poi.xdgf.geom;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GeomUtils {

	
	// determine if two paths intersect each other, and return the
	// points where they intersect
	public static List<Point2D> findIntersections(Path2D path1, Path2D path2) {
		List<Point2D> points = new ArrayList<>();
		findIntersections(path1, path2, points);
		return points;
	}
	
	// determine if two paths intersect each other, and return the
	// points where they intersect
	public static boolean findIntersections(Path2D path1, Path2D path2, List<Point2D> points) {
		return findIntersections(path1, path2, points, 0.01);
	}
	
	// determine if two paths intersect each other, and return the
	// points where they intersect
	// -> Modified from code at https://community.oracle.com/thread/1263985
	public static boolean findIntersections(Path2D path1, Path2D path2, List<Point2D> points, double flatness) {
		
        PathIterator pit = path1.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    Line2D.Double line = new Line2D.Double(lastX, lastY,
                                                           coords[0], coords[1]);
                    findIntersections(path2, line, points, flatness);
                    
                    lastX = coords[0];
                    lastY = coords[1];
            }
            pit.next();
        }
        
        return !points.isEmpty();
    }
	
	// determine if a line intersects a path, and return the points where
	// they intersect
	// -> Modified from code at https://community.oracle.com/thread/1263985
	public static boolean findIntersections(Path2D path, Line2D line, List<Point2D> points, double flatness) {
		
        PathIterator pit = path.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    Line2D.Double next = new Line2D.Double(lastX, lastY,
                                                           coords[0], coords[1]);
                    if(next.intersectsLine(line)) {
                    	//System.out.println("-- next " + getLineRepr(next));
                    	//System.out.println("-- line " + getLineRepr(line));
                    	//System.out.println("-- Pt   " + getLineIntersection(next, line));
                    	points.add(getLineIntersection(next, line));
                    }
                    
                    lastX = coords[0];
                    lastY = coords[1];
            }
            pit.next();
        }
        
        return !points.isEmpty();
    }
	
	public static String getLineRepr(Line2D line) {
		return "Line2D[x1=" + line.getX1() + ", y1=" + line.getY1() + ", x2=" + line.getX2() + ", y2=" + line.getY2() + "]";
	}
	

	// find the point where two lines intersect
	// -> Modified from code at https://community.oracle.com/thread/1264395
	public static Point2D.Double getLineIntersection(Line2D line1, Line2D line2) {
        
		double px = line1.getX1(),
			   py = line1.getY1(),
			   rx = line1.getX2() - px,
			   ry = line1.getY2() - py;
		double qx = line2.getX1(),
			   qy = line2.getY1(),
			   sx = line2.getX2() - qx,
			   sy = line2.getY2() - qy;
		
		double det = sx * ry - sy * rx;
		if (det == 0) {
			// this means the lines are parallel
			return null;
		} else {
			double z = (sx * (qy - py) + sy * (px - qx)) / det;
			//if (z == 0 || z == 1)
			//	return null; // intersection at end point!
			return new Point2D.Double((px + z * rx), (py + z * ry));
		}
    }
	
	// determine if two paths intersect each other
	public static boolean pathIntersects(Path2D path1, Path2D path2) {
		return pathIntersects(path1, path2, 0.01);
	}
	
	// determine if two paths intersect each other
	// -> Modified from code at https://community.oracle.com/thread/1263985
	public static boolean pathIntersects(Path2D path1, Path2D path2, double flatness) {
		
        PathIterator pit = path1.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    Line2D.Double line = new Line2D.Double(lastX, lastY,
                                                           coords[0], coords[1]);
                    if (pathIntersects(path2, line, flatness)) {
                    	return true;
                    }
                    lastX = coords[0];
                    lastY = coords[1];
            }
            pit.next();
        }
        
        return false;
    }
	
	// determine if a line intersects a path
	// -> Modified from code at https://community.oracle.com/thread/1263985
	public static boolean pathIntersects(Path2D path, Line2D line, double flatness) {
		
        PathIterator pit = path.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    Line2D.Double next = new Line2D.Double(lastX, lastY,
                                                           coords[0], coords[1]);
                    if(next.intersectsLine(line)) {
                        return true;
                    }
                    
                    lastX = coords[0];
                    lastY = coords[1];
            }
            pit.next();
        }
        
        return false;
    }
}
