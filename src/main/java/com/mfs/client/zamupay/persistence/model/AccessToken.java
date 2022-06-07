package com.mfs.client.zamupay.persistence.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Stores access token details
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "access_token")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false)
    private Long id;
    @Column(name = "access_token", columnDefinition = "LONGTEXT", nullable = false)
    private String accessToken;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on", nullable = false)
    private Date createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expires_in", nullable = false)
    private Date expiresIn;
    @Column(name = "token_type", nullable = false)
    private String tokenType;
    @Column(name = "scope", nullable = false)
    private String scope;

}

