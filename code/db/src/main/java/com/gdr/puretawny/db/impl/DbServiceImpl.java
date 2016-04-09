package com.gdr.puretawny.db.impl;

import java.util.ArrayList;
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
import com.gdr.puretawny.model.Polygon;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.Arguments;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

@Service
public class DbServiceImpl implements DbService {
    private static final Logger   LOGGER                   = LoggerFactory
            .getLogger(DbServiceImpl.class);

    private static final String   DB_NAME                  = "puretawny";
    private static final String   SAMPLE_POINTS_TABLE_NAME = "geo";
    private static final String   US_POLYGONS_TABLE_NAME   = "us";

    private final String          host;
    private final int             port;
    public static final RethinkDB r                        = RethinkDB.r;

    @Autowired
    public DbServiceImpl(@Value("${rethinkdb.host}") String host,
            @Value("${rethinkdb.port}") int port) {
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
            LOGGER.info("Create Tables");
            r.db(DB_NAME).tableCreate(SAMPLE_POINTS_TABLE_NAME).run(connection);
            r.db(DB_NAME).tableCreate(US_POLYGONS_TABLE_NAME).run(connection);
        }
    }

    @Override
    public void insertPolygonsForUs(final List<Polygon> usPolygons) {

        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            for (Polygon polygon : usPolygons) {
                Arguments args = new Arguments();
                for (Point point : polygon.getPoints()) {
                    args.coerceAndAdd(r.array(point.getLongitude(), point.getLatitude()));
                }
                r.db(DB_NAME).table(US_POLYGONS_TABLE_NAME)
                        .insert(r.hashMap("poly", new com.rethinkdb.gen.ast.Polygon(args)))
                        .run(connection);
            }
            LOGGER.info("Done inserting all polygons");
            r.db(DB_NAME).table(US_POLYGONS_TABLE_NAME).indexCreate("area").optArg("geo", true)
                    .run(connection, OptArgs.of("durability", "soft"));
        }
    }

    @Override
    public void insertPoints(final List<Point> points) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            points.parallelStream().forEach(point -> {
                r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME)
                        .insert(r.hashMap("country", point.getCountry())
                                .with("city", point.getCity()).with("location",
                                        r.point(point.getLongitude(), point.getLatitude())))
                        .run(connection, OptArgs.of("durability", "soft"));
            });
            LOGGER.info("Done inserting all points..creating gis index ...");
            r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME).indexCreate("location")
                    .optArg("geo", true).run(connection, OptArgs.of("durability", "soft"));
            LOGGER.info("..done.");
        }
    }

    @Override
    public void insertPoint(final Point point) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME)
                    .insert(r.hashMap("country", point.getCountry()).with("city", point.getCity())
                            .with("location", r.point(point.getLongitude(), point.getLatitude())))
                    .run(connection);
        }
    }

    @Override
    public List<Point> findAll() {
        List<Point> list = new ArrayList<>();
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Cursor<HashMap> cursor = r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME).run(connection);

            for (HashMap doc : cursor) {
                try {
                    JSONObject j = (JSONObject) doc.get("location");
                    JSONArray o = (JSONArray) j.get("coordinates");
                    String country = (String) doc.get("country");
                    String city = (String) doc.get("city");

                    Double latitudeN = 0.0;
                    Double logitudeN = 0.0;

                    if (o.get(1) instanceof Long) {
                        latitudeN = ((Long) o.get(1)).doubleValue();
                    } else {
                        latitudeN = (Double) o.get(1);
                    }
                    if (o.get(0) instanceof Long) {
                        logitudeN = ((Long) o.get(0)).doubleValue();
                    } else {
                        logitudeN = (Double) o.get(0);
                    }

                    if (null != country && null != city && null != latitudeN && null != logitudeN) {
                        list.add(new Point(country, city, latitudeN, logitudeN));
                    }
                } catch (Exception e) {
                    LOGGER.error("unable to map doc to pojo " + doc, e);
                }
            }
        }
        return list;
    }

    @Override
    public void deletePoint(final Point point) {
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME)
                    .filter(r.hashMap("country", point.getCountry()).with("city", point.getCity())
                            .with("location", r.point(point.getLongitude(), point.getLatitude())))
                    .delete().run(connection);
        }
    }

    @Override
    public Optional<Point> findAt(double latitude, double longitude) {
        Optional<Point> point = Optional.empty();

        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Cursor<HashMap> cursor = r.db(DB_NAME).table(SAMPLE_POINTS_TABLE_NAME)
                    .filter(r.hashMap("location", r.point(longitude, latitude))).limit(1)
                    .run(connection);

            for (HashMap doc : cursor) {
                try {
                    JSONObject j = (JSONObject) doc.get("location");
                    JSONArray o = (JSONArray) j.get("coordinates");
                    String country = (String) doc.get("country");
                    String city = (String) doc.get("city");
                    Double latitudeN = (Double) o.get(1);
                    Double logitudeN = (Double) o.get(0);
                    if (null != country && null != city && null != latitudeN && null != logitudeN) {
                        point = Optional.of(new Point(country, city, latitudeN, logitudeN));
                    }
                } catch (Exception e) {
                    LOGGER.error("unable to map doc to pojo", e);
                }

            }
        }
        return point;
    }

    @Override
    public boolean isPointInUs(final Point point) {
        boolean intersects = false;
        try (Connection connection = r.connection().hostname(host).port(port).connect()) {
            Cursor<HashMap> o = r.db(DB_NAME).table(US_POLYGONS_TABLE_NAME).g("poly")
                    .intersects(r.point(point.getLongitude(), point.getLatitude())).run(connection);
            intersects = o.hasNext();
        }
        return intersects;
    }

}
