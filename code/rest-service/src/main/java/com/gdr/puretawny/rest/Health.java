package com.gdr.puretawny.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Health {

    @RequestMapping("/")
    public String home() {
        return "isAlive";
    }

}
