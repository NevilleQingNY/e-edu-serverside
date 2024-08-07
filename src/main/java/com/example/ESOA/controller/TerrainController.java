package com.example.ESOA.controller;


import com.example.ESOA.service.diffraction_models;
import com.example.ESOA.service.terrain_linear_set_up_problem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/TerrainServlet")
@CrossOrigin(origins = "*")
public class TerrainController {

    private final ObjectMapper objectMapper;

    @Autowired
    public TerrainController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handlePost(request, response);
    }

    @PostMapping
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tutorialChoice = request.getParameter("tutorial_type");
        String terrainProfile = request.getParameter("myData");
        String frequency = request.getParameter("frequency");
        String antennaHeight = request.getParameter("antennaHeight");
        String receiverHeight = request.getParameter("receiverHeight");

        String EP_answer = "";
        String Terrain_answer = "";
        String Measured_answer = "";

        terrain_linear_set_up_problem run_problem = new terrain_linear_set_up_problem();
        run_problem.read_in_variables(frequency, antennaHeight, receiverHeight);
        run_problem.initialise_variables();

        if ("Danish".equals(tutorialChoice)) {
            Terrain_answer = run_problem.read_terrain_from_file(terrainProfile);
            Measured_answer = run_problem.read_measured_from_file(terrainProfile, Double.parseDouble(frequency));
        }

        if ("Google".equals(tutorialChoice)) {
            Terrain_answer = run_problem.read_terrain_from_data(terrainProfile);
            Measured_answer = "";
        }

        diffraction_models run_models = new diffraction_models();
        EP_answer = run_models.EP(run_problem);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(new TerrainResponse(new String[]{EP_answer, Terrain_answer, Measured_answer}));
        response.getWriter().write(jsonResponse);
    }

    static class TerrainResponse {
        public String[] message;

        public TerrainResponse(String[] message) {
            this.message = message;
        }
    }
}
