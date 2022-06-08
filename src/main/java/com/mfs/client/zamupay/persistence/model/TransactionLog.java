package com.mfs.client.zamupay.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "mfs_reference_id")
    private String mfsReferenceId;

    @Column(name = "payment_notes", nullable = false)
    private String paymentNotes;

    @Column(name = "remitter_phone_number")
    private String remitterPhoneNumber;

    @Column(name = "remitter_country")
    private String remitterCountry;

    @Column(name = "remitter_ccy")
    private String remitterCCY;

    @Column(name = "recipient_phone_number", nullable = false)
    private String recipientPhoneNumber;

    @Column(name = "recipient_primary_account_number", nullable = false)
    private String recipientPrimaryAccountNumber;

    @Column(name = "recipient_mccmnc", nullable = false)
    private String recipientMCCMNC;

    @Column(name = "recipient_ccy", nullable = false)
    private String recipientCCY;

    @Column(name = "recipient_country", nullable = false)
    private String recipientCountry;

    @Column(name = "transaction_routeId", nullable = false)
    private String transactionRouteId;

    @Column(name = "transaction_Channel_type", nullable = false)
    private int transactionChannelType;

    @Column(name = "remarks", nullable = false)
    private String remarks;

    @Column(name = "system_conversation_id", nullable = false)
    private String systemConversationId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "result_code", nullable = false)
    private String resultCode;

    @Column(name = "result_desc", nullable = false)
    private String resultDesc;

    @Column(name = "transaction_amount", nullable = false)
    private String transactionAmount;

    @Column(name = "date_logged", nullable = false)
    private Date dateLogged;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "transactionLog", orphanRemoval = true)
    private TransactionDetail transactionDetail;
}



