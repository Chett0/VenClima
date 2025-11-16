package com.venclima.dto;

public class StationDTO {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getName_abbreviation() { return name_abbreviation; }
    public void setName_abbreviation(String name_abbreviation) { this.name_abbreviation = name_abbreviation; }

}
