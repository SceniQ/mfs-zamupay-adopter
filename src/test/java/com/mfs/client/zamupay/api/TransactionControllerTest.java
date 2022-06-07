package com.mfs.client.zamupay.api;

import static com.mfs.client.zamupay.infrastucture.MFSConstants.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.mfs.client.zamupay.MockMvcBase;
import com.mfs.client.zamupay.api.dto.TransactionRequest;
import com.mfs.client.zamupay.api.dto.TransactionResponse;
import com.mfs.client.zamupay.api.service.ConfigService;
import com.mfs.client.zamupay.persistence.EventLogRepository;
import com.mfs.client.zamupay.persistence.TransactionRepository;
import com.mfs.client.zamupay.persistence.model.EventLog;
import com.mfs.client.zamupay.persistence.model.TransactionLog;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for the Transaction controller endpoints
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration
@TestPropertySource(locations = "classpath:application-junit.properties")
public class TransactionControllerTest extends MockMvcBase {

    @MockBean
    private EventLogRepository eventLogRepository;
    @MockBean
    private ConfigService configService;
    @MockBean
    private TransactionRepository transactionRepository;

    private static ClientAndServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Initializing the mock server
     */
    @BeforeClass
    public static void startServer() {
        mockServer = startClientAndServer(8080);
    }

    /**
     * Stopping the mock server after test run
     */
    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Before
    public void init(){

        when(eventLogRepository.save(any(EventLog.class))).thenReturn(EventLog.builder().eventId(1L).build());
        when(configService.getConfigByKey(SUBMIT_TRANSACTION_URL)).thenReturn("http://localhost:8080/mpambaservice/v1/B2CTransfer");
        when(configService.getConfigByKey(QUERY_SERVICE_URL)).thenReturn("http://localhost:8080/mpambaservice/v1/CheckB2CTransaction");
        when(transactionRepository.findByMfsReferenceId(any(String.class))).thenReturn(Optional.empty());
        when(transactionRepository.save(any(TransactionLog.class))).thenReturn(transactionLog());

    }

    /**
     * Success test for submitting a transaction
     *
     * @throws Exception
     */
    @Test
    public void test_for_submit_transaction() throws Exception {
        TransactionResponse response = TransactionResponse.builder()
                .mfsReferenceId("MFS20210902001")
                .transactionId("BANK20210902001")
                .amount(200.00D)
                .primaryAccountNumber("+2547110000")
                .responseCode(0)
                .responseDesc("CREATED")
                .build();

        String clientRequest = "";

        new MockServerClient("localhost", 8080)
                .when(
                        HttpRequest.request("/mpambaservice/v1/B2CTransfer")
                                .withMethod("POST")
                                .withBody(mapper.writeValueAsString(clientRequest), Charset.defaultCharset())
                                .withKeepAlive(true).withSecure(false))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader(new Header("Content-Type","application/*+json"))
                                .withBody(mapper.writeValueAsString(response))
                                .withDelay(TimeUnit.SECONDS, 1)
                );

       mockMvc.perform(post("/bank/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isOk());
    }

    /**
     * Failure test for bad transaction controller tests
     *
     * @throws Exception
     */
    @Test
    public void test_for_bad_transaction_controller_request() throws Exception{

        //using the wrong URL
        mockMvc.perform(post("/ban/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isNotFound());

        //wrong media type
        mockMvc.perform(post("/bank/transfer")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isUnsupportedMediaType());

        //wrong http method
        mockMvc.perform(get("/bank/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isMethodNotAllowed());

    }

    /**
     * Success test for querying transaction status
     *
     * @throws Exception
     */
    @Test
    public void test_for_query_transaction() throws Exception{
        TransactionResponse response = TransactionResponse.builder()
                .mfsReferenceId("MFS20210902001")
                .transactionId("BANK20210902001")
                .amount(200.00D)
                .primaryAccountNumber("+2547110000")
                .responseCode(0)
                .responseDesc("CREATED")
                .build();

        new MockServerClient("localhost", 8080)
                .when(
                        HttpRequest.request("/mpambaservice/v1/CheckB2CTransaction")
                                .withMethod("GET")
                                .withKeepAlive(true).withSecure(false))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader(new Header("Content-Type","application/*+json"))
                                .withBody(mapper.writeValueAsString(response))
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        when(transactionRepository.findByMfsReferenceId(any(String.class)))
                .thenReturn(Optional.of(transactionLog()));
        mockMvc.perform(get("/bank/getStatus/{mfsReferenceId}","MFSTest20210906"))
                .andExpect(status().isOk());

    }

    /**
     * Failure test for querying transaction status for a transaction that does not exist
     *
     * @throws Exception
     */
    @Test
    public void test_for_query_status_with_wrong_trans_id() throws Exception{
        when(transactionRepository.findByMfsReferenceId(any(String.class)))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/bank/getStatus/{mfsReferenceId}","MFSTest20210906"))
                .andExpect(status().isNotFound());
    }

    /**
     * Failure test for bad query status controller tests
     *
     * @throws Exception
     */
    @Test
    public void test_for_bad_query_status_controller_request() throws Exception{

        //using the wrong URL
        mockMvc.perform(get("/getStatus/{mfsReferenceId}","MFSTest20210906"))
                .andExpect(status().isNotFound());

        //wrong Http method
        mockMvc.perform(post("/getStatus/{mfsReferenceId}","MFSTest20210906"))
                .andExpect(status().isNotFound());
    }


    /**
     * Mock transaction request used for testing purposes
     *
     * @return Transaction request
     */
    private TransactionRequest request(){
        return TransactionRequest.builder()
                .mfsReferenceId("MFSTest202108")
                .primaryAccountNumber("+276354254361")
                .amount(100D)
                .build();

    }

    /**
     * Dummy transaction log object used for testing purposes
     *
     * @return complete TransactionLog object
     */
    private TransactionLog transactionLog(){
        Random genId = new Random();
        return TransactionLog.builder()
                .transactionId("TRANS"+genId.nextInt(100))
                .mfsReferenceId("MFSTest202108")
                .primaryAccountNumber("+278646523589")
                .amount(100D)
                .dateLogged(new Date())
                .build();
    }
}