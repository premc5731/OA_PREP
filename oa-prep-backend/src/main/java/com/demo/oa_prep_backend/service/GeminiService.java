package com.demo.oa_prep_backend.service;
import com.demo.oa_prep_backend.dto.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    // --- MODIFIED: Removed @Value annotations from fields ---
    private final String apiKey;
    private final String apiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // --- MODIFIED: Injected api key and url directly into the constructor ---
    public GeminiService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.url}") String apiUrl
    ) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.objectMapper = objectMapper;
        // The apiUrl is now guaranteed to be available when building the WebClient
        this.webClient = webClientBuilder.baseUrl(this.apiUrl).build();
    }

    public Mono<List<Question>> getMcqs(String topic, int count) {
        String prompt = buildPrompt(topic, count);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        return webClient.post()
                .uri("?key=" + this.apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(this::extractAndParseQuestions)
                .onErrorMap(e -> new RuntimeException("Failed to call Gemini API", e));
    }

    
}

