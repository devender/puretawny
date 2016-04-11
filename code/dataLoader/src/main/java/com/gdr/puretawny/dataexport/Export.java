package com.gdr.puretawny.dataexport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.config.AppConfig;
import com.gdr.puretawny.dataLoader.config.DataLoaderConfig;
import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.db.config.DBConfig;
import com.gdr.puretawny.model.DistanceFromCity;
import com.gdr.puretawny.model.Point;

@Service
public class Export {

    private final DbService dbService;

    @Autowired
    public Export(DbService dbService) {
        this.dbService = dbService;
    }

    public void export() throws IOException {
        List<Point> points = dbService.findAll();

        Path path = Paths.get("output.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            String header = "Latitude, Longitude, Within US, POI City, Distance From POI City, Is Within 500 Miles of POI City\n";
            writer.write(header);
            for (Point point : points) {
                List<DistanceFromCity> list = dbService
                        .distanceFromPointsOfInterest(point.getLatitude(), point.getLongitude());
                StringBuffer buffer = new StringBuffer();
                for (DistanceFromCity city : list) {
                    buffer
                            .append(city.getFromPoint().getLatitude()).append(',')
                            .append(city.getFromPoint().getLongitude()).append(',')
                            .append(city.isInUS()).append(',').append(city.getCity().getCity())
                            .append(',').append(city.getDistanceInMiles()).append(',')
                            .append(city.isWithin500MilesOfCity()).append('\n');
                }
                writer.write(buffer.toString());
                writer.flush();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                AppConfig.class, DBConfig.class, DataLoaderConfig.class);
        Export e = ctx.getBean(Export.class);
        e.export();
        ctx.close();
    }
}
