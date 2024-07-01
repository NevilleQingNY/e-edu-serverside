package com.example.ESOA.controller;

import com.example.ESOA.service.set_up_problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/reflectionServlet")
@CrossOrigin(origins = {
        "https://electromagnetic-tutorial.onrender.com"
})
public class ReflectionController {

    private final ObjectMapper objectMapper;

    @Autowired
    public ReflectionController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handlePost(request, response);
    }

    @PostMapping
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String conductivity_value = request.getParameter("conductivity");
        String epsilon_value = request.getParameter("epsilon");
        String frequency_value = request.getParameter("frequency");
        String polarisation_value = request.getParameter("polarisation");
        String scenario_value = request.getParameter("scenario");
        String width_value = request.getParameter("width");
        String tutorial_value = request.getParameter("tutorial_value");

        int tutorial_choice;
        try {
            tutorial_choice = Integer.parseInt(tutorial_value);
            System.out.println(tutorial_choice);

        } catch (NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{\"error\":\"Invalid tutorial_value. It must be an integer.\"}");
            return;
        }

        set_up_problem run_problem = new set_up_problem();
        run_problem.read_in_variables(conductivity_value, epsilon_value, frequency_value, polarisation_value,
                scenario_value, width_value, tutorial_value);
        run_problem.initialise_variables();

        String reflection_answer;
        if (tutorial_choice == 1 || tutorial_choice == 2 || tutorial_choice == 3) {
            reflection_answer = run_problem.propagation_and_reflection_make_frames();
        } else if (tutorial_choice == 4) {
            reflection_answer = run_problem.oblique();
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{\"error\":\"Invalid tutorial choice.\"}");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper
                .writeValueAsString(new ReflectionResponse(new String[] { reflection_answer }));
        response.getWriter().write(jsonResponse);
    }

    static class ReflectionResponse {
        public String[] message;

        public ReflectionResponse(String[] message) {
            this.message = message;
        }
    }
}
