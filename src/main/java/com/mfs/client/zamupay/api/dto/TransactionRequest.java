package com.mfs.client.zamupay.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * Represent JSON request for creating Remittance transaction from MFS
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest implements Serializable {

    @NotBlank(message = "MFS reference ID should not be blank")
    private String mfsReferenceId;
    @NotBlank(message = "Payment notes should not be blank")
    private String paymentNotes;
    @NotBlank(message = "Payment order lines should not be blank")
    private PaymentOrderLines paymentOrderLines;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentOrderLines {

        @ApiModelProperty(notes = "Remitter details")
        private Remitter remitter;

        @ApiModelProperty(notes = "Recipient details")
        private Recipient recipient;

        @ApiModelProperty(notes = "Transaction details", required = true)
        @NotNull(message = "Transaction information should not be blank")
        private Transaction transaction;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Remitter {

            private String remitterName;
            private String remitterAddress;
            private String remitterPhoneNumber;
            private String remitterIdType;
            private String remitterIdNumber;
            private String remitterCountry;
            private String remitterCCY;
            private String remitterFinancialInstitution;
            private String remitterSourceOfFunds;
            private String remitterPrincipalActivity;

            private String remitterDateOfBirth;
            private String remitterIdIssueDate;
            private String remitterIdIssuePlace;
            private String remitterIdExpiryDate;
            private String remitterNationality;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Recipient {

            @NotBlank(message = "Recipient name should not be blank")
            private String recipientName;
            private String recipientAddress;
            private String recipientEmailAddress;
            @NotBlank(message = "Recipient phone number should not be blank")
            private String recipientPhoneNumber;
            private String recipientIdType;
            private String recipientIdNumber;
            private String recipientFinancialInstitution;
            private String recipientInstitutionIdentifier;
            @NotBlank(message = "Recipient primary account number should not be blank")
            private String recipientPrimaryAccountNumber;
            private String recipientMCCMNC;
            private String recipientCCY;
            @NotBlank(message = "Recipient country should not be blank")
            private String recipientCountry;
            private String recipientPurpose;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Transaction {

            private String transactionRouteId;
            private String transactionChannelType;
            @NotBlank(message = "Transaction amount should not be blank")
            private String transactionAmount;
            @NotBlank(message = "Transaction reference should not be blank")
            private String transactionReference;
            private String transactionSystemTraceAuditNumber;
        }
    }


}
