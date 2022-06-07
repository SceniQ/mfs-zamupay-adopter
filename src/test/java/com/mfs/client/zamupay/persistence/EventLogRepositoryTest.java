package com.mfs.client.zamupay.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import com.mfs.client.zamupay.persistence.model.EventLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;



/**
 * Tests the event log repository
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-junit.properties")
public class EventLogRepositoryTest {

	@Autowired
	EventLogRepository eventLogRepository;

	/**
	 * Success test for saving a complete event log in the event log table
	 */
	@Test
	public void test_for_saving_a_valid_log() {
		EventLog eventLog = eventLogRepository.save(eventLog());
		assertThat(eventLog).isNotNull();
		assertThat(eventLog.getEventId()).isGreaterThan(0);
		assertThat(eventLog.getServiceName()).isEqualTo("MFS Client request");
	}

	/**
	 * Success test for updating an existing event log
	 */
	@Test
	public void test_for_updating_event_log() {
		EventLog eventLogNoResponse =  EventLog.builder()
				.mfsReferenceId("MFSBANK0001")
				.request("MFS_Request")
				.serviceName("MFS Client request")
				.dateLogged(new Date())
				.build();
		EventLog eventLog = eventLogRepository.save(eventLogNoResponse);
		assertThat(eventLog).isNotNull();
		assertThat(eventLog.getResponse());

		eventLog.setResponse("Request Response");
		eventLog = eventLogRepository.save(eventLog);
		assertThat(eventLog.getResponse()).isNotNull();
	}

	/**
	 * Failure test for saving a complete event log with a missing mandatory
	 * 								field in the event log table.
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void test_for_saving_invalid_log() {
		EventLog eventLogNoDate =  EventLog.builder()
				.mfsReferenceId("MFSBANK0001")
				.request("MFS_Request")
				.response("Success")
				.serviceName("MFS Client request")
				.build();
		EventLog eventLog = eventLogRepository.save(eventLogNoDate);
		assertThat(eventLog).isNull();
	}

	/**
	 * Mock event log used for testing purposes
	 * 
	 * @return Event log
	 */
	private EventLog eventLog() {
		return EventLog.builder()
				.mfsReferenceId("MFSBANK0001")
				.request("MFS_Request")
				.response("Success")
				.serviceName("MFS Client request")
				.dateLogged(new Date())
			.build();
	}

}