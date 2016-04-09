package com.gdr.puretawny.db;

import java.util.List;
import java.util.Optional;

import com.gdr.puretawny.model.Point;
import com.gdr.puretawny.model.Polygon;

public interface DbService {

    void initDB();

    void insertPoints(List<Point> points);

    void insertPoint(Point point);

    void deletePoint(Point point);

    Optional<Point> findAt(double latitude, double longitude);

    void insertPolygonsForUs(List<Polygon> usPolygons);

    boolean isPointInUs(Point point);

    List<Point> findAll();
    
    void insertPointsOfInterest(List<Point> points);
}
