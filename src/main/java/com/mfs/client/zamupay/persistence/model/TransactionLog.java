package com.mfs.client.zamupay.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity used to store transaction request details sent and response received from Client
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_log_id", nullable = false)
    private int transactionLogId;

    @Column(name = "mfs_reference_id", nullable = false)
    private String mfsReferenceId;

    @Column(name = "payment_notes", nullable = false)
    private String paymentNotes;

    @Column(name = "recipient_phone_number", nullable = false)
    private String recipientPhoneNumber;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "dateLogged", nullable = false)
    private Date dateLogged;
}

