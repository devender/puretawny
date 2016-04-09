package com.gdr.puretawny.dataLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.gdr.puretawny.model.Point;

public interface FileLoader {
    List<Optional<Point>> loadFromFile(Path filePath) throws IOException;
}
