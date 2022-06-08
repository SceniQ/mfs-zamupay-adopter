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
 * This is the Country master entity responsible for storing details about the
 * country pertaining a certain request
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country_master")
public class CountryMaster implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false)
    private Integer countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "phone_code")
    private int phoneCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "country_code_alpha3")
    private String countryCodeAlpha3;

    @Column(name = "numeric_code")
    private String numericCode;
}

