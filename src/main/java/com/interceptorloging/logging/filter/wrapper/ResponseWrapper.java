package com.interceptorloging.logging.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream servletOutputStream;
    private PrintWriter writer;
    private ServletOutputStreamWrapper copyServletOutputStreamWrapper;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public Map<String, String> getAllHeaders() {
        Map<String, String> headers = new HashMap<>();

        getHeaderNames().forEach(headerName -> headers.put(headerName, getHeader(headerName)));

        return headers;

    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (servletOutputStream == null) {
            servletOutputStream = getResponse().getOutputStream();
            copyServletOutputStreamWrapper = new ServletOutputStreamWrapper(servletOutputStream);
        }
        return copyServletOutputStreamWrapper;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (servletOutputStream != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }
        if (writer == null) {
            copyServletOutputStreamWrapper = new ServletOutputStreamWrapper(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(copyServletOutputStreamWrapper, getResponse().getCharacterEncoding()));
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (servletOutputStream != null) {
            copyServletOutputStreamWrapper.flush();
        }
    }

    public byte[] getBody() {
        if (copyServletOutputStreamWrapper != null) {
            return copyServletOutputStreamWrapper.getCopy();
        }
        return new byte[0];
    }
}
