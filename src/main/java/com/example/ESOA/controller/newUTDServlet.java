package com.example.ESOA.controller;

import com.example.ESOA.service.new_UTD_set_up_problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/newUTDServlet")
@CrossOrigin(origins = "*")
public class newUTDServlet {

    private final ObjectMapper objectMapper;

    @Autowired
    public newUTDServlet(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handlePost(request, response);
    }

    @PostMapping
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String TX_height_value = request.getParameter("TX_height");
        String frequency_value = request.getParameter("frequency");
        String RX_distance_value = request.getParameter("RX_distance");
        String epsilon_r_value = request.getParameter("epsilon");


        if (TX_height_value == null || frequency_value == null || RX_distance_value == null || epsilon_r_value == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{\"error\":\"One or more parameters are missing.\"}");
            return;
        }

        String fields_along_line;
        try {
            new_UTD_set_up_problem run_problem = new new_UTD_set_up_problem();
            run_problem.read_in_variables(TX_height_value, frequency_value, RX_distance_value, epsilon_r_value);
            run_problem.initialise_variables();
            fields_along_line = run_problem.compute_fields_along_line();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("{\"error\":\"An error occurred while processing the request: " + e.getMessage() + "\"}");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper
                .writeValueAsString(new UTDResponse(new String[]{fields_along_line}));
        response.getWriter().write(jsonResponse);
    }

    static class UTDResponse {
        public String[] message;

        public UTDResponse(String[] message) {
            this.message = message;
        }
    }
}
