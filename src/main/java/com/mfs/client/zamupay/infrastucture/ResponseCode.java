package com.mfs.client.zamupay.infrastucture;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ENUM represents all Response code we are going to receive from Client.
 *
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
	
	TRANSACTION_ID_NOT_EXIST("404", "Transaction with given reference id does not exist."),
    REQUIRED_MISSING_FIELD("406", "Required field is missing"),
    DUPLICATE_TRANSACTION("406", "Duplicate transaction"),
    INVALID_BANK_BRANCH_CODE("406", "Invalid Bank Branch Code"),
    COUNTRY_CODE_NOT_EXIST("404", "Could not find country code for the given primary account number."),
    NO_COUNTRY_FOUND("400", "No country found given phone number"),
    CLIENT_EXCEPTION("500", "System internal error");

	private String code;
	private String description;

	/**
	 * Get Response code from code
	 * @param code code value
	 * @return ResponseCode
	 */
	public static ResponseCode getResponseCode(String code) {
		for (ResponseCode rc : values()) {
			if (rc.getCode().equals(code)) {
				return rc;
			}
		}
		throw new IllegalArgumentException("Not found any response code for " + code);

	}

}
