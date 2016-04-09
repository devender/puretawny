package com.gdr.puretawny.dataLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.gdr.puretawny.model.Point;

public interface FileLoader {
    List<Point> loadFromFile(Path filePath) throws IOException;

}
