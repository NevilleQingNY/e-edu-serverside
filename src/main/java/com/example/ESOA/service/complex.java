//
//  complex.java
//  propagation
//
//  Created by Conor Brennan on 01/12/2011.
//  Copyright 2011 Dublin City University. All rights reserved.
//

package com.example.ESOA.service  ; 

public class complex {
	public double real, imag ; 
	
	// Make a constructor 
	public complex() {
		real = 0.0 ; 
		imag = 0.0 ; 
	} 
	
	public complex(double the_real, double the_imag) {
		real =  the_real ; 
		imag = the_imag ; 
		} 
	
	
	public double abs() {
		return Math.sqrt(real*real + imag*imag ) ; 
	}

	public complex expj(){
		return new complex( Math.exp(real)*Math.cos(imag), Math.exp(real)*Math.sin(imag) ) ;
		}

	public complex exp_complex(){
		return new complex( Math.exp(real)*Math.cos(imag), Math.exp(real)*Math.sin(imag) ) ;
		}

	public complex exp(){
		return new complex( Math.exp(real)*Math.cos(imag), Math.exp(real)*Math.sin(imag) ) ;
		}

	public complex sin() {
    complex temp1 , temp2, i ;
    i = new complex(0.0,1.0) ;
    
    temp1 = this.mult(i) ; 
    
    temp2 = temp1.exp().minus( (temp1.mult(-1.0)).exp()); 
    temp2 = temp2.div(i.mult(2.0)) ; 
     return temp2 ; 
	}
	
	public complex cos() {
		complex temp1, temp2, i    ;
		i = new complex(0.0,1.0) ; 
	 	temp1 = this.mult(i) ; 
	    temp2 = temp1.exp().plus( (temp1.mult(-1.0)).exp()); 
	    temp2 = temp2.div(2.0) ; 
	     return temp2 ; 
	}		
	
		public complex sqrt(){
	
		double length = Math.sqrt(real*real + imag*imag);
		length = Math.sqrt(length); 
		double phase = Math.atan2(imag,real) ; 
		phase = phase/2.0 ; 
		
		complex temp = new complex(0.0,phase) ; 
		temp = temp.exp() ; 
		temp = temp.mult(length) ; 
		return temp ; 
	}
	
	public void setequal( complex the_complex) {
		real = the_complex.real;
		imag = the_complex.imag ; 
	}

	public void setreal( double the_real) {
		real = the_real;
		}

	public void setimag( double the_imag) {
		imag = the_imag;
		}
	
	public complex plus( complex number1) {
		complex result ; 
		result = new complex(real + number1.real, imag+ number1.imag) ;
		return result ; 
	}
	
	
	public complex minus( complex number1) {
		complex result ; 
		result = new complex(real - number1.real, imag - number1.imag) ;
		return result ; }
	
	public complex conj( ) {
		complex result ; 
		result = new complex(real , -1.0*imag) ;
	return result ; }
	
	
	public complex mult(double number1) {
		complex result ; 
		result = new complex(real*number1,imag*number1) ;
		return result ; }
	
	public complex div(double number1) {
		complex result ; 
		result = new complex(real/number1,imag/number1) ;
	return result ; }
	
	public complex mult(complex number1) {
		complex result ; 
		result = new complex(real*number1.real - imag*number1.imag,real*number1.imag + imag*number1.real) ;
		return result ; }
	
	public complex div(complex number1) {
		complex result ;
		double temp  = number1.abs()*number1.abs() ; 
		result =  new complex((real*number1.real + imag*number1.imag)/temp,(imag*number1.real-real*number1.imag)/temp) ;
	return result ; }
	
   public complex ln(){
	   
   complex result ; 
   double r, theta ; 
   r = Math.sqrt(real * real + imag * imag) ; 
   theta = Math.atan2(imag,real) ; 
   result = new complex( Math.log(r) , theta ) ; 
   return result ;    
   }
	
	public double re() { 
		return real ; 
	} 
	
	public double im() { 
		return imag ; 
	} 
	
	
	
	
	
	
	
	

}
