package com.gdr.puretawny.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;

@RestController
@EnableAutoConfiguration
public class GisService {

    @Autowired
    private DbService dbService;

    @RequestMapping("/point/all")
    public List<Point> home() {
        return dbService.findAll();
    }
}
