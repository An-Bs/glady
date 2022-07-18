package com.glady.backend.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
@Entity(name = "employee")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer companyId;
    private String firstName;
    private String lastName;

    public User() {
        // Do nothing
    }
}
