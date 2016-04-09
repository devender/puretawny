package com.gdr.puretawny.db;

import java.util.List;
import java.util.Optional;

import com.gdr.puretawny.model.Point;

public interface DbService {

    void initDB();

    void insertPoints(List<Point> points);

    void insertPoint(Point point);

    void deletePoint(Point point);

    Optional<Point> findAt(double latitude, double longitude);
}
