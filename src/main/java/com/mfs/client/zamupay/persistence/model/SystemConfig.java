package com.mfs.client.zamupay.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity responsible for storing necessary data for the application default
 * system values
 */

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "system_config")
public class SystemConfig implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "config_id", nullable = false)
	private int configId;

	@Column(name = "config_key", nullable = false, updatable = false, unique = true)
	private String configKey;

	@Setter
	@Column(name = "config_value", nullable = false)
	private String configValue;

}
