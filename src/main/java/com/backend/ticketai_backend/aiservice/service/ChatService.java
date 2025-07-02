package com.backend.ticketai_backend.aiservice.service;

import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final String system_prompt = "You are a customer support agent for customer support tickets management for a company. Your task is to based on the provided query of the user determine the category of the user query. Available categories are billing,technical and informative. If user query is related to something else then return 'other'.\n" +
            "You will be provided with the user query and you have to return the category of the query.\n" +
            "You will be provided with the user query in the following format:\n" +
            "User Query: <user_query>\n" +
            "You have to return the category in the following format:\n" +
            "Category: <category>\n" ;
    
    private HuggingfaceChatModel chatModel;

    public ChatService(HuggingfaceChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generate(String message) {
        String response = this.chatModel.call(system_prompt + "User Query: " + message);
        // Assuming the response is in the format "Category: <category>"
        if (response.startsWith("Category: ")) {
            return response.substring(10).trim(); // Extract the category
        } else {
            return "other"; // Default to 'other' if the format is unexpected
        }
    }

}
