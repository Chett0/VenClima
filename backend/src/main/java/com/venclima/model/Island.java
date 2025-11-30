package com.venclima.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Polygon;


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

    private String district;

    //@Type(value = "jts_geometry")
    private Polygon area;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station station;

}
