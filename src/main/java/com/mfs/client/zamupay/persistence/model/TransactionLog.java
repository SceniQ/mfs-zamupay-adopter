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
@Table(name = "bank_transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_log_id", nullable = false)
    private int transactionLogId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "mfs_reference_id", nullable = false)
    private String mfsReferenceId;
    
    @Column(name = "receiver_msisdn", nullable = false)
    private String receiverMsisdn;

    @Column(name = "primary_account_number", nullable = false)
    private String primaryAccountNumber;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "result_code")
    private int resultCode;

    @Column(name = "result_desc")
    private String resultDesc;

    @Column(name = "dateLogged", nullable = false)
    private Date dateLogged;
}

