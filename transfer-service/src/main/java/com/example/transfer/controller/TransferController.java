package com.example.transfer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from transfer service!";

    }
}