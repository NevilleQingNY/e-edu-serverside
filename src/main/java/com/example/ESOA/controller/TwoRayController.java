package com.example.ESOA.controller;

import com.example.ESOA.service.two_ray_set_up_problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/TwoRayServlet")
@CrossOrigin(origins = "*")
public class TwoRayController {

    private final ObjectMapper objectMapper;

    @Autowired
    public TwoRayController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handlePost(request, response);
    }

    @PostMapping
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //System.out.println("Conductivity Value : " + request.getParameter("conductivity"));
        //System.out.println("Epsilon Value : " + request.getParameter("epsilon"));

        //read in selected height, frequency, power, and models
        //String conductivity_value = request.getParameter("conductivity");
        //String epsilon_value = request.getParameter("epsilon");
        String frequency_value = request.getParameter("frequency");
        //String polarisation_value  = request.getParameter("polarisation")    ;
        String TX_height_value = request.getParameter("TX_height") ;
        String RX_height_value = request.getParameter("RX_height") ;


        String two_ray_answer = new String();

        two_ray_set_up_problem    run_problem =    new two_ray_set_up_problem() ;

        run_problem.read_in_variables(frequency_value,  TX_height_value, RX_height_value) ; 
        run_problem.initialise_variables() ; 

        two_ray_answer =  run_problem.two_ray_analysis()  ; 


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper
                .writeValueAsString(new TwoRayResponse(new String[]{two_ray_answer}));
        response.getWriter().write(jsonResponse);
    }

    static class TwoRayResponse {
        public String[] message;

        public TwoRayResponse(String[] message) {
            this.message = message;
        }
    }
}
