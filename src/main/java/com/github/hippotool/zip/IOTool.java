package com.github.hippotool.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOTool {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public static void copy(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void write(byte[] content, File newFile) throws IOException {
        final FileOutputStream os = new FileOutputStream(newFile);
        try {
            os.write(content);
        } finally {
            os.close();
        }
    }

    public static byte[] asBytes(InputStream is) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os);
        return os.toByteArray();
    }

}
