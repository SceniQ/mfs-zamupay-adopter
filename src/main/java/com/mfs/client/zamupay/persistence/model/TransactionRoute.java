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
 * Entity used to store possible transaction routes values
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_route")
public class TransactionRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "route_id", nullable = false)
    private String routeId;
    @Column(name = "channel_type", nullable = false)
    private int channelType;
    @Column(name = "transaction_type_id", nullable = false)
    private String transactionTypeId;

}

