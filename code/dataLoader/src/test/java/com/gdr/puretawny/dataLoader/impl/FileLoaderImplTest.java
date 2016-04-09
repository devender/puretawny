package com.gdr.puretawny.dataLoader.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import com.gdr.puretawny.model.Point;

public class FileLoaderImplTest {

    @Test
    public void testParse() {
        Optional<Point> point = FileLoaderImpl.parseLine("ad,aixas,Aixàs,06,,42.4833333,1.4666667");
        Assert.assertTrue(point.isPresent());
        Assert.assertThat("City Name Should Match", point.get().getCity(), containsString("aixas"));
        Assert.assertThat("Country Should Match", point.get().getCountry(), containsString("ad"));
        Assert.assertThat("Latitude should match", point.get().getLatitude(), closeTo(42.4833333, 0.0));
        Assert.assertThat("Longitude should match", point.get().getLongitude(), closeTo(1.4666667, 0.0));
    }
}
