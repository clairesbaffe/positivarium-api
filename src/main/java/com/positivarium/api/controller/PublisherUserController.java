package com.positivarium.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/publisher/users")
public class PublisherUserController {

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("This is ok, you can get in");
    }

}
