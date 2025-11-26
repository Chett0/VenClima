package com.example.venclima.models;

public class RealTimeTide {

    private String stationName;
    private Integer tideLevel;

    public RealTimeTide(String stationName, Integer tideLevel) {
        this.stationName = stationName;
        this.tideLevel = tideLevel;
    }

    public String getStationName() {
        return stationName;
    }

    public Integer getTideLevel() {
        return tideLevel;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setTideLevel(Integer tideLevel) {
        this.tideLevel = tideLevel;
    }

}
