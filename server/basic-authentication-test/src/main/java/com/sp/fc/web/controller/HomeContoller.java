package com.sp.fc.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeContoller {

    @GetMapping("/greeting")
    public String greeting() {
        return "hello";
    }

}
