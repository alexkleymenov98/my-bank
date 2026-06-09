package com.example.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/api/cash/hello")
    public String hello2() {
        return "api/cash/hello";
    }

    @GetMapping("/accounts/hello")
    public String hello3() {
        return "/cash/hello";
    }
}
