package com.example.venclima.models;

public class IslandNotification {

    private String islandName;
    private Integer islandId;
    private Boolean isNotified;

    public IslandNotification(String islandName, Integer islandId, Boolean isNotified) {
        this.islandName = islandName;
        this.islandId = islandId;
        this.isNotified = isNotified;
    }

    public void SetIslandName(String islandName) {
        this.islandName = islandName;
    }

    public String getIslandName() {
        return this.islandName;
    }

    public void SetIslandId(Integer islandId) {
        this.islandId = islandId;
    }

    public Integer getIslandId() {
        return this.islandId;
    }

    public void SetIsNotified(Boolean isNotified) {
        this.isNotified = isNotified;
    }

    public Boolean getIsNotified() {
        return this.isNotified;
    }
}
