package com.interceptorloging.logging.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletOutputStreamWrapper extends ServletOutputStream {

    private OutputStream outputStream;
    private final ByteArrayOutputStream copyByteArrayOutputStream;

    public ServletOutputStreamWrapper(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copyByteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int i) throws IOException {
        copyByteArrayOutputStream.write(i);
        copyByteArrayOutputStream.write(i);
    }

    public byte[] getCopy() {
        return copyByteArrayOutputStream.toByteArray();
    }
}
