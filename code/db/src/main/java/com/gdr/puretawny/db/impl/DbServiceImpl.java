package com.gdr.puretawny.db.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gdr.puretawny.db.DbService;
import com.gdr.puretawny.model.Point;
import com.google.common.base.Strings;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

@Service
public class DbServiceImpl implements DbService {
    private static final Logger   LOGGER     = LoggerFactory.getLogger(DbServiceImpl.class);

    private static final String   DB_NAME    = "puretawny";
    private static final String   TABLE_NAME = "geo";

    private final String          host;
    private final int             port;
    public static final RethinkDB r          = RethinkDB.r;

    @Autowired
    public DbServiceImpl(@Value("${rethinkdb.host}") String host, @Value("${rethinkdb.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void initDB() {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Boolean dbExists = r.dbList().contains(DB_NAME).run(connection);

            if (dbExists) {
                LOGGER.info("Drop DB..");
                r.dbDrop(DB_NAME).run(connection);
            }

            LOGGER.info("Create DB");
            r.dbCreate(DB_NAME).run(connection);
            LOGGER.info("Create Table");
            r.db(DB_NAME).tableCreate(TABLE_NAME).run(connection);
        }
    }

    @Override
    public void insertPoints(final List<Point> points) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            for (Point point : points) {
                if (point.getLatitude() > 90 || point.getLatitude() < -90) {
                    System.out.println("what");
                }
                r.db(DB_NAME).table(TABLE_NAME).insert(r.hashMap("country", point.getCountry()).with("city", point.getCity()).with("location", r.point(point.getLongitude(), point.getLatitude())))
                        .run(connection);
            }
            LOGGER.info("Done inserting all points..creating gis index ...");
            r.db(DB_NAME).table(TABLE_NAME).indexCreate("location").optArg("geo", true).run(connection, OptArgs.of("durability", "soft").with("noreply", "True"));
            LOGGER.info("..done.");
        }
    }

    @Override
    public void insertPoint(final Point point) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            r.db(DB_NAME).table(TABLE_NAME).insert(r.hashMap("country", point.getCountry()).with("city", point.getCity()).with("location", r.point(point.getLongitude(),point.getLatitude())))
                    .run(connection);
        }
    }

    @Override
    public void deletePoint(final Point point) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            r.db(DB_NAME).table(TABLE_NAME).filter(r.hashMap("country", point.getCountry()).with("city", point.getCity()).with("location", r.point(point.getLongitude(),point.getLatitude()))).delete()
                    .run(connection);
        }
    }

    @Override
    public Optional<Point> findAt(double latitude, double longitude) {
        Optional<Point> point = Optional.empty();

        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Cursor<HashMap> cursor = r.db(DB_NAME).table(TABLE_NAME).filter(r.hashMap("location", r.point(longitude,latitude))).limit(1).run(connection);

            for (HashMap doc : cursor) {
                try {
                    JSONObject j = (JSONObject) doc.get("location");
                    JSONArray o = (JSONArray) j.get("coordinates");
                    String country = (String) doc.get("country");
                    String city = (String) doc.get("city");
                    Double latitudeN = (Double) o.get(0);
                    Double logitudeN = (Double) o.get(1);
                    if (null != country && null != city && null != latitudeN && null != logitudeN) {
                        point = Optional.of(new Point(country, city, latitude, logitudeN));
                    }
                } catch (Exception e) {
                    LOGGER.error("unable to map doc to pojo", e);
                }

            }
        }
        return point;
    }

}
