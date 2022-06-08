package com.mfs.client.zamupay.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
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
    @Valid
    List<PaymentOrderLines> orderLines;


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

            private String name;
            private String address;
            private String phoneNumber;
            private String idType;
            private String idNumber;
            private String country;
            private String financialInstitution;
            private String sourceOfFunds;
            private String principalActivity;

        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Recipient {

            @NotBlank(message = "Recipient name should not be blank")
            private String name;
            private String address;
            private String emailAddress;
            @NotBlank(message = "Recipient phone number should not be blank")
            private String phoneNumber;
            private String idType;
            private String idNumber;
            private String financialInstitution;
            private String institutionIdentifier;
            @NotBlank(message = "Recipient primary account number should not be blank")
            private String primaryAccountNumber;
            @NotBlank(message = "Recipient country should not be blank")
            private String country;
            private String purpose;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Transaction {

            @NotBlank(message = "Transaction amount should not be blank")
            private String amount;
            @NotBlank(message = "Transaction reference should not be blank")
            private String reference;
            @NotBlank(message = "Transaction system trace audit number should not be blank")
            private String systemTraceAuditNumber;
        }
    }


}
