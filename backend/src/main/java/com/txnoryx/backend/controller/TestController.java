package com.txnoryx.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test() {
        return "routing works";
    }
}