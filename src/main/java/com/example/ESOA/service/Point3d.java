package com.example.ESOA.service;

//
//  Point3d.java
//  Point3d
//
//  Created by Patrick Murphy & Conor Brennan on 04/08/2011.
//  Copyright (c) 2011 Dublin City University. All rights reserved.
//


public class Point3d {

    public double x, y, z;

    // Make a constructor
    public Point3d() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Point3d(double the_x, double the_y, double the_z) {
        x = the_x;
        y = the_y;
        z = the_z;
    }

    public double abs() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void setequal(double the_x, double the_y, double the_z) {
        x = the_x;
        y = the_y;
        z = the_z;
    }

    public void setequal(Point3d the_point) {
        x = the_point.x;
        y = the_point.y;
        z = the_point.z;
    }

    public Point3d add(Point3d point1) {
        Point3d result;
        result = new Point3d(x + point1.x, y + point1.y, z + point1.z);
        return result;
    }

    public Point3d minus(Point3d point1) {
        Point3d result;
        result = new Point3d(x - point1.x, y - point1.y, z - point1.z);
        return result;
    }
    
    public void setx( double the_x) {
		x = the_x;
		}
    public void sety( double the_y) {
		y = the_y;
		}
    public void setz( double the_z) {
		z = the_z;
		}
	
    public double x() { 
		return x ; 
	} 
    public double y() { 
		return y ; 
	} 
    public double z() { 
		return z ; 
	} 
	

}