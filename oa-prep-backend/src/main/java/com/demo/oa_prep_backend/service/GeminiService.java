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

    private String buildPrompt(String topic, int count) {
        return String.format(
                "Generate %d multiple-choice questions about %s. " +
                        "For each question, provide: " +
                        "1. The question text. " +
                        "2. An array of 4 options. " +
                        "3. The zero-based index of the correct answer. " +
                        "Return the result *only* as a valid JSON array of objects. " +
                        "Each object in the array must have these exact keys: " +
                        "\"questionText\" (string), \"options\" (array of 4 strings), \"correctAnswerIndex\" (number). " +
                        "Do not include any other text, explanations, or markdown formatting outside of the JSON array.",
                count, topic
        );
    }

    private Mono<List<Question>> extractAndParseQuestions(JsonNode responseNode) {
        try {
            String textResponse = responseNode
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            List<Question> questions = objectMapper.readValue(textResponse, new TypeReference<>() {});
            return Mono.just(questions);
        } catch (JsonProcessingException | NullPointerException e) {
            return Mono.error(new RuntimeException("Failed to parse questions from Gemini response.", e));
        }
    }
}

