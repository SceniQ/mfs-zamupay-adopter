package com.mfs.client.zamupay.persistence.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the entity responsible for storing data related to the MCCMNC master
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mncmcc_master")
public class MCCMNCMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mncmcc_id", nullable = false)
    private Integer id;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "operator_name", nullable = false)
    private String operatorName;

    @Column(name = "mcc", nullable = false)
    private String mcc;

    @Column(name = "mnc", nullable = false)
    private String mnc;

}

