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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdr.puretawny.dataLoader.FileLoader;
import com.gdr.puretawny.model.Point;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;

public class FileLoaderImpl implements FileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderImpl.class);

    public FileLoaderImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<Optional<Point>> loadFromFile(final Path filePath) throws IOException {

        List<Optional<Point>> list = new ArrayList<>(0);
        
        // NOTE this is a stream that is loaded lazily
        // try with resources will ensure the stream is closed
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.ISO_8859_1)) {

            list =lines.skip(1). // skip header
                    map(line -> parseLine(line)). // convert line to point
                    filter(optional -> optional.isPresent()). // remove empty
                    collect(Collectors.toList()); // make list

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
        if (!Strings.isNullOrEmpty(parts[Latitude_Index]) && Doubles.tryParse(parts[Latitude_Index]) != null) {
            latitude = Doubles.tryParse(parts[Latitude_Index]);
        }

        Double longitude = null;
        if (!Strings.isNullOrEmpty(parts[Longitude_Index]) && Doubles.tryParse(parts[Longitude_Index]) != null) {
            longitude = Doubles.tryParse(parts[Longitude_Index]);
        }

        if (null != country && null != city && null != latitude && null != longitude) {
            point = Optional.of(new Point(country, city, latitude, longitude));
        }
        return point;
    }
}
