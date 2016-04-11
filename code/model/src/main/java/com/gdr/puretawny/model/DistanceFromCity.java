package com.gdr.puretawny.model;

public class DistanceFromCity {
    private double distanceInMiles;
    private Point  city;
    private Point  fromPoint;

    public DistanceFromCity() {

    }

    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(double distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public Point getCity() {
        return city;
    }

    public void setCity(Point city) {
        this.city = city;
    }

    public Point getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(Point fromPoint) {
        this.fromPoint = fromPoint;
    }

    

}
