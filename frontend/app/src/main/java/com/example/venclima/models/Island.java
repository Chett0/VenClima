package com.example.venclima.models;
public class Island {

    private Integer id;
    private String name;
    private Integer minLevel;
    private Integer maxLevel;
    private String district;
    private Integer stationId;
    private String geoJson;

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getMinLevel() { return minLevel; }
    public Integer getMaxLevel() { return maxLevel; }
    public String getDistrict() { return district; }
    public Integer getStationId() { return stationId; }
    public String getGeoJson() { return geoJson; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMinLevel(Integer minLevel) { this.minLevel = minLevel; }
    public void setMaxLevel(Integer maxLevel) { this.maxLevel = maxLevel; }
    public void setDistrict(String district) { this.district = district; }
    public void setStationId(Integer stationId) { this.stationId = stationId; }
    public void setGeoJson(String geoJson) { this.geoJson = geoJson; }
}
