package com.example.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/api/accounts/hello")
    public String hello2() {
        return "api/accounts/hello";
    }

    @GetMapping("/accounts/hello")
    public String hello3() {
        return "/accounts/hello";
    }
}
