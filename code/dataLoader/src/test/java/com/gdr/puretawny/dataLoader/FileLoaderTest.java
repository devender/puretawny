package com.gdr.puretawny.dataLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import com.gdr.puretawny.dataLoader.impl.FileLoaderImpl;
import com.gdr.puretawny.model.Point;

public class FileLoaderTest {

    @Test
    public void loadData() throws URISyntaxException, IOException {
        FileLoader fileLoader = new FileLoaderImpl();
        // you can find world cities pop - 1000.txt in the src/test/resources
        Path path = Paths.get(ClassLoader.getSystemResource("worldcitiespop-1000.txt").toURI());
        List<Optional<Point>> list = fileLoader.loadFromFile(path);
        Assert.assertThat(list, is(not(empty())));
        Assert.assertThat(list, hasSize(999)); // file has header
    }
}
