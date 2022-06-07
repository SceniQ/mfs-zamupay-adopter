package com.mfs.client.zamupay.persistence;

import com.mfs.client.zamupay.persistence.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for performing all database operations on event log entity
 */
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
