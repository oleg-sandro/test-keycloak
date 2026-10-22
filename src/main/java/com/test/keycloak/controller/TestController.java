package com.test.keycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/public")
    public String testPermission() {
        return "Public endpoint: OK";
    }

    @GetMapping("/private")
    public String testAuthentication() {
        return "Private endpoint: OK";
    }

    @GetMapping("/other")
    public String testDenial() {
        return "Other endpoint: OK";
    }
}
