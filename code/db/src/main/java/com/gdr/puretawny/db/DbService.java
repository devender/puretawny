package com.gdr.puretawny.db;

import java.util.List;

import com.gdr.puretawny.model.Point;

public interface DbService {

    void insertPoints(List<Point> points);

    void insertPoint(Point point);
}
