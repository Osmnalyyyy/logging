package com.interceptorloging.logging.service;

import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.interceptorloging.logging.constants.LoggingConstants.X_TRACE_ID;

@Service
public class TraceIdGenerator {
    public void generateRandomTraceId(){
        String traceId= UUID.randomUUID().toString();
        MDC.put(X_TRACE_ID,traceId);
    }
}
