package com.interceptorloging.logging.filter;

import com.interceptorloging.logging.filter.wrapper.RequestWrapper;
import com.interceptorloging.logging.filter.wrapper.ResponseWrapper;
import com.interceptorloging.logging.service.TraceIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.interceptorloging.logging.constants.LoggingConstants.X_TRACE_ID;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringLoggingFilter extends OncePerRequestFilter {

    private final TraceIdGenerator traceIdGenerator;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        setResponseHeaders(response);


        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);


        log.info(String.format("Request Headers : %s", requestWrapper.getAllHeaders().toString()));
        log.info(String.format("Response Headers : %s", responseWrapper.getAllHeaders().toString()));

        traceIdGenerator.generateRandomTraceId();

        logRequest(requestWrapper);
        filterChain.doFilter(requestWrapper, responseWrapper);
        logResponse(responseWrapper);
    }

    private void logRequest(RequestWrapper requestWrapper) throws IOException {
        log.info(String.format("## %s ## Resquest Method : %s , RequestUri  : %s, RequestHeaders : %s,RequestBody : %s",
                requestWrapper.getServerName(),
                requestWrapper.getMethod(),
                requestWrapper.getRequestURI(),
                requestWrapper.getAllHeaders(),
                RequestWrapper.body));
    }

    private static void logResponse(ResponseWrapper responseWrapper) {

        log.info(String.format(" Response Status : %s , ResponseHeaders : %s , ResponseBody : %s ",
                responseWrapper.getStatus(),
                responseWrapper.getAllHeaders(),
                IOUtils.toString(responseWrapper.getBody(), responseWrapper.getCharacterEncoding())));

    }


    private void setResponseHeaders(HttpServletResponse response) {
        response.addHeader(X_TRACE_ID, MDC.get("X_TRACE_ID"));

    }
}
