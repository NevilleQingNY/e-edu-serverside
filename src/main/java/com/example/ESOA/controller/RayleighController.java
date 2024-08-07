package com.example.ESOA.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ESOA.service.ray_leigh_set_up_problem;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rayleighServlet")
@CrossOrigin(origins = "*")
public class RayleighController {

    private final ObjectMapper objectMapper;

    @Autowired
    public RayleighController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> handleGet(@RequestParam String no_of_scatterers,
                                            @RequestParam String no_of_samples,
                                            @RequestParam String LOS_power_in_dBm,
                                            @RequestParam String NLOS_power_in_dBm) throws IOException {
        return handlePost(no_of_scatterers, no_of_samples, LOS_power_in_dBm, NLOS_power_in_dBm);
    }

    @PostMapping
    public ResponseEntity<String> handlePost(@RequestParam String no_of_scatterers,
                                             @RequestParam String no_of_samples,
                                             @RequestParam String LOS_power_in_dBm,
                                             @RequestParam String NLOS_power_in_dBm) throws IOException {
        
        String rayleigh_answer;
        ray_leigh_set_up_problem run_problem = new ray_leigh_set_up_problem();
        
        run_problem.read_in_variables(no_of_scatterers, no_of_samples, LOS_power_in_dBm, NLOS_power_in_dBm);
        run_problem.initialise_variables();
        rayleigh_answer = run_problem.rayleigh_compute();

        String jsonResponse = objectMapper.writeValueAsString(new RayleighResponse(new String[]{rayleigh_answer}));
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    static class RayleighResponse {
        public String[] message;

        public RayleighResponse(String[] message) {
            this.message = message;
        }
    }
}
