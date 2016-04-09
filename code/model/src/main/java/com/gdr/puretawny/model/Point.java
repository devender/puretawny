package com.gdr.puretawny.model;

public class Point {

    private String country;
    private String city;
    private double latitude;
    private double longitude;
    private int    id;

    public Point() {
    }

    public Point(String country, String city, double latitude, double longitude) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Point [country=" + country + ", city=" + city + ", latitude=" + latitude + ", longitude=" + longitude + ", id=" + id + "]";
    }

}
