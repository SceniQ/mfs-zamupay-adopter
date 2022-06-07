package com.mfs.client.zamupay.api.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represent JSON response for all remittance transaction operation to MFS
 */
@Getter @Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable{

	@NotBlank(message = "MFS reference Id should not be blank")
	private String mfsReferenceId;
	
	@NotBlank(message = "Transaction Id should not be blank")
	private String transactionId;

	private String primaryAccountNumber;

	private Double amount;
	
	private int responseCode;
	
	private String responseDesc;
}
