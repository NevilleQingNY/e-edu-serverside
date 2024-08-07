package com.example.ESOA.service;



public class two_ray_set_up_problem {
	public	double		f,  Gamma, T, omega, lambda_0,  eta_0, eta_1, k_0, k_1, f_in_MHz, epsilon_r, epsilon, mu_1 ;
	public	double		t_start, t_end,  t_step, z_start, z_end, z_step, mu, mu_r  , sigma, delta_theta, lambda_d, skin_depth, transmitter_height, receiver_height  ;
	public	int			N, Nt, tutorial_choice, No_of_angles ; 
	public 	complex 	prop_temp1, prop_temp2 , prop_const_material, prop_const_free_space, eta_free_space, eta_material ; 
	public 	complex 	reflection_coefficient, transmission_coefficient, slab_reflection_coefficient, phase_value ; 
	public  String 		the_polarisation, the_scenario ;
	
	public	void read_in_variables( String frequency_value,  String TX_height, String RX_height){
		
		f_in_MHz		=	Double.parseDouble(frequency_value);
		//epsilon_r		=	Double.parseDouble(epsilon_value);
		//sigma			=	Double.parseDouble(conductivity_value) ; 
		transmitter_height = Double.parseDouble(TX_height) ; 
		receiver_height = Double.parseDouble(RX_height) ; 
		//the_polarisation  =  polarisation ;
		
		/*System.out.println("f is "+f_in_MHz) ;
		System.out.println("epsilon is "+epsilon_r) ;
		System.out.println("sigma "+sigma) ;
		System.out.println("the_polarisation "+the_polarisation) ;
		System.out.println("transmitter_hieght is "+transmitter_height) ;
		System.out.println("receiver_hieght is "+receiver_height) ;
		*/
		
       	epsilon = global_definitions.epsilon_0*epsilon_r ; 
       	}

	public	void initialise_variables(){
		
		complex  complex_unity, denominator ; 
		f	=	f_in_MHz*global_definitions.MHz ; 
		omega = 2.0*global_definitions.pi* f ;
		k_0 = omega*Math.sqrt(global_definitions.mu_0*global_definitions.epsilon_0) ; 
		k_1 = omega*Math.sqrt(mu_1*epsilon)	 ;	
		mu_r = 1.0 ;
		mu = global_definitions.mu_0*mu_r ; 


		prop_temp1  =  new complex(0.0,omega*mu) ;
		prop_temp2 = new complex(sigma,omega*epsilon) ; 
		prop_const_material = prop_temp1.mult(prop_temp2) ; 
		prop_const_material = prop_const_material.sqrt() ; 


		prop_temp1  =  new complex(0.0,omega*global_definitions.mu_0) ;
		prop_temp2 = new complex(0.0,omega*global_definitions.epsilon_0) ; 
		prop_const_free_space = prop_temp1.mult(prop_temp2) ; 
		prop_const_free_space = prop_const_free_space.sqrt() ; 

		eta_free_space = new complex(Math.sqrt(global_definitions.mu_0/global_definitions.epsilon_0),0.0) ;
		eta_material = new complex(0.0,omega*mu).div(new complex(sigma,omega*epsilon)) ;
		eta_material = eta_material.sqrt() ; 
		
		reflection_coefficient =  eta_material.minus(eta_free_space) ; 
		reflection_coefficient =  reflection_coefficient.div(eta_material.plus(eta_free_space)) ; 

		//System.out.println("prop_const equals"+prop_const_material.re()+" "+prop_const_material.im()); 

		phase_value = prop_const_material.mult(2.0) ;
		//System.out.println("phase_value equals"+phase_value.re()+" "+phase_value.im()); 
 
//		System.out.println("phase_value equals"+phase_value.re()+" "+phase_value.im()); 

		phase_value = phase_value.exp(); 

	//	System.out.println("phase_value equals"+phase_value.re()+" "+phase_value.im()); 

		slab_reflection_coefficient = reflection_coefficient.mult(phase_value) ;
		slab_reflection_coefficient = reflection_coefficient.minus(slab_reflection_coefficient) ;
		
		complex_unity = new complex(1.0,0.0) ; 
		denominator = complex_unity.minus(reflection_coefficient.mult(reflection_coefficient.mult(phase_value))) ; 
		slab_reflection_coefficient = slab_reflection_coefficient.div(denominator) ; 
		
		transmission_coefficient = reflection_coefficient.plus(new complex(1.0,0.0)) ; 
      /* 	System.out.println("Reflection Coefficient : " + reflection_coefficient.re() + " " + reflection_coefficient.im());
       	System.out.println("Transmission Coefficient : " + transmission_coefficient.re() + " " + transmission_coefficient.im());
       	System.out.println("prop_const free space : " + prop_const_free_space.re() + " " + prop_const_free_space.im());
       	System.out.println("prop_const material : " + prop_const_material.re() + " " + prop_const_material.im());
       	System.out.println("Slab reflection coefficient : " + slab_reflection_coefficient.re() + " " + slab_reflection_coefficient.im());
*/

		lambda_0 = 2*global_definitions.pi/k_0  ;
		lambda_d = 2.0*global_definitions.pi/ prop_const_material.im(); 
		skin_depth = 1.0/prop_const_material.re() ;
		
				
		t_start = 0.0 ; 
		t_end  =  1.0/f ; 
		Nt = 15 ; 
		t_step  = (t_end - t_start)/(double)Nt ; 
		
		
		N = (int) Math.ceil(30.0*6.0/lambda_d) ;
 
		// Should make N a multiple of 200 to make nice plot (Otherwise get a noticeable jump as we cross boundary). 
		while( N % 200 != 0) 
		{N++;} 
		
		
		No_of_angles = 180 ; 
		delta_theta = global_definitions.pi/(2.0*No_of_angles) ;
		if( tutorial_choice == 1) {
		z_start = 0.0; 
		z_end = 6.0 ;}
		
		if(tutorial_choice == 2) { 
		z_start = -3.0; 
		z_end = 3.0 ;}

		if(tutorial_choice == 3) { 
			z_start = -3.0; 
			z_end = 0.0 ;}

	//	System.out.println("z_start : " + z_start);

		z_step = (z_end - z_start)/N ;

	}
	
 	
// This code used for the propagation and reflection tutorials
public String two_ray_analysis(){ 
		String two_ray_answer = new String(); 
		String two_ray_loss_values = new String(); 
		String free_space_loss_values = new String(); 
		String r_4_loss_values = new String(); 
		String distance_values = new String(); 
		
		
		double delta_x, ref_const ; 
		int N, counter ; 
		Point3d TX, Image_TX, RX_start, field_point , the_vec; 
		double[] two_ray_loss , free_space_loss, r_4_loss ; 
		double R1, R2 , term ; 
		complex[] free_space_E ; 
		complex[] two_ray_E ; 
		
		field_point = new Point3d() ; 
		
		double break_distance = 4.0*transmitter_height*receiver_height/lambda_0  ; 
		double break_distance_log = Math.log10(break_distance) ;
				
		double distance = break_distance*40.0 ;

		
		N = 4*(int)(distance/lambda_0) ; 
		
		delta_x = distance/N ; 
		TX = new Point3d(0.0,transmitter_height,0.0) ;
		Image_TX = new Point3d(0.0,-transmitter_height,0.0) ;
		RX_start = new Point3d(1.0,receiver_height,0.0) ;
		complex  phase_term ; 
		
		phase_term = new complex(0.0,0.0) ; 

					ref_const = -1.0 ; 

					two_ray_loss = new double[N] ; 
					r_4_loss = new double[N] ; 
					free_space_loss = new double[N] ;
				free_space_E = new complex[N] ; 
				two_ray_E = new complex[N] ; 
				
					for( counter=0; counter<N; counter++){ 

					field_point.setequal(RX_start.x() + (counter+0.5)*delta_x,receiver_height,0.0)  ;
					the_vec = field_point.minus(TX) ;
					R1 = the_vec.abs() ; 
					the_vec = field_point.minus(Image_TX) ;
					R2 = the_vec.abs() ; 
					
					term = (eta_free_space.re()*2.0)/(4.0*global_definitions.pi*R1*R1*R1*R1) ; 
 				    r_4_loss[counter] = 10.0*Math.log10( 1.0/(R1*R1*R1*R1)) ; 
 				    
 				    if(counter == 0 ){
 		/*		    	System.out.println("eta_free_Space equals "+eta_free_space.re()) ; 
 				   	System.out.println("r_4_loss equals "+r_4_loss[counter]) ; 
 					System.out.println("R1 equals "+R1) ; 
 		*/		    
 				    }
 				    
 				    phase_term.setreal(0.0) ;
				    phase_term.setimag(-k_0*R1) ;
				    phase_term = phase_term.exp(); 
				    free_space_E[counter] = phase_term.mult(new complex(1.0/R1,0.0)) ; 
				    free_space_loss[counter] = 10.0*Math.log10( Math.pow(free_space_E[counter].abs(),2.0) ) ; 
				    
				    two_ray_E[counter] 	= free_space_E[counter] ; 
				    phase_term.setreal(0.0) ;
				    phase_term.setimag(-k_0*R2) ;
				    phase_term = phase_term.exp(); 
				    two_ray_E[counter] = two_ray_E[counter].minus(phase_term.mult(new complex(1.0/R2,0.0))) ; 
				    
				    
				    two_ray_loss[counter] =  10.0*Math.log10(Math.pow(two_ray_E[counter].abs(),2.0)) ; 
				    
				    	 
 				    r_4_loss_values = r_4_loss_values + r_4_loss[counter] + " \n" ; 
 			 		//System.out.println("r_4 equals "+r_4_loss[counter]) ;
 			 		

 				    two_ray_loss_values = two_ray_loss_values + two_ray_loss[counter] + " \n" ; 
 				   //System.out.println("two ray loss  equals "+two_ray_loss[counter]) ;
			 		free_space_loss_values = free_space_loss_values + free_space_loss[counter] + " \n" ; 
			 		//System.out.println("free space loss  equals "+free_space_loss[counter]) ;
			 		
 				    distance_values = distance_values + Math.log10((field_point.minus(TX)).abs() ) + " \n" ; 
 				    
 					//System.out.println(" distance "+ Math.log10((field_point.minus(RX_start)).abs() )) ;

				    
					}
/*					
					
					distance(counter,1) = field_point_x - RX_start_x ; 
					two_ray_loss(counter,1) = 10.0*log10(abs(e_received*e_received)/(2.0*impedance)) ; 

*/
 
					
two_ray_answer = 	 N + " " + break_distance + " " + break_distance_log + " " + distance_values + free_space_loss_values + two_ray_loss_values + r_4_loss_values ; 

	return(two_ray_answer) ;
		

		}
		
}
