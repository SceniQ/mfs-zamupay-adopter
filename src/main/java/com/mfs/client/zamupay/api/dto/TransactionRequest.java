package com.mfs.client.zamupay.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represent JSON request for creating Remittance transaction from MFS
 */
@Getter
@EqualsAndHashCode @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Represents remittance transaction request.")
public class TransactionRequest implements Serializable{

	@ApiModelProperty(notes = "MFS reference for remittance transaction", required = true)
	@NotBlank(message = "MFS reference Id should not be blank")
	private String mfsReferenceId;

	@ApiModelProperty(notes = "Primary account number for remittance transaction", required = true)
	@NotBlank(message = "Primary account number should not be blank")
	private String primaryAccountNumber;

	@ApiModelProperty(notes = "Amount for remittance transaction", required = true)
	@Setter
	@NotNull(message = "Amount should not be null")
	private Double amount;

}
