package com.example;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Cacheable
public class Person extends PanacheEntity {
    @Column(nullable = false)
    public String lastname;

    @Column(nullable = false)
    public String firstname;

    public LocalDate birthday;
}
