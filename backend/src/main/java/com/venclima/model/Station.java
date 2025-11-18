package com.venclima.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    @Id
    private Integer id;
    private String name;
    private double latitude;
    private double longitude;
    private String name_abbreviation;
    public String name_link;

}
