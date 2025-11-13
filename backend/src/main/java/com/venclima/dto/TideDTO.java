package com.venclima.dto;

import java.time.LocalDateTime;

public class TideDTO {

    private long id;
    private LocalDateTime date;
    private double level;
    private long stationId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }

    public long getStationId() { return stationId; }
    public void setStationId(long stationId) { this.stationId = stationId; }
}
