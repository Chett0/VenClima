package com.venclima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class TideInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private LocalDateTime date;
    private double level;

    public TideInfo() {
        super();
    }
    public TideInfo(long id, LocalDateTime date, double level) {
        this.id = id;
        this.date = date;
        this.level = level;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getLevel() {
        return level;
    }
    public void setLevel(double level) {
        this.level = level;
    }
}
