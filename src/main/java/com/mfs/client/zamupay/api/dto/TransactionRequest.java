package com.mfs.client.zamupay.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represent JSON request for creating Remittance transaction from MFS
 */
@Data
@EqualsAndHashCode @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest implements Serializable{

	@NotBlank(message = "MFS reference ID should not be blank")
	private String mfsReferenceId;
	@NotBlank(message = "Payment notes should not be blank")
	private String paymentNotes;

	@ApiModelProperty(notes = "Remitter details", required = true)
	@NotNull(message = "Remitter information should not be blank")
	private Remitter remitter;
	@ApiModelProperty(notes = "Recipient details", required = true)
	@NotNull(message = "Recipient information should not be blank")
	private Recipient recipient;
	@ApiModelProperty(notes = "Transaction details", required = true)
	@NotNull(message = "Transaction information should not be blank")
	private Transaction transaction;

	public static class Remitter{}
	@Data
	@EqualsAndHashCode @ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Recipient{

		private String phoneNumber;
	}
	public static class Transaction{}

}
