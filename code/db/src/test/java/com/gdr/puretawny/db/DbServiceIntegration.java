package com.gdr.puretawny.db;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdr.puretawny.config.AppConfig;
import com.gdr.puretawny.db.config.DBConfig;
import com.gdr.puretawny.model.Point;
import static org.hamcrest.Matchers.hasSize;

/**
 * Since this requires the RethinkDB to be running making this an integration
 * test.
 * 
 * @author devender_gollapally
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { AppConfig.class, DBConfig.class }) })
public class DbServiceIntegration {

    @Autowired
    private DbService          dbService;

    private static final Point zuzumba     = new Point("zw", "zuzumba", -20.0333333, 27.9333333);
    private static final Point zvishavane  = new Point("zw", "zvishavane", -20.3333333, 30.0333333);
    private static final Point abercrombie = new Point("us", "abercrombie", 32.8486111,
            -87.1650000);
    private static final Point la          = new Point("us", "la", 34.052235, -118.243683);

    @Test
    public void testDeleteAndInsert() {
        dbService.deletePoint(zuzumba);
        dbService.deletePoint(zvishavane);
        dbService.insertPoint(zuzumba);
        dbService.insertPoint(zvishavane);
        Optional<Point> actualZuzumba = dbService.findAt(zuzumba.getLatitude(),
                zuzumba.getLongitude());

        Assert.assertTrue(actualZuzumba.isPresent());
        Assert.assertTrue(actualZuzumba.get().equals(zuzumba));
        dbService.deletePoint(zuzumba);
        dbService.deletePoint(zvishavane);
    }

    @Test
    public void isPointInUs() {
        Assert.assertTrue(dbService.isPointInUs(la));
        Assert.assertTrue(dbService.isPointInUs(abercrombie));
        Assert.assertFalse(dbService.isPointInUs(zuzumba));
        Assert.assertFalse(dbService.isPointInUs(zvishavane));
    }

    @Test
    public void loadAll() {
        List<Point> points = dbService.findAll();
        Assert.assertNotNull(points);
        Assert.assertThat(points, hasSize(10000));
    }
}
