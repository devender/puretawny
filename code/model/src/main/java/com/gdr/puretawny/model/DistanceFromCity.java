package com.gdr.puretawny.model;

public class DistanceFromCity {
    private double  distanceInMiles;
    private Point   city;
    private Point   fromPoint;
    private boolean isWithin500MilesOfCity;
    private boolean inUS;

    public DistanceFromCity() {

    }

    public boolean isInUS() {
        return inUS;
    }

    public void setInUS(boolean inUS) {
        this.inUS = inUS;
    }

    public void setWithin500MilesOfCity(boolean isWithin500MilesOfCity) {
        this.isWithin500MilesOfCity = isWithin500MilesOfCity;
    }

    public boolean isWithin500MilesOfCity() {
        return isWithin500MilesOfCity;
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
