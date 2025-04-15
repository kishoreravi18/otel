package com.yubi.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.UUID;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String TRACE_ID_PREFIX = "";
    private static final String TRACE_ID_DIV = "-";


    public static final Logger LOG = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGenericException(Exception e) {
        String traceId = getTraceId();
        LOG.error("exception occurred traceId {}", traceId, e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(status);
    }

    private String getTraceId() {
        return TRACE_ID_PREFIX + TRACE_ID_DIV + UUID.randomUUID() + TRACE_ID_DIV + Instant.now().getEpochSecond();
    }
}