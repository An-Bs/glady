package com.glady.backend.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class VoutcherDeposit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private Integer companyId;

    private Integer userId; // the employee can change a company and still have a valid  voutcher

    private Float amount;

    private Date expirationDate;


    public VoutcherDeposit() {
        // Do nothing
    }
}
