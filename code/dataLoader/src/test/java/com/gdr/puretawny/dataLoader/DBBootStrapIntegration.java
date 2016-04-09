package com.gdr.puretawny.dataLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdr.puretawny.config.AppConfig;
import com.gdr.puretawny.dataLoader.config.DataLoaderConfig;
import com.gdr.puretawny.db.config.DBConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { AppConfig.class, DataLoaderConfig.class,
        DBConfig.class }) })
public class DBBootStrapIntegration {

    @Autowired
    private DBBootStrap dbBootStrap;

    @Test
    public void testBoot() {
        dbBootStrap.bootStrapDb();
    }
}
