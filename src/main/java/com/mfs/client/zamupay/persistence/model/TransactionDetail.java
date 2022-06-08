package com.mfs.client.zamupay.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

/**
 * Entity used to store transaction details
 */
@Entity
@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_detail")
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_detail_id", nullable = false)
    private Long transactionDetailId;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "transaction_log_id")
    private TransactionLog transactionLog;

    @Column(name = "remitter_name", nullable = false)
    private String remitterName;

    @Column(name = "remitter_address", nullable = false)
    private String remitterAddress;

    @Column(name = "remitter_id_type", nullable = false)
    private String remitterIdType;

    @Column(name = "remitter_id_number", nullable = false)
    private String remitterIdNumber;

    @Column(name = "remitter_financial_institution", nullable = false)
    private String remitterFinancialInstitution;

    @Column(name = "remitter_source_of_funds", nullable = false)
    private String remitterSourceOfFunds;

    @Column(name = "remitter_principal_activity", nullable = false)
    private String remitterPrincipalActivity;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_address", nullable = false)
    private String recipientAddress;

    @Column(name = "recipient_email_address", nullable = false)
    private String recipientEmailAddress;

    @Column(name = "recipient_id_type", nullable = false)
    private String recipientIdType;

    @Column(name = "recipient_id_number", nullable = false)
    private String recipientIdNumber;

    @Column(name = "recipient_financial_institution", nullable = false)
    private String recipientFinancialInstitution;

    @Column(name = "recipient_purpose", nullable = false)
    private String recipientPurpose;

    @Column(name = "transaction_reference", nullable = false)
    private String transactionReference;

    @Column(name = "transaction_system_trace_audit_number", nullable = false)
    private String transactionSystemTraceAuditNumber;

}
