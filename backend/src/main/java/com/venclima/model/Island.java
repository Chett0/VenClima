package com.venclima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "islands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Island {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minLevel;

    @Column(nullable = false)
    private Integer maxLevel;

    @Column(nullable = false)
    private String district;

    private LocalDateTime lastNotified;

    //@Type(value = "jts_geometry")
    private Polygon area;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

    @ManyToMany(mappedBy = "islands", fetch = FetchType.EAGER)
    private List<User> users;

}
