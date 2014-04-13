package com.github.hippotool.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipSimple {

    public static byte[] zip(Map<String, byte[]> data) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        zip(data, out);
        return out.toByteArray();
    }

    public static void zip(Map<String, byte[]> data, OutputStream os) throws IOException {
        final ZipOutputStream zos = new ZipOutputStream(os);
        try {
            for (Entry<String, byte[]> file : data.entrySet()) {
                zos.putNextEntry(new ZipEntry(file.getKey()));
                zos.write(file.getValue());
                zos.closeEntry();
            }
        } finally {
            zos.close();
        }
    }

    public static Map<String, byte[]> unzip(byte[] zip) throws IOException {
        return unzip(new ByteArrayInputStream(zip));
    }

    public static Map<String, byte[]> unzip(InputStream io) throws IOException {
        final Map<String, byte[]> files = new HashMap<String, byte[]>();
        final ZipInputStream zis = new ZipInputStream(io);
        try {
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                files.put(ze.getName(), IOTool.asBytes(zis));
            }
        } finally {
            zis.close();
        }
        return files;
    }
}
