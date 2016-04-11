package com.gdr.puretawny.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;

@RestController
@EnableAutoConfiguration
public class GisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GisService.class);

    @Autowired
    private DbService           dbService;

    @RequestMapping("/point/all")
    public List<Point> home() {
        return dbService.findAll();
    }

    @RequestMapping("/point/longitude/{longitude:.+}/latitude/{latitude:.+}")
    public ResponseEntity lookup(@PathVariable double longitude, @PathVariable double latitude) {
        LOGGER.debug("longitude {}, latitude {} ", longitude, latitude);

        Optional<Point> o = dbService.findAt(latitude, longitude);
        if (o.isPresent()) {
            return new ResponseEntity<Point>(o.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/point/longitude/{longitude:.+}/latitude/{latitude:.+}")
    public ResponseEntity add(@PathVariable double longitude, @PathVariable double latitude) {
        LOGGER.debug("adding longitude {}, latitude {} ", longitude, latitude);
        boolean b = dbService.insertPoint(new Point(latitude, longitude));
        if (b) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        }
    }
}
