package com.gdr.puretawny.dataLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.gdr.puretawny.model.Point;
import com.gdr.puretawny.model.Polygon;

public interface FileLoader {
    List<Point> loadFromFile(Path filePath) throws IOException;
    
    List<Polygon> loadPolygons(Path filePath) throws IOException,ParseException;

}
