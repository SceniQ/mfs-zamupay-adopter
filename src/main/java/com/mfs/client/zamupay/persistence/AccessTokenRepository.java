package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

/**
 * Handles CRUD operations for access token details
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    /**
     * Retrieves valid accessToken from access_token table by checking if the current time falls between
     * the created & expired time of token
     *
     * @param currentTime the current time
     * @return access token object
     */
    @Query(value = "SELECT * FROM access_token WHERE created_on <= :currentTime AND expires_in >= :currentTime ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<AccessToken> getByCreatedDateBetween(@Param("currentTime") Date currentTime);
}
