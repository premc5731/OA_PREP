package com.demo.oa_prep_backend.controller;
import com.demo.oa_prep_backend.dto.McqRequest;
import com.demo.oa_prep_backend.dto.Question;
import com.demo.oa_prep_backend.service.GeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class McqController {

    // --- NEW: Added a proper logger ---
    private static final Logger logger = LoggerFactory.getLogger(McqController.class);

    private final GeminiService geminiService;

    public McqController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate-mcqs")
    public Mono<ResponseEntity<List<Question>>> generateMcqs(@RequestBody McqRequest request) {
        return geminiService.getMcqs(request.getTopic(), request.getCount())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    // --- MODIFIED: More detailed error logging ---
                    // We will now log the full error to the console using the logger.
                    logger.error("An exception occurred in the Gemini service call", e);

                    // We will ALSO print the full stack trace to be 100% sure we see it.
                    System.err.println("--- Full Stack Trace for Gemini API Error ---");
                    e.printStackTrace(System.err);
                    System.err.println("---------------------------------------------");

                    return Mono.just(ResponseEntity.status(500).build());
                });
    }
}

