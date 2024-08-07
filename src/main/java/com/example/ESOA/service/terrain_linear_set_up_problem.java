package com.example.ESOA.service;

//
//  set_up_problem.java
//
//
//  Created by Patrick Murphy & Conor Brennan on 04/08/2011.
//  Copyright (c) 2011 Dublin City University. All rights reserved.
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;


public class terrain_linear_set_up_problem {
    public double 	f, transmitter_height, receiver_height, P0, omega, lambda, wavenumber, eta, DELTAX;
    public double	average_terrain_height ; 
    public int run_the_GFPM, run_deygout, run_EP,  run_single_knife_edge ; 
    public int terrain_group_counter = -1 ; 
    public int group_counter;
    public Point3d[] terrain_start;
    public Point3d za;
   // public static final String relName = ""; 
    public  String relName = ""; 
     

    
    public void read_in_variables(String frequency, String antennaHeight, String receiverHeight) {
      System.out.println("Received parameters:");
      System.out.println("Frequency: " + frequency);
      System.out.println("Antenna Height: " + antennaHeight);
      System.out.println("Receiver Height: " + receiverHeight);

      // Ensure that none of the parameters are null
      if (frequency == null || antennaHeight == null || receiverHeight == null) {
          throw new IllegalArgumentException("Parameters cannot be null");
      }

      try {
          // Parse the parameters
          f = Double.parseDouble(frequency) * global_definitions.MHz;
          transmitter_height = Double.parseDouble(antennaHeight);
          receiver_height = Double.parseDouble(receiverHeight);
      } catch (NumberFormatException e) {
          System.err.println("Error parsing parameters: " + e.getMessage());
          throw new IllegalArgumentException("Invalid parameter format", e);
      }

      System.out.println("Parsed values:");
      System.out.println("Frequency in Hz: " + f);
      System.out.println("Transmitter Height: " + transmitter_height);
      System.out.println("Receiver Height: " + receiver_height);
  }
    public void initialise_variables() {

        omega = 2.0 * global_definitions.pi * f;
        lambda = global_definitions.c / f;
        wavenumber = 2.0 * global_definitions.pi / lambda;
        eta = Math.sqrt(global_definitions.mu0 / global_definitions.epsilon0);
    }

    public String read_measured_from_file(String the_terrain_profile, double the_frequency) {
    	
    	
    	String return_to_servlet  = ""; 
    	String snippet  = "" ; 
    	int frequency_as_integer  = (int) the_frequency ; 
    	
    	//System.out.println(the_terrain_profile) ; 
    	
    	if(the_terrain_profile.equals("Hjorring") == true )
    		snippet = "hjo" ; 
    	if(the_terrain_profile.equals("Mjels") == true )
    		snippet = "mje" ; 
    	if(the_terrain_profile.equals("Ravnstru") == true )
    		snippet = "rav" ; 
    	if(the_terrain_profile.equals("Jerslev") == true )
    		snippet = "jer" ; 
    	if(the_terrain_profile.equals("Hadsund") == true )
    		snippet = "had" ; 
    	
    	
    relName = "../../terrain_information/"+the_terrain_profile+"/m"+snippet+frequency_as_integer+".10m";
    
  //  System.out.println("The path is "+relName) ; 
   // relName = "../../mhjo144.10m";


    	InputStream s = set_up_problem.class.getResourceAsStream(relName);
    		  
    		  if( s == null)
    		  { 
    			  System.out.println("Problem with file") ; 
     		  }
    		  InputStreamReader jimmy ; 
    		  BufferedReader reader ;    
    			
    		    
    		  try{  
    	      jimmy = new InputStreamReader(s,"UTF-8" );
    	      reader = new BufferedReader(jimmy);
	      	 	
    		  String currentline  ;
    		  String[] temp_string ;
    		  double x, y ; 
    		  
    		  
    		  temp_string = new String[2] ;
    		  
    		  
    		  while ( (currentline = reader.readLine()) != null) 
    		  {
    		  temp_string = currentline.trim().split(" ");
/*   			  System.out.println(" temp string[0] is "+temp_string[0]) ;
   			  System.out.println(" temp string[1] is "+temp_string[1]) ;
*/
                  //  System.out.println("Line is "+line); 

    //  System.out.println("temp_string "+temp_string[0]) ;
     // System.out.println("temp_string "+temp_string[1]) ;
      

                    x = Double.parseDouble(temp_string[0].trim());
                    y = Double.parseDouble(temp_string[1].trim());
                    
                    
                    //System.out.println("Measured data x "+x) ;
                    //System.out.println("Measured data y "+y) ;
                                  
                    return_to_servlet = return_to_servlet + x +" "+y+" \n" ;
    		  }
    		  
    		  }
    		  catch( IOException e){
    			 } 

    		  return return_to_servlet; 
    		  
    }
    
    
    public String read_terrain_from_file(String the_terrain_profile) {
    	
    	String return_to_servlet  = ""; 

    relName = "../../terrain_information/"+the_terrain_profile+"/"+the_terrain_profile+".dhm";
   // relName = "../../terrain_information/Hjorring/Hjorring.dhm" ; 
    
   // System.out.println("The path is "+relName) ; 
   // relName = "../../mhjo144.10m";
        terrain_start = new Point3d[global_definitions.MAX_NUMBER_OF_TERRAIN_SAMPLES];

   average_terrain_height = 0.0 ; 

    	InputStream s = set_up_problem.class.getResourceAsStream(relName);
    		  
    		  if( s == null)
    		  { 
    			  System.out.println("Problem with file") ; 
     		  }
    		  InputStreamReader jimmy ; 
    		  BufferedReader reader ;    
    			
    		    
    		  try{  
    	      jimmy = new InputStreamReader(s,"UTF-8" );
    	      reader = new BufferedReader(jimmy);
	      	 	
    		  String currentline  ;
    		  String[] temp_string ;
    		  double x, y ; 
    		  
			 // System.out.println("terrain_group_counter " +terrain_group_counter) ;

    		  
    		  temp_string = new String[2] ;
    		  
    		  
    		  while ( (currentline = reader.readLine()) != null) 
    		  {
    			  
    			  //System.out.println("Hey buddy boy " +currentline) ;
    			  //System.out.println("terrain_group_counter " +terrain_group_counter) ;
    			  
    			  
    			 temp_string = currentline.trim().split(" ");
   			 // System.out.println(" temp string[0] is "+temp_string[0]) ;
   			  //System.out.println(" temp string[1] is "+temp_string[1]) ;


                  //  System.out.println("Line is "+line); 

    //  System.out.println("temp_string "+temp_string[0]) ;
     // System.out.println("temp_string "+temp_string[1]) ;
      

                    x = Double.parseDouble(temp_string[0].trim());
                    y = Double.parseDouble(temp_string[1].trim());
                    

                    average_terrain_height += y ; 

                    //System.out.println("x "+x) ;
                    //System.out.println("y "+y) ;
                    
                    
                    terrain_group_counter++;
                    //System.out.println(" Here "+terrain_group_counter) ;

                    terrain_start[terrain_group_counter] = new Point3d(x, y, 0.0);
                    //System.out.println("terrain_start "+terrain_group_counter +" equals "+terrain_start[terrain_group_counter].x +" and "+terrain_start[terrain_group_counter].y) ;
                   
                    return_to_servlet = return_to_servlet +terrain_start[terrain_group_counter].x +" " ;
                    return_to_servlet = return_to_servlet +terrain_start[terrain_group_counter].y +" \n";
    		  }
    		  

    		  average_terrain_height /= average_terrain_height ; 
            za = new Point3d(terrain_start[0].x, terrain_start[0].y + transmitter_height, 0.0);
                              	 	 
    		  }
    		  catch( IOException e){
    			 } 

    		  return return_to_servlet; 
    		  
    }
    	 
    
    public String read_terrain_from_data(String myanswer)
    {
    	//System.out.println("myanswer = "+myanswer);
        double x = 0.0;
        double y = 0.0;

        String line;
        String[] temp_string;
        String return_to_servlet  = ""; 

        terrain_start = new Point3d[global_definitions.MAX_NUMBER_OF_TERRAIN_SAMPLES];

        
        terrain_group_counter = -1;
        group_counter = 0;


           StringTokenizer st = new StringTokenizer(myanswer, "\n");
            //System.out.println("myanswer "+st) ;

            line = st.nextToken();
            //System.out.println("line "+line) ;

        
            while (st.hasMoreTokens() )
            {
                temp_string = line.trim().split(" ");
              //  System.out.println("Line is "+line); 

//  System.out.println("temp_string "+temp_string[0]) ;
 // System.out.println("temp_string "+temp_string[1]) ;
  

                x = Double.parseDouble(temp_string[0]);
                y = Double.parseDouble(temp_string[1]);
                
                //System.out.println("x "+x) ;
                //System.out.println("y "+y) ;
                
                
                terrain_group_counter++;

                terrain_start[terrain_group_counter] = new Point3d(x, y, 0.0);
                //System.out.println("terrain start equals "+terrain_start[terrain_group_counter].x) ;
            
                return_to_servlet = return_to_servlet +terrain_start[terrain_group_counter].x +" " ;
                return_to_servlet = return_to_servlet +terrain_start[terrain_group_counter].y +" \n";
                line = st.nextToken();
         }

        za = new Point3d(terrain_start[0].x, terrain_start[0].y + transmitter_height, 0.0);
        return(return_to_servlet) ; 
        
    }
    
}
