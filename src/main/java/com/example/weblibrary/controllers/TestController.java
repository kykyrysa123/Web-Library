package com.example.weblibrary.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "pong - Web Library работает! " + new java.util.Date();
    }

    @GetMapping("/")
    public String home() {
        return "Добро пожаловать в Web Library API!";
    }
}