package org.apache.poi.xdgf.geom;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.section.geometry.SplineKnot;
import org.apache.poi.xdgf.usermodel.section.geometry.SplineStart;

import com.graphbuilder.curve.ControlPath;
import com.graphbuilder.curve.ShapeMultiPath;
import com.graphbuilder.curve.ValueVector;
import com.graphbuilder.geom.PointFactory;

public class SplineCollector {

	SplineStart _start;
	ArrayList<SplineKnot> _knots = new ArrayList<>();

	public SplineCollector(SplineStart start) {
		_start = start;
	}
	
	public void addKnot(SplineKnot knot) {
		if (!knot.getDel())
			_knots.add(knot);
	}
	
	public void addToPath(java.awt.geom.Path2D.Double path, XDGFShape parent) {
		// ok, we have the start, and all knots... do something with this
		
		Point2D last = path.getCurrentPoint();
		
		// create a control path and knots
		ControlPath controlPath = new ControlPath();
		ValueVector knots = new ValueVector(_knots.size() + 3);
		
		double firstKnot = _start.getB();
		double lastKnot = _start.getC();
		int degree = _start.getD();
		
		// first/second knot
		knots.add(firstKnot);
		knots.add(_start.getA());
		
		// first/second control point
		controlPath.addPoint(PointFactory.create(last.getX(), last.getY()));
		controlPath.addPoint(PointFactory.create(_start.getX(), _start.getY()));
		
		// middle knots/control points
		for (SplineKnot knot: _knots) {
			knots.add(knot.getA());
			controlPath.addPoint(PointFactory.create(knot.getX(), knot.getY()));
		}
		
		// last knot
		knots.add(lastKnot);
		
		ShapeMultiPath shape = SplineRenderer.createNurbsSpline(controlPath, knots, null, degree);
		path.append(shape, true);
	}
}
