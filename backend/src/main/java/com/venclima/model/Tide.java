package com.venclima.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Tide {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private LocalDateTime date;
    private double level;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    public Tide() {
        super();
    }
    public Tide(long id, LocalDateTime date, double level, Station station) {
        this.id = id;
        this.date = date;
        this.level = level;
        this.station = station;
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public double getLevel() { return level; }
    public void setLevel(double level) { this.level = level; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

}
