package com.gdr.puretawny.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(value = "/api")
public class HealthService {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public String isAlive() {
        return "isAlive ";
    }
}
