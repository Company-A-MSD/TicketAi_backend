package com.backend.ticketai_backend.aiservice.service;

import java.util.Map;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    // private final HuggingfaceChatModel chatModel;
    private static final String system_prompt = "You are a customer support agent for customer support tickets management for a company. Your task is to based on the provided query of the user determine the category of the user query. Available categories are billing,technical and general. If user query is related to something else then return 'other'.\n" +
            "You will be provided with the user query and you have to return the category of the query.\n" +
            "You will be provided with the user query in the following format:\n" +
            "User Query: <user_query>\n" +
            "You have to return the category in the following format:\n" +
            "Category: <category>\n" ;
            
    private HuggingfaceChatModel chatModel;

    public ChatController(HuggingfaceChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map<String, Object> generate(@RequestParam(value = "message", defaultValue = "general") String message) {
        return Map.of("generation", this.chatModel.call(system_prompt + "User Query: " + message));
    }

    // private final AzureOpenAiChatModel chatModel;

    // public ChatController(AzureOpenAiChatModel chatModel) {
    //     this.chatModel = chatModel;
    // }

    // @GetMapping("/ai/generate-azure")
    // public Map<String, Object> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    //     return Map.of("generation", this.chatModel.call(message));
    // }

}