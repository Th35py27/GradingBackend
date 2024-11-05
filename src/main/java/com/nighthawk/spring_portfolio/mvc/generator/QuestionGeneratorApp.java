package com.nighthawk.spring_portfolio.mvc.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping("/generate")
@CrossOrigin(origins = "*")
public class QuestionGeneratorApp {

    private static final Logger logger = LoggerFactory.getLogger(QuestionGeneratorApp.class);
    
    private static final String GROQ_API_KEY = "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"; // Your GroqCloud API key
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions"; // GroqCloud API URL

    public static void main(String[] args) {
        SpringApplication.run(QuestionGeneratorApp.class, args);
    }

    @PostMapping("/question")
    public ResponseEntity<String> generateQuestion(@RequestBody UserRequest userRequest) {
        logger.info("Received request to generate question for topic: {}", userRequest.getTopic());
        String prompt = createPrompt(userRequest);
        String generatedQuestion = callGroqAPI(prompt);
        return ResponseEntity.ok(generatedQuestion);
    }

    private String createPrompt(UserRequest userRequest) {
        StringBuilder prompt = new StringBuilder();
        if (userRequest.isMultipleChoice()) {
            prompt.append("Generate a multiple-choice question about ")
                  .append(userRequest.getTopic())
                  .append(". Ensure the question is clear and provides specific instructions.")
                  .append(" Include four options (A, B, C, D), with one correct answer.");
        } else {
            prompt.append("Generate a question about ")
                  .append(userRequest.getTopic())
                  .append(". The question should guide students to write a code block fulfilling the requirements: ")
                  .append(userRequest.getRequirements())
                  .append(". Just ask the question; don't include your descriptions.");
        }
        return prompt.toString();
    }

    private String callGroqAPI(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + GROQ_API_KEY);
        
        // Prepare request body
        String requestBody = String.format("{\"model\": \"llama3-8b-8192\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", prompt);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        // Call the API
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("choices").get(0).get("message").get("content").asText();
            } else {
                logger.error("Error calling Groq API: {}", response.getStatusCode());
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            logger.error("Exception while calling Groq API: {}", e.getMessage());
            return "Error calling Groq API.";
        }
    }
}

// Model for user input
class UserRequest {
    private String topic;
    private String requirements;
    private boolean isMultipleChoice;

    // Getters and Setters
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public boolean isMultipleChoice() { return isMultipleChoice; }
    public void setMultipleChoice(boolean multipleChoice) { isMultipleChoice = multipleChoice; }
}
