package com.gdr.puretawny.dataLoader.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.dataLoader.DBBootStrap;
import com.gdr.puretawny.dataLoader.FileLoader;
import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;

@Service
public class DbBootStrapImpl implements DBBootStrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBBootStrap.class);

    private final FileLoader    fileLoader;
    private final DbService     dbService;
    private final Path          path;

    @Autowired
    public DbBootStrapImpl(FileLoader fileLoader, @Value("${data.filePath}") String filePath, DbService dbService) {
        this.fileLoader = fileLoader;
        this.path = Paths.get(filePath);
        this.dbService = dbService;
    }

    @Override
    public void bootStrapDb() {
        try {
            dbService.initDB();

            LOGGER.info("Reading data from file {}", path.toString());
            List<Point> points = fileLoader.loadFromFile(path);
            LOGGER.info("Read {} points ", points.size());
            
            dbService.insertPoints(points);

        } catch (IOException e) {
            LOGGER.error("Unable to read file", e);
        }

    }
}
