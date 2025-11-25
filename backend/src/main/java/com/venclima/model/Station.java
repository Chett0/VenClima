package com.venclima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "station")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    public Station(Integer id, String name, double latitude, double longitude, String name_abbreviation, String name_link) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name_abbreviation = name_abbreviation;
        this.name_link = name_link;
    }

    @Id
    private Integer id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;
    private String name_link;

    @ManyToMany(mappedBy = "stations")
    private List<User> users;

}
