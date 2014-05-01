package com.github.hippotool.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTool {

    public static Zip zip() {
        return new Zip(new ByteArrayOutputStream());
    }

    public static Zip zip(File file) throws FileNotFoundException {
        return new Zip(new FileOutputStream(file));
    }

    public static Zip zip(OutputStream os) {
        return new Zip(os);
    }

    public static Unzip unzip(byte[] zip) throws FileNotFoundException {
        return new Unzip(new ByteArrayInputStream(zip));
    }

    public static Unzip unzip(File zip) throws FileNotFoundException {
        return new Unzip(new FileInputStream(zip));
    }

    public static Unzip unzip(InputStream is) {
        return new Unzip(is);
    }

    public static class Zip {

        private final ZipOutputStream zos;
        private final OutputStream os;

        private Zip(OutputStream os) {
            this.zos = new ZipOutputStream(os);
            this.os = os;
        }

        public Zip add(String name, byte[] value) throws IOException {
            zos.putNextEntry(new ZipEntry(name));
            zos.write(value);
            zos.closeEntry();
            return this;
        }

        public Zip add(String name, String value) throws IOException {
            return add(name, value.getBytes("UTF-8"));
        }

        public Zip add(String name, InputStream is) throws IOException {
            zos.putNextEntry(new ZipEntry(name));
            IOTool.copy(is, zos);
            zos.closeEntry();
            return this;
        }

        public Zip add(File file) throws IOException {
            zipFile(file.getName(), file);
            return this;
        }

        public Zip add(Map<String, byte[]> values) throws IOException {
            for(Entry<String, byte[]> entry : values.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public void close() throws IOException {
            zos.close();
        }

        public byte[] asBytes() {
            return ((ByteArrayOutputStream)os).toByteArray();
        }

        private void zipFile(String name, File file) throws IOException {
            if(file.isDirectory()) {
                for(File child : file.listFiles()) {
                    zipFile(name + File.separator + child.getName(), child);
                }
            } else {
                final InputStream is = new FileInputStream(file);
                try {
                    add(name, is);
                } finally {
                    is.close();
                }
            }
        }

    }

    public static class Unzip {

        private final InputStream is;

        private Unzip(InputStream is) {
            this.is = is;
        }

        public void to(File dir) throws IOException {
            final ZipInputStream zip = new ZipInputStream(is);
            try {
                for (ZipEntry ze = zip.getNextEntry(); ze != null; ze = zip.getNextEntry()) {
                    final File dest = new File(dir, ze.getName());
                    dest.getParentFile().mkdirs();
                    final FileOutputStream os = new FileOutputStream(dest);
                    try {
                        IOTool.copy(zip, os);
                    } finally {
                        os.close();
                    }
                }
            } finally {
                zip.close();
            }
        }

        public Map<String, byte[]> asMap() throws IOException {
            final ZipInputStream zip = new ZipInputStream(is);
            try {
                final Map<String, byte[]> files = new HashMap<String, byte[]>();
                for (ZipEntry ze = zip.getNextEntry(); ze != null; ze = zip.getNextEntry()) {
                    files.put(ze.getName(), IOTool.asBytes(zip));
                }
                return files;
            } finally {
                zip.close();
            }
        }

    }

}
