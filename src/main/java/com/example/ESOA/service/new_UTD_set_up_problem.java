package com.example.ESOA.service;




import java.io.*;
import java.util.*;


public class new_UTD_set_up_problem {
	public	double		f,  Gamma, T, omega, lambda_0,  eta_0, eta_1, k_0, k_1, f_in_GHz, wedge_angle, epsilon_r, epsilon, mu_1, theta_inc, radius_in_wavelengths ;
	public	double		t_start, t_end,  t_step, z_start, z_end, z_step, mu, mu_r  , sigma, delta_theta, c_0, rho, phi_dash, propagation_direction, reflection_propagation_direction, n  ;
	public	int			N, Nt, tutorial_choice, No_of_angles ; 
	public 	complex 	prop_temp1, prop_temp2 , prop_const_material, prop_const_free_space, eta_free_space, eta_material ; 
	public 	complex 	reflection_coefficient, transmission_coefficient ; 
	
	public double		x,y,kx_i,ky_i, kx_r, ky_r ; 
	public double[]		C1, S1 ; 
	public double		TX_h, RX_x_distance ; 
	
	
	
	public	void read_in_variables(String TX_height_value, String frequency_value, String RX_distance_value, String epsilon_r_value){

		TX_h		= Double.parseDouble(TX_height_value); 
		f_in_GHz		=	Double.parseDouble(frequency_value);
		RX_x_distance  = Double.parseDouble(RX_distance_value) ; 
		epsilon_r		= Double.parseDouble(epsilon_r_value) ;
	}

	public	void initialise_variables(){
		f	=	f_in_GHz*1000.0*global_definitions.MHz ; 
		
		omega = 2.0*global_definitions.pi* f ;
		epsilon = epsilon_r*global_definitions.epsilon_0 ; 
		mu = global_definitions.mu_0 ; 
		
		
	
		c_0 = 1.0/Math.sqrt(global_definitions.mu_0*global_definitions.epsilon_0) ;
		lambda_0 = c_0/f ; 
		rho = lambda_0*radius_in_wavelengths ; 

		
		prop_temp1  =  new complex(0.0,omega*global_definitions.mu_0) ;
		prop_temp2 = new complex(0.0,omega*global_definitions.epsilon_0) ; 
		prop_const_free_space = prop_temp1.mult(prop_temp2) ; 
		prop_const_free_space = prop_const_free_space.sqrt() ; 

		eta_free_space = new complex(Math.sqrt(global_definitions.mu_0/global_definitions.epsilon_0),0.0) ;
		
		prop_temp1  =  new complex(0.0,omega*mu) ;
		prop_temp2 = new complex(sigma,omega*epsilon) ; 
		prop_const_material = prop_temp1.mult(prop_temp2) ; 
		prop_const_material = prop_const_material.sqrt() ; 
		eta_material = new complex(0.0,omega*mu).div(new complex(sigma,omega*epsilon)) ;
		eta_material = eta_material.sqrt() ; 
		
		
		
		No_of_angles = 180 ; 
		delta_theta = (2.0*global_definitions.pi - wedge_angle*global_definitions.pi/180.0 )/No_of_angles ;
	}
	
	public void initialise_fresnel_data(){
		C1 = new double[57] ;
		S1 = new double[57] ;
		
		C1[0] = 0.62666 ;  C1[1] = 0.52666 ; C1[2] = 0.42669 ;  C1[3] = 0.32690 ;  C1[4] = 0.22768 ; 
		C1[5] = 0.12977 ; C1[6] = 0.03439 ; C1[7] = -0.05672 ; C1[8] = -0.14119 ; C1[9] = -0.21606 ; 
		C1[10] = -0.27787 ; C1[11] = -0.32285 ; C1[12] = -0.34729 ; C1[13] = -0.34803 ; C1[14] = -0.32312 ; 
		C1[15] = -0.27253 ; C1[16] = -0.19886 ; C1[17] = -0.10790 ; C1[18] = -0.00871 ; C1[19] = 0.08680 ; 
		C1[20] = 0.16520 ; C1[21] = 0.21359 ; C1[22] = 0.22242 ; C1[23] = 0.18833 ; C1[24] = 0.11650 ; 
		C1[25] = 0.02135 ; C1[26] = -0.07518 ; C1[27] = -0.14816 ; C1[28] = -0.17646 ; C1[29] = -0.15021 ; 
		C1[30] = -0.07621 ;C1[31] = 0.02152 ; C1[32] = 0.10791 ;C1[33] = 0.14907 ; C1[34] = 0.12691 ;
		C1[35] = 0.04965 ;C1[36] = -0.04819 ;C1[37] = -0.11929 ; C1[38] = -0.12649 ; C1[39] = -0.06469 ;
		C1[40] = 0.03219 ;C1[41] = 0.10690 ;C1[42] = 0.11228 ;C1[43] = 0.04374 ; C1[44] = -0.05287 ;
		C1[45] = -0.10884 ; C1[46] = -0.08188 ; C1[47] = 0.00810 ; C1[48] = 0.08905 ; C1[49] = 0.09277 ;
		C1[50] = 0.01519 ; C1[51] = -0.07411 ; C1[52] = -0.09125; C1[53] = -0.01892 ; C1[54] = 0.07063 ;
		C1[55] = 0.08408 ;C1[56] = 0.00641 ;

		S1[0] = 0.62666 ; S1[1] = 0.62632 ; S1[2] = 0.62399 ; S1[3] = 0.61766 ; S1[4] = 0.60536 ; 
		S1[5] = 0.58518 ; S1[6] = 0.55532 ; S1[7] = 0.51427 ; S1[8] = 0.46092 ; S1[9] = 0.39481 ; 
		S1[10] = 0.31639 ; S1[11] = 0.22728 ; S1[12] = 0.13054 ; S1[13] = 0.03081 ; S1[14] = -0.06573 ; 
		S1[15] = -0.15158 ; S1[16] = -0.21861 ; S1[17] = -0.25905 ; S1[18] = -0.26682 ; S1[19] = -0.23918 ; 
		S1[20] = -0.17812 ; S1[21] = -0.09141 ; S1[22] = 0.00743 ; S1[23] = 0.10054 ; S1[24] = 0.16879 ; 
		S1[25] = 0.19614 ; S1[26] = 0.17454 ; S1[27] = 0.10789 ; S1[28] = 0.01329 ; S1[29] = -0.08181 ; 
		S1[30] = -0.14690 ;S1[31] = -0.15883 ; S1[32] = -0.11181 ;S1[33] = -0.02260 ;S1[34] = 0.07301 ;
		S1[35] = 0.13335 ;S1[36] = 0.12973 ;S1[37] = 0.06258 ;S1[38] = -0.03483 ;S1[39] = -0.11030 ;
		S1[40] = -0.12048 ; S1[41] = -0.05815 ;S1[42] = 0.03885 ;S1[43] = 0.10751 ;S1[44] = 0.10038 ;
		S1[45] = 0.02149 ;S1[46] = -0.07126 ;S1[47] = -0.10594 ;S1[48] = -0.05381 ;S1[49] = 0.04224 ;
		S1[50] = 0.09874 ;S1[51] = 0.06405 ;S1[52] = -0.03004;S1[53] = -0.09235 ;S1[54] = -0.05976 ;
		S1[55] = 0.03440 ;S1[56] = 0.08900 ;
			}

	public complex compute_fresnel(double x){
			complex	result  ;
			complex 	j_pi_over_4 ; 
			complex 	term1, term2, term3, term4, term5  ; 
			int 		index ; 
			double		factor1, factor2 ; 
			
			result = new complex(0.0,0.0) ; 
			
			j_pi_over_4 = new complex(0.0,Math.PI/4.0); 
			
	
				if(x <= 0.3 ){
				term1 = new complex(Math.sqrt(Math.PI*x),0.0)  ;
				
				term2 = (j_pi_over_4.exp_complex()).mult((2.0*x)) ; 
				
				
				
				term3 = ((j_pi_over_4.mult(-1.0)).exp_complex()).mult( new complex((2.0/3.0)*x*x,0.0)) ; 
				result = (term1.minus(term2)).minus(term3) ; 
				result = result.mult( (j_pi_over_4.plus(new complex(0.0,x))).exp_complex() ) ; 
				
				}
				
				if(x >= 5.5 ){
				term1 = new complex(1.0,0.0) ; 
				term2 = (new complex(0.0,1.0)).div((2.0*x)) ; 
				term3 = new complex(0.75/(x*x),0.0) ; 
				term4 = (new complex(0.0,15.0/8.0)).div((x*x*x)) ; 
				term5 = new complex((75.0/16.0)/(x*x*x*x),0.0) ; 
				result = (((term1.plus(term2)).minus(term3)).minus(term4)).plus(term5) ;
				
				}
				
				
				if( (x > 0.3) && (x < 5.5) ) {

					
				index = (int) Math.floor((float)(Math.sqrt(x)/0.1))   ; 

				

				factor1 = Math.sqrt(x) - index*0.1 ; 
				factor2 = 0.1 - factor1 ;
				
		//		System.out.println(" factors  " + factor1);
			//	System.out.println(" factors  " + factor2);
				

				result = new complex( (C1[index]*factor2 + C1[index+1]*factor1)/0.1,0.0) ; 

				//System.out.println(" result  " + result.abs());
				result = result.minus( new complex(0.0,(S1[index]*factor2 + S1[index+1]*factor1)/0.1))  ; 
				result = result.mult(new complex(0.0,2.0*Math.sqrt(x))) ; 
				result = result.mult( (new complex(0.0,x)).exp_complex()) ; 
				
				}
				
				return result ; 
				
	}

	
	public String compute_fields_along_line(){ 
		
		String fields_along_line  = new String() ; 
		String incident_values  = new String() ;
		String reflected_values  = new String() ;
		String GO_values  = new String() ;
		String D_values = new String(); 
		String total_values = new String() ; 
		
		String field_point_values = new String() ; 
		
		
		
		
		double ymin = -5 ; 
		double ymax = 0 ; 
		double n = 1.5 ; 

		double xmin = -5.0 ; 
		double	xmax = 0.0 ; 
		
		double x_edge1 = xmin;
		double x_edge2 = xmax;

		double TX_x  = xmin ; 
		double TX_y = TX_h  ;
		double h = TX_y   ;
		
		double RX_x =  RX_x_distance ; 

		double h_dash = h*RX_x/(xmax - xmin) ;  // Used to compute where LOS and reflections occur. assumes vertical path. 


		double RX_Start_x = RX_x  ; 
		double RX_Start_y = -5.0 ;   //RX Line Start Point
		double RX_End_x = RX_x ; 
		double RX_End_y = 5.0 ;  //RX Line End Point
		
		double Line_Length = RX_End_y - RX_Start_y;
		int  Points = 1001;  // Total Points

		
		double step = Line_Length/Points;
		double[] RX_points_x ;
		double[] RX_points_y ;
		double[] s ; 
		int[] LOS ; 
		
		LOS = new int[Points] ; 
		s = new double[Points] ;
		
		RX_points_x =  new double[Points] ; 
		RX_points_y =  new double[Points] ; 
		int ct ; 
		
		
		
		initialise_fresnel_data() ; 

		for (ct = 0; ct < Points; ct++){
		    RX_points_x[ct] = RX_Start_x;
		    RX_points_y[ct] = RX_Start_y + (ct*step);
		    
		    field_point_values = field_point_values + RX_points_y[ct] + " \n" ; 
		    }


	
		double xs ; 
		double ys ; 
		

		for(ct=0; ct < Points ; ct++){
		    
		    xs = RX_points_x[ct] -TX_x;
		    ys = RX_points_y[ct] -TX_y ;
		    s[ct] = Math.sqrt((xs*xs)+(ys*ys));    // Distance between TX and RX Point
		    
		    if( RX_points_y[ct] > -h_dash) 
		        LOS[ct] = 1 ; 
		        else
		        	LOS[ct] = 0 ; 
		    
		}

		 double image_x , image_y ; 
		double y_line ; 
		int ref_count; 
		
		image_x = TX_x ; 
		image_y  = -h ;            //Image Point
		
		
		y_line = 0;     //visible edge line equation: y=3
		
		ref_count=0;    // Reflection Points Counter 
		
		double[] reflection_point_x ;
		double[] reflection_point_y ;
		int[] reflection_exists ; 
		reflection_point_x = new double[Points] ; 
		reflection_point_y = new double[Points] ; 
		reflection_exists = new int[Points] ; 		
		double x_pt ; 
		
		for (ct=0 ; ct < Points ; ct++){
		    
		    x_pt = ( (y_line - RX_points_y[ct])*image_x - (y_line-image_y)*RX_points_x[ct] )/(image_y - RX_points_y[ct]);   // See Sajjad report
		    		 
		     
		    		
		    
		    if( RX_points_y[ct] > h_dash ){
		        
		    if(x_pt <= x_edge2){ 
		    	if( x_pt >= x_edge1){
		    
		        reflection_exists[ct] = 1 ;  
		        reflection_point_x[ct] = x_pt;
		    	}
		    }
		    
		    }
		}

		for (ct=0; ct < ref_count; ct++){
		    reflection_point_y[ct] = y_line ;   // All reflection points lie on same edge
		}
		
		
		
		double[] s1 ; 
		s1= new double[Points] ;
		double[] s2 ; 
		s2= new double[Points]  ;
		double xs1, ys1 , xs2 , ys2  ; 
		
		
				
		for(ct=0 ; ct < Points ; ct++){

		    xs1 = reflection_point_x[ct] - TX_x ;
		    ys1 = reflection_point_y[ct] - TX_y ;
		    
		    s1[ct] = Math.sqrt( (xs1*xs1) + (ys1*ys1) );   //Distances between TX and Reflection Point
		    
		    xs2 = RX_points_x[ct] - reflection_point_x[ct] ; 
		    ys2 = RX_points_y[ct] - reflection_point_y[ct] ;
		    
		    s2[ct] = Math.sqrt( (xs2*xs2)+(ys2*ys2));       //Distances between RX points and Reflection Point
		}

		
		

		double[] theta_i ; 
		double[] theta_t ; 
		double[] 	Ref_Coefficient ; 
		Ref_Coefficient = new double[Points] ;
		theta_i = new double[Points] ;
		theta_t = new double[Points] ; 
		double perp ; 
		

		/********************************/
		// Need to define k0 and k !! 
		//	Also need eta etc...
		double k0, k  ; 
		double eta1, eta2 ; 
		/*******************************/
		
		
		k0 = prop_const_free_space.im(); 
		k = prop_const_material.im() ; 
		eta1 = eta_free_space.re()  ; 
		eta2= eta_material.re() ; 
		
       	//System.out.println(" k0:  " + k0 );
       	//System.out.println(" k:  " + k );
       	//System.out.println(" eta0:  " + eta1 );
       	//System.out.println(" eta1:  " + eta2 );
       	

		

		for(ct=0; ct < Points; ct++){ //Calculation of Reflection Coefficients. See Report
		 
			if(reflection_exists[ct] == 1){ 
		    
				perp = reflection_point_x[ct] -TX_x;
				theta_i[ct] = Math.asin(perp/s1[ct]);
				theta_t[ct]= Math.asin( (k0/k)*Math.sin(theta_i[ct]) );
		    Ref_Coefficient[ct] = (  eta2*Math.cos(theta_i[ct]) - eta1*Math.cos(theta_t[ct]) ) / (eta2*Math.cos(theta_i[ct])+ eta1*Math.cos(theta_t[ct]) );
			}
		}



		 double dist_to_edge ; 
		 double theta_i_at_edge,  theta_t_at_edge, Ref_Coefficient_at_edge  ;
		 
		    xs = x_edge2 - TX_x ;
		    ys = y_line - TX_y;
		    dist_to_edge = Math.sqrt(xs*xs + ys*ys) ;
		    
		    perp= x_edge2 - TX_x;
		    
		    theta_i_at_edge = Math.asin(perp/dist_to_edge);
		    theta_t_at_edge = Math.asin((k0/k)*Math.sin(theta_i_at_edge));
		    Ref_Coefficient_at_edge = ((eta2*Math.cos(theta_i_at_edge))-(eta1*Math.cos(theta_t_at_edge)))/((eta2*Math.cos(theta_i_at_edge))+(eta1*Math.cos(theta_t_at_edge)));


		   
		    complex[] E_inc ; 
		    complex[] E_ref; 
		    complex[] E_d1 ; 
		    complex[] E_d2 ; 
		    complex[] E_GO ; 
		    complex[] E_D ; 
		    complex[] E_total ; 
		    E_inc = new complex[Points] ; 
		    E_ref = new complex[Points] ; 
		    E_d1 = new complex[Points] ; 
		    E_d2 = new complex[Points] ; 
		    E_GO = new complex[Points] ; 
		    E_D = new complex[Points] ; 
		    E_total = new complex[Points] ; 
		    double max_value  = 0.0 ; 
		    double incident_shadow_boundary, delta_y, delta_x, reflection_shadow_boundary   ; 
		
		    // Assume wedge tip at (0,0)!!!  
		    
		    delta_x = RX_x_distance - TX_x ; 
		    delta_y = (TX_y * delta_x)/(-TX_x) ; 
		    
		    
		    incident_shadow_boundary = TX_y - delta_y ;
	       	//System.out.println(" Shadow boundary  : "+incident_shadow_boundary);

		    reflection_shadow_boundary = RX_x_distance*TX_y/(-TX_x)  ; 
		    
		    
		
		for(ct=0 ; ct <Points; ct++){
		  
		   if(LOS[ct] == 1)
			   E_inc[ct] = new complex( SpecialFunction.jn(0,k0*s[ct]), -SpecialFunction.yn(0, k0*s[ct])) ; 
		   else
		    E_inc[ct] = new complex(0.0,0.0) ; 
		}

		
		complex temp1, temp2, temp3, temp4  ; 

		for(ct=0 ; ct <Points; ct++){
			 
		    
			if(reflection_exists[ct] == 0){
				E_ref[ct] = new complex(0.0,0.0) ; 
			}
	
			
			if(reflection_exists[ct] == 1){
	 temp1 = new complex(Ref_Coefficient[ct]*Math.sqrt(s1[ct]/(s1[ct]+s2[ct]) ),0.0) ;
	 temp2 = new complex(Math.cos(k0*s2[ct]),-Math.sin(k0*s2[ct])) ; 
	 temp3 = new complex(SpecialFunction.jn(0,k0*s1[ct]),-SpecialFunction.yn(0, k0*s1[ct])) ;
	 E_ref[ct] = temp1.mult(temp2.mult(temp3)) ; 

		    }
		    	
	//	E_ref[ct] =   new complex(1.0,0.0) ; //(besselh(0,2,k0*s1(ct))*Ref_Coefficient(ct)*exp(-j*k0*s2(ct))*sqrt(s1(ct)/(s1(ct)+s2(ct))));   %Reflected Field
		    
		    E_GO[ct]  = E_inc[ct].plus(E_ref[ct])  ;    // Total GO Field

		    incident_values  = incident_values + (20.0*Math.log10(E_inc[ct].abs())) + " \n" ;
		    reflected_values  = reflected_values + (20.0*Math.log10(E_ref[ct].abs())) + " \n" ;
			 GO_values =   GO_values + (20.0*Math.log10(E_GO[ct].abs()) ) + " \n" ;
		
			 if(E_inc[ct].abs() > max_value){
				 max_value = E_inc[ct].abs(); 
			 }
			 if(E_ref[ct].abs() > max_value){
				 max_value = E_ref[ct].abs(); 
			 }
			 if(E_GO[ct].abs() > max_value){
				 max_value = E_GO[ct].abs(); 
			 }
			 
		}
		
		
       	//System.out.println(" Getting to here : ");


		 		 
		double phi2_dash, phi_2_dash ; 
		double[] rho2 ; 
		double[]  phi2 ; 
		rho2 = new double[Points] ; 
		phi2 = new double[Points] ; 
		complex E_inc_at_edge ; 
		double x, y, L, angle , g_plus, g_minus; 
		complex fresnel_plus, fresnel_minus ;
		int N_plus, N_minus ; 
		
		// Diffraction from Edge-2. See Report

		phi2_dash = Math.atan2((TX_y - y_line),TX_x - x_edge2 );
		phi2_dash = Math.PI  - phi2_dash ; 


		phi_2_dash = Math.atan( (TX_y - y_line)/(x_edge2 - TX_x ) )  ; 


		
		xs= x_edge2 - TX_x;
		 ys= y_line - TX_y;


		   
		dist_to_edge = Math.sqrt(xs*xs + ys*ys) ; 
		E_inc_at_edge = new complex( SpecialFunction.jn(0,k0*dist_to_edge), -SpecialFunction.yn(0,k0*dist_to_edge))  ;

       	//System.out.println(" E_inc at esge: " + E_inc_at_edge.abs());

		
		complex D_i, D_s, D_r  ; 
		D_i = new complex(0.0,0.0) ; 
				
		 
		for( ct=0 ; ct < Points ; ct++ ){
		    
		
		phi2[ct] = Math.atan2((RX_points_y[ct]- y_line),(RX_points_x[ct]  - x_edge2 ));
		phi2[ct] = Math.PI - phi2[ct] ; 

		phi2[ct] = Math.PI + Math.atan(( y_line - RX_points_y[ct]) / (RX_points_x[ct]  - x_edge2 ));

		
	
		 y = RX_points_y[ct] - y_line;
		 x = RX_points_x[ct] - x_edge2;
		 rho2[ct]= Math.sqrt(y*y + x*x);
		 
		   L = (rho2[ct] * dist_to_edge)/ ( rho2[ct] + dist_to_edge ) ; 
		     
		angle = phi2[ct] - phi2_dash ; 

		
		
      // 	System.out.println(" angle: " + angle);

	    if(angle == -(n+1)*Math.PI){
	    //System.out.println("Hello!!! Chaos!") ; 
	    }
		N_plus = (int) Math.round((Math.PI + angle)/(2.0*n*Math.PI)) ;
		g_plus = 1.0 + Math.cos(angle - 2.0*n*Math.PI*N_plus); 
		//fresnel_plus = compute_fresnel(k0*rho2(ct)*g_plus);  
		fresnel_plus = compute_fresnel(k0*L*g_plus);  


		if(angle == -(n-1)*Math.PI){
		    //System.out.println("Hello!!! Chaos in minus!") ; 
		    }
		N_minus = (int) Math.round((-Math.PI + angle)/(2.0*n*Math.PI)) ;
		g_minus = 1.0 + Math.cos(angle - 2.0*n*Math.PI*N_minus); 

       	//System.out.println(" g minus: " + g_minus);
       	//System.out.println(" argument is: " + k0*L*g_minus);
       	

		//fresnel_minus = compute_fresnel(k0*rho2(ct)*g_minus) ;
		fresnel_minus = compute_fresnel(k0*L*g_minus) ;
       	
/*
if( RX_points_y[ct] > -3.76 ){
	if( RX_points_y[ct] < -3.69 ){
		
		System.out.println("CT  equals Addone for Matlab!: "  + ct) ; 
		
		System.out.println("y coordinate: "  + RX_points_y[ct]) ; 
		System.out.println("phi2 equals: "  + phi2[ct]) ; 
			System.out.println("rho2 equals: "  + rho2[ct]) ; 
			System.out.println("L equals: "  + L) ;
			System.out.println("angle equals: "  + angle) ;
			System.out.println("N plus equals: "  + N_plus) ;
			System.out.println("N minus equals: "  + N_minus) ;
			System.out.println("g plus equals: "  + g_plus) ;
			System.out.println("g minus equals: "  + g_minus) ;
				System.out.println(" fresnel: " + fresnel_plus.abs());
System.out.println(" fresnel minus: " + fresnel_minus.abs());
System.out.println(" argument for fresnel minus: " + k0*L*g_minus);

}
}
	*/	

		   
		temp1 = new complex( 1.0/Math.tan( (Math.PI + angle)/(2.0*n)  ),0.0)  ; 
		temp1 = temp1.mult(fresnel_plus) ; 
		temp2 = new complex( 1.0/Math.tan( (Math.PI - angle)/(2.0*n)  ),0.0)  ; 
		temp2 = temp2.mult(fresnel_minus) ; 
		temp1 = temp1.plus(temp2) ; 
		
		temp3 = new complex(-Math.cos(Math.PI/4.0), Math.sin(Math.PI/4.0)) ; 
		temp4 = new	complex(2.0*n*Math.sqrt(2.0*Math.PI*k0),0.0) ; 
		temp3 = temp3.div(temp4) ; 
	    D_i = temp3.mult(temp1) ; 
       //	System.out.println(" D_i: " + D_i.abs());

	    //D_values  = D_values + (D_i.abs()) + " \n" ;


		
		//    D_i = -exp(-j*pi/4.0)/(2.0*n*sqrt(2.0*pi*k0))*( cot((pi + angle)/(2*n))*fresnel_plus + cot( (pi -angle)/(2*n))*fresnel_minus)  ;   
		    
		


		angle = phi2[ct] + phi2_dash ; 
		if(angle == -(n+1)*Math.PI){
		    //System.out.println("Hello!!! Chaos!") ; 
		    }
		N_plus = (int) Math.round((Math.PI  + angle)/(2.0*n*Math.PI)) ;
		g_plus = 1.0 + Math.cos(angle - 2.0*n*Math.PI*N_plus) ;
		fresnel_plus = compute_fresnel(k0*L*g_plus) ; 

		if(angle == -(n-1)*Math.PI){
		    //System.out.println("Hello!!! Chaos in minus!") ; 
		    }
		N_minus = (int) Math.round((-Math.PI + angle)/(2.0*n*Math.PI)) ; 
		g_minus = 1.0 + Math.cos(angle - 2.0*n*Math.PI*N_minus) ;
		//fresnel_minus = compute_fresnel(k0*rho2(ct)*g_minus) ; 
		fresnel_minus = compute_fresnel(k0*L*g_minus) ; 


		
		temp1 = new complex( 1.0/Math.tan( (Math.PI + angle)/(2.0*n)  ),0.0)  ; 
		temp1 = temp1.mult(fresnel_plus) ; 
		temp2 = new complex( 1.0/Math.tan( (Math.PI - angle)/(2.0*n)  ),0.0)  ; 
		temp2 = temp2.mult(fresnel_minus) ; 
		temp1 = temp1.plus(temp2) ; 
		
		temp3 = new complex(-Math.cos(Math.PI/4.0), Math.sin(Math.PI/4.0)) ; 
		temp4 = new	complex(2.0*n*Math.sqrt(2.0*Math.PI*k0),0.0) ; 
		temp3 = temp3.div(temp4) ; 
	    D_r = temp3.mult(temp1) ; 
       	
		//D_r = -exp(-j*pi/4.0)/(2.0*n*sqrt(2.0*pi*k0))*( cot((pi + angle)/(2*n))*fresnel_plus+ cot( (pi -angle)/(2*n))*fresnel_minus ) ;


	     temp1 = new complex(Ref_Coefficient_at_edge,0.0) ;
		D_s = D_i.plus(temp1.mult(D_r)) ;   //TMZ Polarisation 

		
		//D_s = D_i  ; 
 
		temp1 = new complex(Math.cos(k0*rho2[ct]), -Math.sin(k0*rho2[ct])) ;
		temp2 = new complex(Math.sqrt(rho2[ct]),0.0) ;
		temp1 = temp1.div(temp2) ; 
		temp1 = temp1.mult(D_s) ; 
		
		E_d2[ct] = E_inc_at_edge.mult(temp1); 

		/*if( RX_points_y[ct] > -3.76 ){
			if( RX_points_y[ct] < -3.69 ){
						System.out.println("y coordinate: "  + RX_points_y[ct]) ; 

						System.out.println("phi2 equals: "  + phi2[ct]) ; 
					
					System.out.println("rho2 equals: "  + rho2[ct]) ; 
					System.out.println("L equals: "  + L) ;
					System.out.println("angle equals: "  + angle) ;
					System.out.println("N plus equals: "  + N_plus) ;
					System.out.println("N minus equals: "  + N_minus) ;
					System.out.println("g plus equals: "  + g_plus) ;
					System.out.println("g minus equals: "  + g_minus) ;
						System.out.println(" fresnel: " + fresnel_plus.abs());
		System.out.println(" fresnel minus: " + fresnel_minus.abs());
		System.out.println(" argument for fresnel minus: " + k0*L*g_minus);

		System.out.println(" Real D_i:  " + D_i.re());
		System.out.println(" Imaginary D_i:  " + D_i.im());
		System.out.println(" Real D_r:  " + D_r.re());
		System.out.println(" Imaginary D_r:  " + D_r.im());
			
			System.out.println(" Real D_s:  " + D_s.re());
			System.out.println(" Imaginary D_s:  " + D_s.im());
			System.out.println(" Real E_d2:  " + E_d2[ct].re());
			System.out.println(" Imaginary E_d2:  " + E_d2[ct].im());
			System.out.println(" Real E_inc_at edge: " +E_inc_at_edge.re());
			System.out.println(" Imaginary E_inc_at edge:  " +E_inc_at_edge.im());
			}} */
		
		E_total[ct] = E_GO[ct].plus( E_d2[ct]) ; 
		

	    D_values  = D_values + (20.0*Math.log10(E_d2[ct].abs()) ) + " \n" ;
	    total_values  = total_values + (20.0*Math.log10(E_total[ct].abs())) + " \n" ;
		

		//E_d2[ct] = E_inc_at_edge*(exp(-j*k0*rho2(ct))/sqrt(rho2(ct)))*D_s ;   //Total Diffracted Field From Edge-2

	    
		} 

		/*for(ct=1:Points+1)
		    E_D(ct)=E_d2(ct);          %Total Diffracted field(Edge-1 + Edge-2)
		    E_total(ct)=E_GO(ct)+E_D(ct);       %Total Field(GO+Diffracted)
		end


*/
		
max_value = -1000000.0 ; 
	for(ct = 0 ; ct < Points ; ct++) { 
		
		if(20.0*Math.log10(E_inc[ct].abs()) > max_value){
	
			 max_value = 20.0*Math.log10(E_inc[ct].abs()); 
		 }
		 if(20.0*Math.log10(E_ref[ct].abs()) > max_value){
			 max_value = 20.0*Math.log10(E_ref[ct].abs()); 
		 }
		 if(20.0*Math.log10(E_GO[ct].abs()) > max_value){
			 max_value = 20.0*Math.log10(E_GO[ct].abs()); 
		 }
		 if(20.0*Math.log10(E_total[ct].abs()) > max_value){
			 max_value = 20.0*Math.log10(E_total[ct].abs()); 
		 }
		 if(20.0*Math.log10(E_d2[ct].abs()) > max_value){
			 max_value = 20.0*Math.log10(E_d2[ct].abs()); 
		 }
		 
	}
	
	double min_value = 10000.0 ; 
	
for(ct = 0 ; ct < Points ; ct++) { 
		

if(E_inc[ct].abs()  > 0.0 ){
if(20.0*Math.log10(E_inc[ct].abs()) < min_value){
	
			 min_value = 20.0*Math.log10(E_inc[ct].abs()); 
		 }
}

if(E_ref[ct].abs()  > 0.0 ){
		 if(20.0*Math.log10(E_ref[ct].abs()) < min_value){
			 min_value = 20.0*Math.log10(E_ref[ct].abs()); 
		 }
}

if(E_GO[ct].abs()  > 0.0 ){
		 if(20.0*Math.log10(E_GO[ct].abs()) < min_value){
			 min_value = 20.0*Math.log10(E_GO[ct].abs()); 
		 }}

if(E_total[ct].abs()  > 0.0 ){
		 if(20.0*Math.log10(E_total[ct].abs()) < min_value){
			 min_value = 20.0*Math.log10(E_total[ct].abs()); 
		 }}

if(E_d2[ct].abs()  > 0.0 ){
		 if(20.0*Math.log10(E_d2[ct].abs()) < min_value){
			 min_value = 20.0*Math.log10(E_d2[ct].abs()); 
		 }
}
	}
	

	/*System.out.println(" min_value " + min_value );
	System.out.println(" max_value " + max_value );
	
System.out.println(" Writing to string! " );
*/

		
		fields_along_line = Points  + " " + min_value + " " + max_value + " ";
		fields_along_line = fields_along_line + incident_shadow_boundary + " " + reflection_shadow_boundary + " "; 

		 fields_along_line = fields_along_line + field_point_values ;
		 fields_along_line = fields_along_line + incident_values ;
		 fields_along_line = fields_along_line + reflected_values ;
		 fields_along_line = fields_along_line + GO_values ;
		 fields_along_line = fields_along_line + D_values ; 
		 fields_along_line = fields_along_line + total_values ; 
		 

		return(fields_along_line); 
		
		
	}
	
	}



