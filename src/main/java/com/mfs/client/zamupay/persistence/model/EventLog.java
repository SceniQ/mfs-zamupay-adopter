package com.mfs.client.zamupay.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity used to store logging request sent and response received from Client
 */

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "bank_event_log")
public class EventLog implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id", length = 20, nullable = false)
	private Long eventId;

	@Column(name = "mfs_reference_id", length = 50, nullable = false)
	private String mfsReferenceId;

	@Column(name = "request", columnDefinition = "MEDIUMTEXT")
	private String request;

	@Setter
	@Column(name = "response", columnDefinition = "MEDIUMTEXT")
	private String response;

	@Column(name = "service_name", length = 200)
	private String serviceName;

	@Setter
	@Column(name = "date_logged", nullable = false)
	private Date dateLogged;

}
