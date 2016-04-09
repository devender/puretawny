package com.gdr.puretawny.dataLoader.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.dataLoader.FileLoader;
import com.gdr.puretawny.model.Point;
import com.gdr.puretawny.model.Polygon;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;

@Service
public class FileLoaderImpl implements FileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderImpl.class);

    @Override
    public List<Point> loadFromFile(final Path filePath) throws IOException {

        List<Point> list = new ArrayList<>(0);

        // NOTE this is a stream that is loaded lazily
        // try with resources will ensure the stream is closed
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.ISO_8859_1)) {

            list = lines.skip(1). // skip header
                    map(line -> parseLine(line)). // convert line to point
                    filter(Optional::isPresent). // remove empty
                    map(Optional::get).collect(Collectors.toList());

        }

        return list;
    }

    @Override
    public List<Polygon> loadPolygons(final Path filePath) throws IOException, ParseException {
        List<Polygon> list = new ArrayList<>();
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.ISO_8859_1);
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(content);
        JSONArray features = (JSONArray) obj.get("features");
        for (int i = 0; i < features.size(); i++) {
            JSONObject feature = (JSONObject) features.get(i);
            JSONObject geometry = (JSONObject) feature.get("geometry");
            JSONArray multiPolygonCoordinates = (JSONArray) geometry.get("coordinates");
            for (int j = 0; j < multiPolygonCoordinates.size(); j++) {
                JSONArray c1 = (JSONArray) multiPolygonCoordinates.get(i);
                // multiPolygonCoordinates contains array of polygons
                for (int k = 0; k < c1.size(); k++) {
                    Polygon polygon = new Polygon();
                    JSONArray c2 = (JSONArray) c1.get(k);
                    for (int l = 0; l < c2.size(); l++) {
                        JSONArray c3 = (JSONArray) c2.get(l);
                        Double latitude = (Double) c3.get(1);
                        Double longitude = (Double) c3.get(0);
                        polygon.addPoint(new Point(latitude, longitude));
                    }
                    list.add(polygon);
                }
            }
        }

        return list;
    }

    private static final int Country_Index    = 0;
    private static final int City_Index       = 1;
    private static final int AccentCity_Index = 2;
    private static final int Region_Index     = 3;
    private static final int Population_Index = 4;
    private static final int Latitude_Index   = 5;
    private static final int Longitude_Index  = 6;

    static Optional<Point> parseLine(String line) {
        Optional<Point> point = Optional.empty();

        String[] parts = line.split(",");

        String country = null;
        if (!Strings.isNullOrEmpty(parts[Country_Index])) {
            country = parts[Country_Index];
        }

        String city = null;
        if (!Strings.isNullOrEmpty(parts[City_Index])) {
            city = parts[City_Index];
        }

        Double latitude = null;
        if (!Strings.isNullOrEmpty(parts[Latitude_Index])
                && Doubles.tryParse(parts[Latitude_Index]) != null) {
            latitude = Doubles.tryParse(parts[Latitude_Index]);
        }

        Double longitude = null;
        if (!Strings.isNullOrEmpty(parts[Longitude_Index])
                && Doubles.tryParse(parts[Longitude_Index]) != null) {
            longitude = Doubles.tryParse(parts[Longitude_Index]);
        }

        if (null != country && null != city && null != latitude && null != longitude) {
            point = Optional.of(new Point(country, city, latitude, longitude));
        }
        return point;
    }
}
