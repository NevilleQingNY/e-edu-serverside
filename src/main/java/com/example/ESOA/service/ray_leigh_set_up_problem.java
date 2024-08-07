package com.example.ESOA.service;

//
//  set_up_problem.java
//  Rayleigh
//
//  Created by Conor Brennan on 07/09/2011.
//  Copyright 2011 Dublin City University. All rights reserved.
//


import java.util.Random;

 

public class ray_leigh_set_up_problem {
	public  complex 	sample_value, rician_sample_value ; 
	public  double		sigma, LOS_power_in_dBm, LOS_power_in_linear, LOS_amplitude ;
	public double	NLOS_power_in_dBm, NLOS_power_in_linear, NLOS_amplitude  ; 
	public 	int			no_of_bins ;
	public int		no_of_scatterers ; 
	public int		no_of_samples ; 
	public double	alpha ; 

	
	
	public	void read_in_variables(String no_of_scatterers_value, String no_of_samples_value, String LOS_power_in_dBm_value, String NLOS_power_in_dBm_value) {
		
		//sigma 			= 	Double.parseDouble(sigma_value) ; 
		no_of_scatterers 	= 	Integer.parseInt(no_of_scatterers_value) ; 
		no_of_samples 	= 	Integer.parseInt(no_of_samples_value) ; 
		LOS_power_in_dBm 	= 	Double.parseDouble(LOS_power_in_dBm_value) ; 
		NLOS_power_in_dBm 	= 	Double.parseDouble(NLOS_power_in_dBm_value) ; 
			
//    	//System.out.println("LOS_power_in_dB : " + LOS_power_in_dB);
    	   	
		
       /*	//System.out.println("f in MHz : " + f_in_MHz);
       	//System.out.println("Epsilon Value : " + epsilon_r);
       	//System.out.println("Conductivity Value : " + sigma);
       	//System.out.println("Polarisation : " + the_polarisation);
       	
       	//System.out.println("Scenario  : " + the_scenario);
       	//System.out.println("Tutorial choice is is is : " + tutorial_choice);
       	*/
		
       	//epsilon = global_definitions.epsilon_0*epsilon_r ; 
       	}

	public	void initialise_variables(){
		
		
		no_of_bins  = 1000 ; 
		sample_value = new complex(0.0,0.0) ;
		LOS_power_in_linear = Math.pow(10.0,LOS_power_in_dBm/10.0)/1000.0  ;
		NLOS_power_in_linear = Math.pow(10.0,NLOS_power_in_dBm/10.0)/1000.0  ;
		LOS_amplitude = Math.sqrt(LOS_power_in_linear);
		NLOS_amplitude = Math.sqrt(NLOS_power_in_linear);
			
	    			}
	
 
	public String rayleigh_compute(){
			
			int	 		bin_counter, counter1, counter2 ;
			int 		NO_OF_SAMPLES = no_of_samples;
			int			NO_OF_SCATTERERS = no_of_scatterers ; 
			int 		bin_index ;
			
			double[]	rayleigh_values ;
			double[]	rician_values ; 
			double[]	x_values ;
			double[]	rayleigh_samples ;
			double[]	rician_samples; 
			double[] 		rayleigh_hist ;
			double[] 		rician_hist ;
			double[]	rayleigh_empirical_cdf ; 
			double[]	rician_empirical_cdf ; 
			double[]	rayleigh_cdf ; 
			double[]	rician_cdf ; 
			
			
			String 	rayleigh_answer = new String() ; 
			String	rayleigh_output = new String() ; 
			String 	rician_output = new String() ; 
			
			String 	bin_output 		=	new	String() ;
			String	rayleigh_samples_string = new String() ; 
			String	rician_samples_string = new String() ; 
			
			String	rayleigh_empirical_cdf_string = new String() ; 
			String	rayleigh_cdf_string = new String() ; 
			String	rician_empirical_cdf_string = new String() ; 
			String	rician_cdf_string = new String() ; 
			

			
			rayleigh_values =			new double[no_of_bins] ; 
			rician_values 	=			new double[no_of_bins] ;
			x_values 			=		new	double[no_of_bins] ; 
			rayleigh_empirical_cdf = new double[no_of_bins] ; 
			rician_empirical_cdf = new double[no_of_bins] ; 
			rayleigh_cdf = new double[no_of_bins] ; 
			rician_cdf = new double[no_of_bins] ; 
			
			rayleigh_samples  = new double[NO_OF_SAMPLES] ;
			rician_samples  = new double[NO_OF_SAMPLES] ;
			
			rayleigh_hist  = new double[no_of_bins] ;
			rician_hist  = new double[no_of_bins] ;
			
			double 	the_max_value  = -100   ;
			double	the_min_value = 0.0  ; 
			
			Random 	rndNumbers = new Random();
	        double 	rndNumber1, rndNumber2 ; 
	        

	        
	       // //System.out.println(" Starting");

	        alpha = Math.sqrt(NLOS_power_in_linear/NO_OF_SCATTERERS) ; 
	       


			for( counter1 = 0 ; counter1 < no_of_bins ; counter1++){
				rayleigh_hist[counter1] = 0.0 ; 
				rician_hist[counter1] = 0.0 ; 
				
			}
			
			double pi_value = Math.acos(-1.0) ; 
			
			
			
			for( counter1 = 0 ; counter1 < NO_OF_SAMPLES ; counter1++){
			       ////System.out.println(" counter1  : " + counter1);
				sample_value = new complex(0.0,0.0) ;
				
			       
				for(counter2 = 0 ; counter2 < NO_OF_SCATTERERS ; counter2++){
					rndNumber1  = rndNumbers.nextDouble() ;
					rndNumber2  = rndNumbers.nextDouble() ;
					
					
					sample_value = sample_value.plus( new complex( Math.cos(2.0*(rndNumber1-0.5)*pi_value), Math.sin(2.0*(rndNumber1-0.5)*pi_value) ) );
					 
					
					}
				rician_sample_value = sample_value.mult(new complex(alpha,0.0)) ;
				rician_sample_value = rician_sample_value.plus(new complex(LOS_amplitude,0.0)) ; 
				
				
			rayleigh_samples[counter1] = sample_value.abs()*alpha; 
			rician_samples[counter1] = rician_sample_value.abs() ; 
		
			if(rayleigh_samples[counter1] > the_max_value){
				the_max_value = rayleigh_samples[counter1] ;
				} 
			
			if(rician_samples[counter1] > the_max_value){
				the_max_value = rician_samples[counter1] ;
				} 
			
			
	}
			
sigma = Math.sqrt(NLOS_power_in_linear/2.0) ; 

double K_value = LOS_power_in_linear/NLOS_power_in_linear ; 



		the_max_value = the_max_value*1.000000001 ; 
	    // System.out.println(" the max value : " + the_max_value);
			
			double 	delta_x = (the_max_value - the_min_value)/(no_of_bins) ;
		     //  //System.out.println(" delta_x : " + delta_x);
			
		    for(counter1 = 0 ; counter1 < NO_OF_SAMPLES ; counter1++){   
			bin_index = (int) Math.floor(rayleigh_samples[counter1]/delta_x) ;
			
			if(bin_index >= no_of_bins)
				bin_index = no_of_bins - 1 ; 
			

		    rayleigh_hist[bin_index]  = rayleigh_hist[bin_index] + 1.0/(NO_OF_SAMPLES*delta_x)  ; 
		    
		    bin_index = (int) Math.floor(rician_samples[counter1]/delta_x) ;
			
			if(bin_index >= no_of_bins)
				bin_index = no_of_bins - 1 ; 
			

		    rician_hist[bin_index]  = rician_hist[bin_index] + 1.0/(NO_OF_SAMPLES*delta_x)  ; 

		    }

		    
			
double max_y_value = 0.0 ; 


rayleigh_empirical_cdf[0] =  rayleigh_hist[0]*delta_x ; 
rician_empirical_cdf[0] = rician_hist[0]*delta_x;
//rayleigh_cdf[0] = 0.0 ;
rayleigh_cdf[0] = 1 - Math.exp(- Math.pow(x_values[0],2)/(2.0*sigma*sigma)) ; 
rician_cdf[0] = 0.0 ; 


rayleigh_empirical_cdf_string =  rayleigh_empirical_cdf_string + (rayleigh_empirical_cdf[0]) + "\n" ; 
rayleigh_cdf_string =  rayleigh_cdf_string + (rayleigh_cdf[0]) + "\n" ; 
rician_empirical_cdf_string =  rician_empirical_cdf_string + (rician_empirical_cdf[0]) + "\n" ; 
rician_cdf_string =  rician_cdf_string + (rician_cdf[0]) + "\n" ; 

for(bin_counter  = 0 ; bin_counter < no_of_bins ; bin_counter++){

	x_values[bin_counter] = (bin_counter+0.5)*delta_x ; 
	 //rician(count) = x(count)/rayleigh_sigma_squared*(exp(-(x(count)*x(count) + s*s)/(2.0*rayleigh_sigma_squared)))*BESSELI(0,x(count)*s/rayleigh_sigma_squared); 
	   
	
	rayleigh_values[bin_counter] = (x_values[bin_counter]/(sigma*sigma))*Math.exp(-(x_values[bin_counter]*x_values[bin_counter])/(2.0*(sigma*sigma)) ) ; 
	rician_values[bin_counter] = (x_values[bin_counter]/(sigma*sigma))*Math.exp(-(x_values[bin_counter]*x_values[bin_counter] + LOS_power_in_linear)/(2.0*(sigma*sigma))) ;
	rician_values[bin_counter] = rician_values[bin_counter]*SpecialFunction.besselI0(x_values[bin_counter]*LOS_amplitude/(sigma*sigma)) ; 

	if(bin_counter>0){
	rayleigh_empirical_cdf[bin_counter] =  rayleigh_empirical_cdf[bin_counter-1] +  rayleigh_hist[bin_counter]*delta_x ; 
	rician_empirical_cdf[bin_counter] =  rician_empirical_cdf[bin_counter-1] + rician_hist[bin_counter]*delta_x ;
	rayleigh_cdf[bin_counter] =  rayleigh_cdf[bin_counter-1] + rayleigh_values[bin_counter]*delta_x ; 
	//rayleigh_cdf[bin_counter] = 1 - Math.exp(- Math.pow(x_values[bin_counter],2)/(2.0*sigma*sigma)) ; 
	
	rician_cdf[bin_counter] = rician_cdf[bin_counter-1] + rician_values[bin_counter]*delta_x ; 
	rayleigh_empirical_cdf_string =  rayleigh_empirical_cdf_string + (rayleigh_empirical_cdf[bin_counter]) + " " ; 
	 rayleigh_cdf_string =  rayleigh_cdf_string + (rayleigh_cdf[bin_counter]) + " " ; 

	 rician_empirical_cdf_string =  rician_empirical_cdf_string + (rician_empirical_cdf[bin_counter]) + " " ; 
	 rician_cdf_string =  rician_cdf_string + (rician_cdf[bin_counter]) + " " ; 

	}
	
	//System.out.println(" argument is   : " + x_values[bin_counter]*LOS_amplitude/(sigma*sigma));
	//System.out.println(" bessle values  : " + SpecialFunction.besselI0(x_values[bin_counter]*LOS_amplitude/(sigma*sigma)) );
	//System.out.println(" rician values  : " + rician_values[bin_counter]);
	
	
	if(rayleigh_hist[bin_counter] > max_y_value){
		 max_y_value = rayleigh_hist[bin_counter] ;}
	if(rician_hist[bin_counter] > max_y_value){
		 max_y_value = rician_hist[bin_counter] ;}
	if(rician_values[bin_counter] > max_y_value){
		 max_y_value = rician_values[bin_counter] ;}

	
	bin_output = bin_output + (x_values[bin_counter]) + " " ; 
	rayleigh_output = rayleigh_output  + (rayleigh_values[bin_counter]) + " " ; 
    ////System.out.println(" rayleigh_hist  : " + rayleigh_hist[bin_counter]);
	rician_output = rician_output  + (rician_values[bin_counter]) + " " ; 

	rayleigh_samples_string = rayleigh_samples_string + (rayleigh_hist[bin_counter]) + " " ; 
	rician_samples_string = rician_samples_string + (rician_hist[bin_counter]) + " " ; 
 
	
	 	 
}

//System.out.println(" max_value  : " + the_max_value);

	rayleigh_answer = no_of_bins + " " + the_max_value + " " + max_y_value + " "  +  K_value + " " + sigma + " " + Math.sqrt(LOS_power_in_linear) + " " ; 
	//Lose bin_output! 2016 CB 
	
	rayleigh_answer = rayleigh_answer + bin_output + rayleigh_output+ rayleigh_samples_string + rician_output + rician_samples_string ; 
	rayleigh_answer = rayleigh_answer + rayleigh_empirical_cdf_string + rayleigh_cdf_string  ; 
	rayleigh_answer = rayleigh_answer + rician_empirical_cdf_string + rician_cdf_string  ; 
	
	
	/*if( the_polarisation.equals("perpendicular") == true){
		reflection_answer = reflection_answer+perp_ref_coeff_values + perp_trans_coeff_values ; 
	}
	else{
		reflection_answer = reflection_answer+par_ref_coeff_values + par_trans_coeff_values ; 
	}*/
	
	//System.out.println(" Rayleigh answer is: " + rayleigh_answer) ; 	
	return(rayleigh_answer) ;

}
}
