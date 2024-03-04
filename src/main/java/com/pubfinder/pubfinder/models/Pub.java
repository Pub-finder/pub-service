package com.pubfinder.pubfinder.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;


@Entity
@Data
public class Pub {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private Double[] geocode;
    @Column()
    private String open;
    @Column(nullable = false)
    private String location;
    @Column()
    private String description;

    public Pub() {
    }

    public Pub(UUID id, String name, Double[] geocode, String open, String location, String description) {
        this.id = id;
        this.name = name;
        this.geocode = geocode;
        this.open = open;
        this.location = location;
        this.description = description;
    }

    public Pub(String name, Double[] geocode, String open, String location, String description) {
        this.name = name;
        this.geocode = geocode;
        this.open = open;
        this.location = location;
        this.description = description;
    }
}
