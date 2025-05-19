package com.sk.skala.myapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> helloWorld() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<Map<String, String>> getMethodName(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();
        response.put("name", name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String hello() {
        return "Hello World";
    }
}
