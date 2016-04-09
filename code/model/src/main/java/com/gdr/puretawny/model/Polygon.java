package com.gdr.puretawny.model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points = new ArrayList<Point>();

    public Polygon() {
    }

    public Polygon addPoint(Point point) {
        this.points.add(point);
        return this;
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Point point : points) {
            sb.append('{').append(point.getLatitude()).append(',').append(point.getLongitude())
                    .append('}').append(',');
        }
        sb.append(']');
        return sb.toString();
    }
}
