package com.example.ESOA.service;

//
//  diffraction_models.java
//  terrain_propagation_models
//
//  Created by Patrick Murphy & Conor Brennan on 05/08/2011.
//  Copyright 2011 Dublin City University. All rights reserved.
//


public class diffraction_models {

    public	int[] global_pos_x ;
    public	double[] global_pos_y ;
    
    
    public double hata_model(double h_antenna, double h_MS, double f_in_MHz , double d_in_km ,int area_type){ 

    	int i  = area_type;
    	double	c_hr = 0.0 ; 
    	double 	LdB = 0.0 ; 

    if( (f_in_MHz>=100.0) && (f_in_MHz <= 2000.0) ){
    	
    	
 
        if( (h_antenna >= 10.0) && (h_antenna <= 200.0)){

            if( (h_MS >=1 ) && ( h_MS <= 100.0))      // Need to modify 100 --> 10
               
            	if( (i==1) || (i==2) || (i==3) || (i==4) ){

                    if( (d_in_km >= 0.0) && (d_in_km <=40.0)){     // Need to modify 40 --> 20

                        if( (i==1) || (i==3) || (i==4)){
                            c_hr = (1.1 * Math.log10(f_in_MHz) - 0.7) * h_MS - (1.56 * Math.log10(f_in_MHz) - 0.8);
                        }
            
                        if(i==2){
                            if(f_in_MHz <= 200.0){
                                c_hr = 8.29 * Math.pow(Math.log10(1.54 * h_MS),2.0) - 1.1;
                            }
                            if( (f_in_MHz < 400.0 ) && (f_in_MHz >200 )){
                                c_hr = 3.2 * Math.pow( Math.log10(11.75 * h_MS),2.0) - 4.97 ;
                            }
                        }
                        
                        LdB = 69.55 + 26.16 * Math.log10(f_in_MHz) - 13.82 * Math.log10(h_antenna) - c_hr + (44.9 - 6.55 * Math.log10(h_antenna)) * Math.log10(d_in_km);
        
                        if(i == 3){
                            LdB = LdB - 2.0 * Math.pow(Math.log10(f_in_MHz / 28),2.0) - 5.4;
                        		}
            
                        if(i == 4){
                            LdB = LdB - 4.78 * Math.pow(Math.log10(f_in_MHz),2.0) + 18.33 * Math.log10(f_in_MHz) - 40.94;
                        }
        }
    } 
    } 
    }
    return LdB ; 
}

       public double epstein_peterson(int n, double the_freq) {

        double fc = the_freq  ; 
        double c  = 300000000.0 ; 
        double lambda = c/fc ;
    	double Ld = 0 ; 
    			int m ; 
    			double d1, d2 , hobs, nu ; 
    			double[] Ldi ; 
    			Ldi = new double[n]  ; 


    			for (m=0; m < n; m++) { 
    			    
    				d1= global_pos_x[m+1]-global_pos_x[m];
    			    d2= global_pos_x[m+2]-global_pos_x[m+1];
    			    /*System.out.println("pos_x m equals "+global_pos_x[m]) ;
    			    System.out.println("pos_x m equals "+global_pos_x[m+1]) ;
    			    System.out.println("pos_x m equals "+global_pos_x[m+2]) ;
        			  */  
    			    hobs= global_pos_y[m+1]-(( d1 *(global_pos_y[m+2]-global_pos_y[m])/(global_pos_x[m+2]- global_pos_x[m]  ))+ global_pos_y[m] );
    			    nu = hobs*Math.sqrt((2.0/lambda)*(d1+d2)/(d1*d2));
    			   
    			    /*System.out.println("nu equals "+nu) ;
    			    System.out.println("hobs equals "+hobs);
    			    System.out.println("d1 equals "+d1) ;
    			    System.out.println("d2 equals "+d2);
    			  */
    			    
    			    if (nu< -0.7)
    			        Ldi[m] = 0.0;
    			    else
    			        Ldi[m]=6.9+20*Math.log10(Math.sqrt((nu-0.1)*(nu-0.1)+1.0) +nu -0.1) ;
    			    Ld = Ld + Ldi[m] ; 
    			}
return Ld ; 
       }

    public int find_obstructs_derivative(int end_point, terrain_linear_set_up_problem run_problem){
    	int threshold = 0;
    	int start_point = 0 ; 
    	int[] local_max_x, pos_x, pos_final_x ; 
    	double[] dev , local_max_y, pos_y , pos_final_y ; 
    	int i    ; 
    	dev = new double[4] ;
    	local_max_x = new int[end_point+1] ;
    	local_max_y = new double[end_point+1] ;
    	pos_x = new int[end_point+2]; 
    	pos_y = new double[end_point+2]; 
    	pos_final_x = new int[end_point+2]; 
    	pos_final_y = new double[end_point+2]; 
    	int m = 0;
    	
    	for (i = start_point+2 ; i <= end_point-2 ; i++){
    	    dev[0] = run_problem.terrain_start[i].y - run_problem.terrain_start[i-2].y ;
    	    dev[1] = run_problem.terrain_start[i].y - run_problem.terrain_start[i-1].y ;
    	    dev[2] = run_problem.terrain_start[i].y - run_problem.terrain_start[i+1].y ;
    	    dev[3] = run_problem.terrain_start[i].y - run_problem.terrain_start[i+2].y ;
    	    
    	    if ( ((dev[0] > threshold) && (dev[1] > threshold) && (dev[2] >= 0) && (dev[3] >= 0) ) ||  ( (dev[0] >= 0) && (dev[1] >= 0) && (dev[2] > threshold) && (dev[3] > threshold)) ){
    	        local_max_x[m] = i;
    	        local_max_y[m] = run_problem.terrain_start[i].y ;
    	        m = m + 1;
    	    }
    	}
    	// NB At this point local_max_x runs from 0 to m-1 inclusive.
    	
    	pos_x[0] = start_point   ; 
    	pos_y[0] = run_problem.terrain_start[start_point].y ; 
    	
    	for(i = 0 ; i < m ; i++){
    	pos_x[i+1] =local_max_x[i] ; 
    	pos_y[i+1] =local_max_y[i] ; 
    	}
    	
    	pos_x[m+1] = end_point ; 
    	pos_y[m+1] = run_problem.terrain_start[end_point].y ; 

    	int length_of_final_pos_x = m+2 ; // pos_x runs from 0 to m+1 inclusive
    	
threshold = 1;              // This threshold is used to omit fake obstacles (m)


double threshold_2 = 0.5;          // Threshold to omit the ...
int times = 6;                  // Repeat times for the loop 

for(i = 0 ; i <= m+1 ; i++){
pos_final_x[i] = pos_x[i] ; 
pos_final_y[i] = pos_y[i] ; 
}

//System.out.println("Getting to here") ;

double h_MS = run_problem.receiver_height ; 
double h_antenna = run_problem.transmitter_height; 
int k = 0 ; 

for (m = 1 ; m<= times ; m++){ 
	//System.out.println("m equals " +m) ;
	k = 0 ;
double temp = 0.0 ; 
int[] temp_2 ; 
temp_2 = new int[end_point+2] ; 

for(i = 0; i < length_of_final_pos_x - 2; i++){

	if( i==0)
        temp = (pos_final_y[i+2]+ h_MS - pos_final_y[i]-h_antenna)*(pos_final_x[i+1]-pos_final_x[i])/(pos_final_x[i+2]-pos_final_x[i]) + pos_final_y[i]+h_antenna;
    else
        temp = (pos_final_y[i+2]-pos_final_y[i])*(pos_final_x[i+1]-pos_final_x[i])/(pos_final_x[i+2]-pos_final_x[i]) + pos_final_y[i];

	if (temp < pos_final_y[i+1] - threshold_2){
    temp_2[k] = pos_final_x[i+1];
    k = k + 1;
	}

} // end of i

// At this stage temp_2 is of length k-1 ; 

// pos_final will be of length k+1 (running from 0 to k) 
pos_final_x[0] = pos_x[0] ; 
pos_final_y[0] = run_problem.terrain_start[pos_x[0]].y  ; 

for(i=0; i<k ;  i++){
pos_final_x[i+1] = temp_2[i] ; 
pos_final_y[i+1] = run_problem.terrain_start[pos_final_x[i+1]].y ; 
}

pos_final_x[k+1] = end_point ; 
pos_final_y[k+1] = run_problem.terrain_start[pos_final_x[k+1]].y ;
length_of_final_pos_x = k+2 ; 

}

for(i=0;i<=k+1;i++){
global_pos_x[i] = pos_final_x[i] ; 
global_pos_y[i] = pos_final_y[i] ; 
/*System.out.println("i equals "+ i ) ;
System.out.println("global_pos_x equals "+ global_pos_x[i] ) ;
System.out.println("global_pos_y equals "+ global_pos_y[i] ) ;*/
}


int length_of_global_pos_x  =  k+2 ; 
return length_of_global_pos_x ; 
}

    
    public String EP(terrain_linear_set_up_problem run_problem ) {
    	String return_to_servlet  = ""; 
        String diffraction_start_index = "" ; 
       String diffraction_points = "" ; 
       String running_total_st = "" ; 
       String field_values_st = "";
        double fc = run_problem.f ;
        
        int 	i,counter  ; 
        int start_point = 0 ; 
    	int no_of_points  ; 
        int running_total = 0 ; 
        double[]  Lfs , Ld, Loss; 
        double segment_length ; 
        
        global_pos_x = new int[512] ; 
        global_pos_y = new double[512] ; 
        
        Lfs = new double[512] ;
        Ld = new double[512] ;
        Loss = new double[512] ; 
        
        no_of_points = 0 ; 
        diffraction_start_index = diffraction_start_index + no_of_points+ "\n" ; 

        
        segment_length = run_problem.terrain_start[1].x - run_problem.terrain_start[0].x ; 
        
        for( i = 1; i <= run_problem.terrain_group_counter ; i++){

         diffraction_start_index = diffraction_start_index + no_of_points+ "\n" ; 

        	//System.out.println("i equals " +i +" \n") ;
        	
        /* Make the pos_x and the pos_y */
        	
        no_of_points = find_obstructs_derivative(i,run_problem);       // Make pos_x and pos_y ; 
        running_total = running_total + no_of_points ; 
        
    	//System.out.println("no_of_points found equals " +no_of_points +" \n") ;
        for(counter =1 ; counter<no_of_points ; counter++ ){
        	diffraction_points = diffraction_points + global_pos_y[counter] + "\n"; 
        }
        
        Ld[i] = -1.0*epstein_peterson(no_of_points-2,run_problem.f) ; 
        //System.out.println("Diffraction loss " +Ld[i]) ;
        
        Lfs[i]  = -(32.5 +20.0*Math.log10(fc/1000000.0) + 20.0*Math.log10(segment_length*(i-start_point)/1000.0  )) ; 
        Loss[i] = Lfs[i] + Ld[i] ; 
        field_values_st = field_values_st + segment_length*i + " " + Loss[i] +" \n" ; 
          

        }
        
        /*running_total_st = running_total_st + running_total + "\n" ; 
        return_to_servlet = return_to_servlet + running_total_st ;
        */
    	
        return_to_servlet = return_to_servlet + field_values_st ; 

    	return return_to_servlet ; 
    	
    }

 }




