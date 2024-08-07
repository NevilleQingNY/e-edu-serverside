package com.example.ESOA.service ; 


public class set_up_problem {
	public	double		f,  Gamma, T, omega, lambda_0,  eta_0, eta_1, k_0, k_1, f_in_MHz, epsilon_r, epsilon, mu_1 ;
	public	double		t_start, t_end,  t_step, z_start, z_end, z_step, mu, mu_r  , sigma, delta_theta, lambda_d, skin_depth, width  ;
	public	int			N, Nt, tutorial_choice, No_of_angles ; 
	public 	complex 	prop_temp1, prop_temp2 , prop_const_material, prop_const_free_space, eta_free_space, eta_material ; 
	public 	complex 	reflection_coefficient, transmission_coefficient, slab_reflection_coefficient, phase_value ; 
	public  String 		the_polarisation, the_scenario ;
	public 	complex		gamma_12, gamma_23, gamma_21, T_12, T_23, T_21  ;
	public	complex		region2_term1, region2_term2 ; 
	public complex		region3_term  ; 
	
	
	
	
	public	void read_in_variables(String conductivity_value, String epsilon_value, String frequency_value, String polarisation, String scenario, String width_value, String tutorial_value){
		
		f_in_MHz		=	Double.parseDouble(frequency_value);
		epsilon_r		=	Double.parseDouble(epsilon_value);
		tutorial_choice =	Integer.parseInt(tutorial_value) ;
		sigma			=	Double.parseDouble(conductivity_value) ; 
		the_polarisation  =  polarisation ;
		the_scenario 	= scenario ; 
		width			=	Double.parseDouble(width_value) ; 
		
       /*	System.out.println("f in MHz : " + f_in_MHz);
       	System.out.println("Epsilon Value : " + epsilon_r);
       	System.out.println("Conductivity Value : " + sigma);
       	System.out.println("Polarisation : " + the_polarisation);
       	
       	System.out.println("Scenario  : " + the_scenario);
       	System.out.println("Tutorial choice is is is : " + tutorial_choice);
       	*/
		
       	epsilon = global_definitions.epsilon_0*epsilon_r ; }

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

		phase_value = phase_value.mult(-1.0*width) ;
		
		

		//System.out.println("width equals "+width); 
 
		//System.out.println("phase_value equals"+phase_value.re()+" "+phase_value.im()); 

		phase_value = phase_value.exp(); 

		//System.out.println("phase_value equals"+phase_value.re()+" "+phase_value.im()); 

		slab_reflection_coefficient = reflection_coefficient.mult(phase_value) ;
		slab_reflection_coefficient = reflection_coefficient.minus(slab_reflection_coefficient) ;
		
		complex_unity = new complex(1.0,0.0) ; 
		denominator = complex_unity.minus(reflection_coefficient.mult(reflection_coefficient.mult(phase_value))) ; 
		slab_reflection_coefficient = slab_reflection_coefficient.div(denominator) ; 
		
		transmission_coefficient = reflection_coefficient.plus(new complex(1.0,0.0)) ; 
		
		gamma_12 = reflection_coefficient ;
		gamma_23 = reflection_coefficient.mult(new complex(-1.0,0.0)) ; 
		gamma_21 = gamma_23 ; 
		
		T_12 =  complex_unity.plus(gamma_12) ; 
		T_23 =  complex_unity.plus(gamma_23) ; 
		T_21 =  T_23 ; 
		
		denominator = complex_unity.minus(gamma_23.mult(gamma_21).mult(phase_value)) ; 
		
		region2_term1  = T_12.div(denominator)  ; 
		
		region2_term2 = T_12.mult(gamma_23.mult(phase_value)) ;
		region2_term2 = region2_term2.div(denominator) ; 
		
		// Need to half phase value ; 
		phase_value = prop_const_material.mult(1.0) ;
		phase_value = phase_value.mult(-1.0*width) ;
		phase_value = phase_value.exp(); 

		region3_term = T_12.mult(T_23.mult(phase_value)) ; 
		region3_term   = region3_term.div(denominator) ; 
		
		
		
       	/*System.out.println("Reflection Coefficient : " + reflection_coefficient.re() + " " + reflection_coefficient.im());
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
			z_end = 3.0 ;}

	//	System.out.println("z_start : " + z_start);

		z_step = (z_end - z_start)/N ;

	}
	
 	
// This code used for the propagation and reflection tutorials
public String propagation_and_reflection_make_frames(){
		
		int		countert, counterz ; 
		double	t, time_lower ; 
		double[]	z ; 
		double[] 	region1_z ;
		double[] 	region2_z ;
		double[] 	region3_z ;
		
		
		double[][]  e_field ; 
		double[][]  inc_field ; 
		double[][]  ref_field ; 
		double[][]  trans_field ; 
		double[][]  region1_field ; 
		double[][]  region2_field ; 
		double[][]  region3_field ; 
		
		complex[]	complex_e_field ;
		complex[]	complex_inc_field ;
		complex[]	complex_ref_field ;
		complex[]	complex_trans_field ;
		complex[] 	complex_region1_field ; 
		complex[] 	complex_region2_field ; 
		complex[] 	complex_region3_field ; 
		
		
		complex		temp1, temp2, temp3, temp4, temp5 ; 
		String field_values = new String();
		String inc_values = new String();
		String ref_values = new String();
		String trans_values = new String();
		String region1_values = new String();
		String region2_values = new String();
		String region3_values = new String();
			
		
		String z_values = new String();
		String region1_z_values = new String();
		String region2_z_values = new String();
		String region3_z_values = new String();
		
		String reflection_answer = new String() ; 
		double	max_value = -100000.0 ;
		double min_value = 1000000.0 ; 

		z =			new double[N] ;
		region1_z =			new double[N] ;
		region2_z =			new double[N] ;
		region3_z =			new double[N] ;
		
		
		complex_e_field = new complex[N] ; 
		complex_inc_field = new complex[N] ; 
		complex_ref_field = new complex[N] ; 
		complex_trans_field = new complex[N] ; 
		
		complex_region1_field = new complex[N] ; 
		complex_region2_field = new complex[N] ; 
		complex_region3_field = new complex[N] ; 
		
		
		e_field =	new double[N][Nt] ;
		inc_field =	new double[N][Nt] ;
		ref_field =	new double[N][Nt] ;
		trans_field =	new double[N][Nt] ;
		
		region1_field =	new double[N][Nt] ;
		region2_field =	new double[N][Nt] ;
		region3_field =	new double[N][Nt] ;
		
		
		int region1_z_counter = 0  ; 
		int region2_z_counter = 0  ; 
		int region3_z_counter = 0  ; 
		
	
		for(counterz = 0; counterz < N ; counterz++ ){ 
			z[counterz]  =   z_start + (counterz)*z_step ;
			z_values  = z_values + (z[counterz]) + " " ;
			complex_e_field[counterz] = new complex(0.0,0.0) ;
			complex_inc_field[counterz] = new complex(0.0,0.0) ;
			complex_ref_field[counterz] = new complex(0.0,0.0) ;
			complex_trans_field[counterz] = new complex(0.0,0.0) ;
			complex_region1_field[counterz] = new complex(0.0,0.0) ; 
			complex_region2_field[counterz] = new complex(0.0,0.0) ; 
			complex_region3_field[counterz] = new complex(0.0,0.0) ; 
			
			
			
			if(tutorial_choice ==1){
		//System.out.println("Hello - Doing propagation! ");

			temp1 = prop_const_material.mult(-z[counterz]) ; 
			complex_e_field[counterz]  = temp1.exp()  ;}
			
			if(tutorial_choice == 2){
				

				if( z[counterz] < 0.0 ){
					temp1 = prop_const_free_space.mult(-z[counterz]) ; 
					temp1 = temp1.exp() ; 
					complex_inc_field[counterz] = temp1 ; 
					temp2 = prop_const_free_space.mult(z[counterz]) ; 
					temp2 = temp2.exp() ; 
					temp2 = temp2.mult(reflection_coefficient) ; 
					complex_ref_field[counterz] = temp2 ; 
					complex_e_field[counterz] = temp1.plus(temp2) ;
					}
				else{
					temp3 = prop_const_material.mult(-z[counterz]) ; 
					temp3 = temp3.exp() ; 
					temp3 = temp3.mult(transmission_coefficient) ; 
					complex_e_field[counterz] = temp3 ; 
					complex_trans_field[counterz] = temp3 ; 
				}

				
			}
			
				if(tutorial_choice == 3){
				
					
					
					
					if(z[counterz] < 0.0){
						region1_z[region1_z_counter] = z[counterz] ;
						region1_z_values  = region1_z_values + (z[counterz]) + " " ;

						region1_z_counter ++  ; 
						
						
						
					temp1 = prop_const_free_space.mult(-z[counterz]) ; 
					temp1 = temp1.exp() ; 
					complex_inc_field[counterz] = temp1 ; 
					temp2 = prop_const_free_space.mult(z[counterz]) ; 
					temp2 = temp2.exp() ; 
					temp2 = temp2.mult(slab_reflection_coefficient) ; 
					complex_ref_field[counterz] = temp2 ; 
					complex_region1_field[counterz] = temp1.plus(temp2) ;
					}
					
					if( (z[counterz] >= 0.0)&(z[counterz]<width)){
						region2_z[region2_z_counter] = z[counterz] ;
						region2_z_values  = region2_z_values + (z[counterz]) + " " ;
						region2_z_counter ++  ; 
						
						temp1 = prop_const_material.mult(-z[counterz]) ; 
						temp1 = temp1.exp() ; 
						temp2 = prop_const_material.mult(z[counterz]) ; 
						temp2 = temp2.exp() ; 
			
						complex_region2_field[counterz] = (temp1.mult(region2_term1)).plus(temp2.mult(region2_term2)) ;
						//System.out.println("region2 equals " + complex_region2_field[counterz].re()  ) ; 

						
						
						}
					if( (z[counterz] >= width) ){
						region3_z[region3_z_counter] = z[counterz] ;
						region3_z_values  = region3_z_values + (z[counterz]) + " " ;

						region3_z_counter ++  ; 
						temp1 = prop_const_free_space.mult(-(z[counterz]-width)) ; 
						temp1 = temp1.exp() ;
						complex_region3_field[counterz] = temp1.mult(region3_term) ; 
						}
					
				}
				
		}
		
		time_lower = 0.0 ; 
		for( countert = 0; countert < Nt; countert ++){ 
			t = time_lower + countert * t_step  ; 
		
			for(counterz = 0; counterz < N ; counterz++  ){ 
				
				temp1 = new complex(0.0,omega*t); 
				temp1 = temp1.exp() ; 
				
				if( (tutorial_choice == 1)||(tutorial_choice==2)) { 
					temp2 = temp1.mult(complex_e_field[counterz]); 
				
				temp3 = temp1.mult(complex_inc_field[counterz]); 
				temp4 = temp1.mult(complex_ref_field[counterz]); 
				temp5 = temp1.mult(complex_trans_field[counterz]); 
				
				e_field[counterz][countert] =  temp2.re(); 
				inc_field[counterz][countert] =  temp3.re(); 
				ref_field[counterz][countert] =  temp4.re(); 
				trans_field[counterz][countert] =  temp5.re(); 
				}
				if( (tutorial_choice == 3)) { 
					temp2 = temp1.mult(complex_region1_field[counterz]); 
				
				temp3 = temp1.mult(complex_region2_field[counterz]); 
				temp4 = temp1.mult(complex_region3_field[counterz]); 
				
					region1_field[counterz][countert] =  temp2.re();
				
				
					region2_field[counterz][countert] =  temp3.re(); 
				
				
					region3_field[counterz][countert] =  temp4.re(); 
				}
				
				if(tutorial_choice == 1) {
					if( e_field[counterz][countert] > max_value)
					max_value = e_field[counterz][countert] ;
				if(e_field[counterz][countert] < min_value)
					min_value = e_field[counterz][countert] ;
				}
				
				if(tutorial_choice == 2) {
					if( e_field[counterz][countert] > max_value)
					max_value = e_field[counterz][countert] ;
				if(e_field[counterz][countert] < min_value)
					min_value = e_field[counterz][countert] ;
				
				if( inc_field[counterz][countert] > max_value)
					max_value = inc_field[counterz][countert] ;
				if(inc_field[counterz][countert] < min_value)
					min_value = inc_field[counterz][countert] ;
				
				if( ref_field[counterz][countert] > max_value)
					max_value = ref_field[counterz][countert] ;
				if(ref_field[counterz][countert] < min_value)
					min_value = ref_field[counterz][countert] ;
				
				if( trans_field[counterz][countert] > max_value)
					max_value = trans_field[counterz][countert] ;
				if(trans_field[counterz][countert] < min_value)
					min_value = trans_field[counterz][countert] ;
				
				}
				
				if(tutorial_choice == 3) {
					if( region1_field[counterz][countert] > max_value)
					max_value = region1_field[counterz][countert] ;
				if(region1_field[counterz][countert] < min_value)
					min_value = region1_field[counterz][countert] ;
				
				if( region2_field[counterz][countert] > max_value)
					max_value = region2_field[counterz][countert] ;
				if(region2_field[counterz][countert] < min_value)
					min_value = region2_field[counterz][countert] ;
				
				if( region3_field[counterz][countert] > max_value)
					max_value = region3_field[counterz][countert] ;
				if(region3_field[counterz][countert] < min_value)
					min_value = region3_field[counterz][countert] ;
				
				}
				
				field_values  = field_values + (e_field[counterz][countert]) + " " ;
				inc_values  = inc_values + (inc_field[counterz][countert]) + " " ;
				ref_values  = ref_values + (ref_field[counterz][countert]) + " " ;
				trans_values  = trans_values + (trans_field[counterz][countert]) + " " ;
				//if( z[counterz] <0.0){
				region1_values = region1_values + (region1_field[counterz][countert]) + " " ; 
				//}
				
				//if( (z[counterz] >= 0.0)&(z[counterz]<width) ) {
					region2_values = region2_values + (region2_field[counterz][countert]) + " " ;
				//}
				
				//if( z[counterz] >= width ){
				region3_values = region3_values + (region3_field[counterz][countert]) + " " ;
				//}
				
		}

		}
		
		if( tutorial_choice == 1){
			reflection_answer = min_value+" " + max_value+  " " + N +  " " +Nt + " " + prop_const_material.re() + " " + prop_const_material.im() + " " + lambda_d + " " + skin_depth + " " + eta_material.re() + " " + eta_material.im() +" "+ z_values+field_values;	}		
		if( tutorial_choice == 2){
			reflection_answer = min_value+" " + max_value+ " " + N + " " + Nt +  " " + lambda_d + " " +eta_material.re() +  " " +eta_material.im() + " " + reflection_coefficient.re() + " " + reflection_coefficient.im() + " " + transmission_coefficient.re() + " " + transmission_coefficient.im() + " " + z_values+field_values+inc_values+ref_values+trans_values ;	}
	if( tutorial_choice == 3){
				reflection_answer = min_value+" " + max_value+ " " + N + " " + Nt +  " " + lambda_d + " " +eta_material.re() +  " " +eta_material.im() + " " + slab_reflection_coefficient.re() + " " + slab_reflection_coefficient.im() + " " + transmission_coefficient.re() + " " + transmission_coefficient.im() + " "  ; 
				//reflection_answer = reflection_answer + region1_z_counter + " " +  region2_z_counter + " " + region3_z_counter + " " ; 
				//reflection_answer= reflection_answer + region1_z_values + region2_z_values + region3_z_values  ; 
				reflection_answer= reflection_answer + z_values ;
				reflection_answer  = reflection_answer + region1_values + region2_values + region3_values+trans_values ;	}
			
	
return(reflection_answer) ;
		

		}
		
	
	

	// This code used for the propagation and reflection tutorials
	public String oblique(){
			
			int	 	counter_angle ; 
			double[]	theta_i ; 
			complex[] 	theta_t  ; 
			complex[]  reflection_coefficient ; 
			complex[]	transmission_coefficient ; 
			complex[] 	prop_const ; 
			complex[]	eta ; 
			complex 	numerator, denominator, temp, temp2, temp3, complex_unity, complex_i ; 
			String 	reflection_answer = new String() ; 
			String	theta_i_values = new String(); 
			String	theta_t_values = new String(); 
			
			String 	perp_ref_coeff_values = new String()  ; 	
			String 	perp_trans_coeff_values = new String()  ; 
			String 	par_ref_coeff_values = new String()  ; 	
			String 	par_trans_coeff_values = new String()  ; 
			
			theta_i =			new double[No_of_angles] ; 
			theta_t = 		new complex[No_of_angles] ; 
			reflection_coefficient = new complex[No_of_angles] ; 
			transmission_coefficient = new complex[No_of_angles] ; 
			prop_const = 	new complex[2] ; 
			eta =			new complex[2] ; 
			
for(counter_angle = 0 ; counter_angle < No_of_angles ; counter_angle++){
	
	if( the_scenario.equals("vacuum") == true){
	prop_const[0] = prop_const_free_space ;
	prop_const[1] = prop_const_material ;
	eta[0] = eta_free_space ; 
	eta[1] = eta_material ; 
	}
	
	if( the_scenario.equals("material") == true){
		prop_const[1] = prop_const_free_space ;
		prop_const[0] = prop_const_material ;
		eta[1] = eta_free_space ; 
		eta[0] = eta_material ; 
		}
	
	theta_i[counter_angle] =  delta_theta*(counter_angle+0.5) ; 
	theta_i_values  = theta_i_values + (theta_i[counter_angle]*180.0/global_definitions.pi) + " \n" ;

	temp = prop_const[0].div(prop_const[1]) ; 
	temp2 = temp.mult(Math.sin(theta_i[counter_angle])) ; 
	
	complex_unity = new complex(1.0,0.0) ; 
	complex_i  = new complex(0.0,1.0) ; 
	// Need to get sin inverse of temp2 
	temp3 = complex_unity.minus(temp2.mult(temp2)) ; 
	temp3 = temp3.sqrt(); 
	//temp3 = temp3.mult(complex_i) ; 
	temp3 = (temp2.mult(complex_i)).plus(temp3) ;
	temp3 = temp3.ln(); 
	temp3 = temp3.mult(complex_i) ;
	temp3 = temp3.mult(-1.0) ;
	
	theta_t[counter_angle] = temp3; 
	theta_t_values  = theta_t_values + (theta_t[counter_angle].re()*180.0/global_definitions.pi) + " \n" ;
	
	//System.out.println("argument equals " + temp2.re() +" j" +temp2.im() ) ; 
	//System.out.println("theta_t equals " + theta_t[counter_angle].re()+ " j"+theta_t[counter_angle].im()) ; 

	numerator 	=  eta[1].mult(Math.cos(theta_i[counter_angle])) ; 
	numerator  	= numerator.minus(eta[0].mult(  (theta_t[counter_angle]).cos()    )) ;
	denominator = eta[1].mult(Math.cos(theta_i[counter_angle])) ;
	denominator = denominator.plus(eta[0].mult(  (theta_t[counter_angle]).cos()   )) ; 
	reflection_coefficient[counter_angle] = numerator.div(denominator) ; 
	//System.out.println("reflection_coefficient equals " + (reflection_coefficient[counter_angle]).abs()) ; 
	perp_ref_coeff_values = perp_ref_coeff_values + (reflection_coefficient[counter_angle].abs()) + " \n" ; 
	
	numerator = eta[1].mult(2.0*Math.cos(theta_i[counter_angle])); 
	denominator = eta[1].mult(Math.cos(theta_i[counter_angle])) ; 
	denominator = denominator.plus(eta[0].mult(  (theta_t[counter_angle]).cos()   )) ;
	transmission_coefficient[counter_angle] = numerator.div(denominator) ;
//System.out.println("transmission_coefficient equals " + (transmission_coefficient[counter_angle]).abs()) ; 
	
	perp_trans_coeff_values = perp_trans_coeff_values + (transmission_coefficient[counter_angle].abs()) + " \n" ; 
	
	numerator = eta[0].mult(-1.0*Math.cos(theta_i[counter_angle])) ; 
	numerator  = numerator.plus(eta[1].mult( (theta_t[counter_angle]).cos()   )) ; 
	denominator = eta[0].mult(Math.cos(theta_i[counter_angle])) ; 
	denominator  = denominator.plus(eta[1].mult(  (theta_t[counter_angle]).cos()   )) ; 
	reflection_coefficient[counter_angle] = numerator.div(denominator); 
	par_ref_coeff_values = par_ref_coeff_values + (reflection_coefficient[counter_angle].abs()) + " \n" ; 
	
	numerator = eta[1].mult(2.0*Math.cos(theta_i[counter_angle])) ; 
	transmission_coefficient[counter_angle] = numerator.div(denominator) ; 
	par_trans_coeff_values = par_trans_coeff_values + (transmission_coefficient[counter_angle].abs()) + " \n" ; 
	
	
}
				
	reflection_answer = No_of_angles + " " + theta_i_values + theta_t_values ; 
	
	if( the_polarisation.equals("perpendicular") == true){
		reflection_answer = reflection_answer+perp_ref_coeff_values + perp_trans_coeff_values ; 
	}
	else{
		reflection_answer = reflection_answer+par_ref_coeff_values + par_trans_coeff_values ; 
	}
		
	System.out.println(" Reflection answer is: " + reflection_answer) ; 	

	return(reflection_answer) ;

}
}
