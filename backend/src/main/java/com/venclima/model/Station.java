package com.venclima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Station {

    @Id
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;
    public String name_link;

    public Station() {
        super();
    }

    public Station(int id, String name, double latitude, double longitude, String name_abbreviation, String name_link) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name_abbreviation = name_abbreviation;
        this.name_link = name_link;
    }

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

    public String getName_link() { return name_link; }
    public void setName_link(String name_link) { this.name_link = name_link; }
}
