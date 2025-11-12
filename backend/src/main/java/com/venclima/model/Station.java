package com.venclima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Station {

    @Id
    private long id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;

    public Station() {
        super();
    }

    public Station(long id, String name, double latitude, double longitude, String name_abbreviation) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name_abbreviation = name_abbreviation;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getName_abbreviation() { return name_abbreviation; }
    public void setName_abbreviation(String name_abbreviation) { this.name_abbreviation = name_abbreviation; }
}
