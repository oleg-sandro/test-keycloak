package com.test.keycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test/role")
public class RoleTestController {

    @GetMapping("/admin")
    public String testAdminAccess() {
        return "Admin role: OK";
    }

    @GetMapping("/user")
    public String testUserAccess() {
        return "User role: OK";
    }
}
