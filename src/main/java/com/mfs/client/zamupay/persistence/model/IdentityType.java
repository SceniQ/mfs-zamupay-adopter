package com.mfs.client.zamupay.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity used to store possible values for identity
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "identity_type")
public class IdentityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "id_type", nullable = false)
    private String idType;
    @Column(name = "id_value", nullable = false)
    private String idValue;

}
