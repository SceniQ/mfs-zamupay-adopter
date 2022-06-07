package com.mfs.client.zamupay.api.dto;

import lombok.*;

/**
 * Represents generic response for all remittance transaction request
 *
 * @param <T> represents Response body
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MFSResponse<T> {
	
	int statusCode;
	T response;

}
