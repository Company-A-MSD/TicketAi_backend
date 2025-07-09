package com.backend.ticketai_backend.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    private static final String system_prompt = "You are a customer support agent for customer support tickets management for a company. Your task is to based on the provided query of the user determine the category of the user query. Available categories are billing,technical and informative. If user query is related to something else then return 'other'.\n" +
            "You will be provided with the user query and you have to return the category of the query and the priority of the ticket.if the category is billig the priority should be High and if category is technical priority should be Moderate and if category is something else prority should be Low.\n" +
            "You will be provided with the user query in the following format:\n" +
            "User Query: <user_query>\n" +
            "You have to return the category in the following format:\n" +
            "{\"Category\": <category> , \"Priority\":<priority>}\n";

    private HuggingfaceChatModel chatModel;

    public ChatService(HuggingfaceChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public Map<String, String> generate(String message) {
        String response = this.chatModel.call(system_prompt + "User Query: " + message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Try to parse the response as JSON
            return mapper.readValue(response, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            // If parsing fails, return a default map
            Map<String, String> fallback = new HashMap<>();
            fallback.put("Category", "other");
            fallback.put("Priority", "Low");
            return fallback;
        }
    }

}
