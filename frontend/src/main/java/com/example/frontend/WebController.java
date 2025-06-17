package com.example.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @Value("${api.user-service.url}")
    private String userServiceUrl;

    @Value("${api.record-service.url}")
    private String recordServiceUrl;

    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }

    @GetMapping("/api/config")
    @ResponseBody
    public Map<String, String> getApiConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("userServiceUrl", userServiceUrl);
        config.put("recordServiceUrl", recordServiceUrl);
        return config;
    }
}
