package com.gdr.puretawny.dataLoader.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.dataLoader.DBBootStrap;
import com.gdr.puretawny.dataLoader.FileLoader;
import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;
import com.gdr.puretawny.model.Polygon;

@Service
public class DbBootStrapImpl implements DBBootStrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBBootStrap.class);

    private final FileLoader    fileLoader;
    private final DbService     dbService;
    private final String        sampleCitiesFile;
    private final String        usPolygonsFile;

    @Autowired
    public DbBootStrapImpl(FileLoader fileLoader,
            @Value("${data.cities.filePath}") String sampleCitiesFile, DbService dbService,
            @Value("${data.us_polygons.filePath}") String usPolygonsFile) {
        this.fileLoader = fileLoader;
        this.sampleCitiesFile = sampleCitiesFile;
        this.dbService = dbService;
        this.usPolygonsFile = usPolygonsFile;
    }

    @Override
    public void bootStrapDb() {
        try {
            dbService.initDB();

            Path path = Paths.get(ClassLoader.getSystemResource(sampleCitiesFile).toURI());
            LOGGER.info("Reading data from file {}", path.toString());
            List<Point> points = fileLoader.loadFromFile(path);
            LOGGER.info("Read {} points ", points.size());

            dbService.insertPoints(points);

            LOGGER.info("Reading US polygon data");
            Path usPolygonsPath = Paths.get(ClassLoader.getSystemResource(usPolygonsFile).toURI());
            List<Polygon> usPolygons = fileLoader.loadPolygons(usPolygonsPath);
            LOGGER.info("Read {} polygons for US...inserting", usPolygons.size());
            dbService.insertPolygonsForUs(usPolygons);

        } catch (IOException | ParseException | URISyntaxException e) {
            LOGGER.error("Unable to read file", e);
        }

    }
}
