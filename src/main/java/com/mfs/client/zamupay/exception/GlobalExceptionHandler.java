package com.mfs.client.zamupay.exception;

import com.mfs.client.zamupay.infrastucture.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;


/**
 * Class will handle the various Exceptions that may be experienced in application.
 */
@AllArgsConstructor
@ControllerAdvice()
@Log4j2
public class GlobalExceptionHandler {

    /**
     * Catches a DataAccessException for an issues related to the database
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public MFSErrorResponse dbError(DataAccessException exception) {
    	log.info("Database Error: {}",exception.getMessage());
        log.error("Application failure occured due to database operation!", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Application failure occured due to database operation!")
                .message(exception.getMessage())
                .build();

    }

    /**
     * Catches a RuntimeException for any application failure during run time
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public MFSErrorResponse applicationFailure(RuntimeException exception) {
    	log.info("Application Error: {}",exception.getMessage());
        log.error("Application failure occured!", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(exception.getMessage())
                .build();

    }

    /**
     * Catches a MissingConfigValueException for retrieving a non existent config value
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingConfigValueException.class)
    public @ResponseBody
    ResponseEntity<?> systemConfigError(MissingConfigValueException exception) {
    	log.info("Missing system config Error: {}",exception.getMessage());
        log.error("Application failure occured!", exception);
        exception.printStackTrace();

        MFSErrorResponse errorResponse = MFSErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Missing system config")
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Catches a DuplicateRequestException for the same request that is more than once
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateRequestException.class)
    @ResponseBody
    public MFSErrorResponse duplicateTransactionException(DuplicateRequestException exception) {
    	log.info("Duplication transaction Error: {}",exception.getMessage());
        log.error("DuplicateRequestException", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .error(String.valueOf(HttpStatus.CONFLICT))
                .message(ResponseCode.DUPLICATE_TRANSACTION.getDescription())
                .build();

    }

    /**
     * Catches a NoSuchTransactionException for retrieving a non existent transaction record
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchTransactionExistsException.class)
    @ResponseBody
    public MFSErrorResponse missingTransactionException(NoSuchTransactionExistsException exception) {
    	log.info("No transaction found Error: {}",exception.getMessage());
        log.error("NoSuchTransactionExistsException", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .error(String.valueOf(HttpStatus.NOT_FOUND))
                .message(ResponseCode.TRANSACTION_ID_NOT_EXIST.getDescription())
                .build();
    }

    /**
     * Catches a NoCountryFoundException for retrieving a country code that does not exist
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoCountryFoundException.class)
    @ResponseBody
    public MFSErrorResponse countryNotFoundException(NoCountryFoundException exception) {
    	log.info("Invalid country code Error: {}",exception.getMessage());
        log.error("CountryCodeNotFoundException", exception);
        exception.printStackTrace();
        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .error(String.valueOf(HttpStatus.NOT_FOUND))
                .message(ResponseCode.COUNTRY_CODE_NOT_EXIST.getDescription())
                .build();
    }

    /**
     * Catches a MissingRequiredFieldException for an invalid request payload
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(MissingRequiredFieldException.class)
    @ResponseBody
    public MFSErrorResponse missingRequiredFieldException(MissingRequiredFieldException exception) {
    	log.info("Mandatory field Error: {}",exception.getMessage());
        log.error("Mandatory field Error: {}",exception.getMessage());
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .error(String.valueOf(HttpStatus.NOT_ACCEPTABLE))
                .message(ResponseCode.REQUIRED_MISSING_FIELD.getDescription())
                .build();
    }

    /**
     * Catches a ConstraintViolationException for an invalid request payload
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public MFSErrorResponse handleValidationException(ConstraintViolationException exception) {
    	log.info("Mandatory field Error: {}",exception.getMessage());
        log.error("ConstraintViolationException", exception);
        exception.printStackTrace();

        final StringBuilder errorMsgBuilder = new StringBuilder("Missing mandatory fields: ");
        if (exception.getConstraintViolations() == null) {
            errorMsgBuilder.append(exception.getMessage());
        } else {
            exception.getConstraintViolations().forEach(cv -> errorMsgBuilder.append(cv.getPropertyPath()));
        }

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(errorMsgBuilder.toString())
                .build();
    }

    /**
     * Catches a HttpMessageNotReadableException
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public MFSErrorResponse badRequest(HttpMessageNotReadableException exception) {
    	log.error("Application failure occured due to Http message invalid!", exception);
        log.info("Application failure occured due to Http message invalid!", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Application failure occured due to database operation!")
                .message(exception.getMessage())
                .build();
    	
    }

    /**
     * Catches a MFSClientException
     *
     * @param exception represents the caught exception
     * @return a MFS error response
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MFSClientException.class)
    @ResponseBody
    public MFSErrorResponse clientException(MFSClientException exception) {
        log.error("ClientException", exception);
        log.info("ClientException", exception);
        exception.printStackTrace();

        return MFSErrorResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ResponseCode.CLIENT_EXCEPTION.getDescription())
                .message(exception.getMessage())
                .build();

    }

    /**
     * Represents a JSON response for all the caught exceptions
     */
    @Generated
    @Data
    @Builder
    public static class MFSErrorResponse {

        int statusCode;
        String error;
        String message;

    }

}
