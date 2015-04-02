/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.geom;

import java.awt.geom.Dimension2D;

public class Dimension2dDouble extends Dimension2D {

	double width;
	double height;
	
	public Dimension2dDouble() {
		width = 0d;
		height = 0d;
	}
	
	public Dimension2dDouble(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dimension2dDouble) {
			Dimension2dDouble other = (Dimension2dDouble)obj;
			return width == other.width && height == other.height;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		double sum = width + height;
        return (int)Math.ceil(sum * (sum + 1)/2 + width);
	}
	
	@Override
	public String toString() {
		return "Dimension2dDouble[" + width + ", " + height + "]";
	}
}
