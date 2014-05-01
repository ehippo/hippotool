package com.github.hippotool.zip;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ZipToolTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void testZipToolInMemory() throws IOException {
        final String TEST_NAME = "test.txt";
        final String TEST_CONTENT = "Test Content";
        final String TEST_BIN_NAME = "test.bin";
        final byte[] TEST_BIN_CONTENT = new byte[] {1,2,3};
        final String TEST_FILE_NAME = "testfile.bin";

        final byte[] archive = ZipTool.zip()
                .add(TEST_NAME, TEST_CONTENT)
                .add(TEST_BIN_NAME, TEST_BIN_CONTENT)
                .add(createFile(TEST_FILE_NAME, TEST_BIN_CONTENT))
                .asBytes();
        final Map<String, byte[]> files = ZipTool.unzip(archive).asMap();

        assertEquals(files.size(), 3);
        assertEquals(new String(files.get(TEST_NAME), "UTF-8"), TEST_CONTENT);
        assertArrayEquals(files.get(TEST_BIN_NAME), TEST_BIN_CONTENT);
        assertArrayEquals(files.get(TEST_FILE_NAME), TEST_BIN_CONTENT);
    }

    @Test
    public void testZipUnzipDirInMemory() throws IOException {
        final File dir = createTestDir();
        final byte[] archive = ZipTool.zip().add(dir).asBytes();
        final Map<String, byte[]> files = ZipTool.unzip(archive).asMap();

        assertEquals(3, files.size());
    }

    @Test
    public void testZipUnzipToDir() throws IOException {
        final File dir = createTestDir();
        final File dest = folder.newFolder("dest");
        final byte[] archive = ZipTool.zip().add(dir).asBytes();
        ZipTool.unzip(archive).to(dest);

        assertEquals(1, dest.listFiles().length);
        final File destDir1 = dest.listFiles()[0];
        assertEquals(3, destDir1.listFiles().length);
    }

    private File createTestDir() throws IOException {
        File dir1 = folder.newFolder("dir1");
        File file1 = new File(dir1, "file1");
        IOTool.write(new byte[] {1,2,3}, file1);
        File file2 = new File(dir1, "file2");
        IOTool.write(new byte[] {3, 2, 1}, file2);

        File dir12 = new File(dir1, "dir12");
        dir12.mkdirs();
        File file12 = new File(dir12, "file1");
        IOTool.write(new byte[] {4,5,6}, file12);
        return dir1;
    }

    private File createFile(String name, byte[] content) throws IOException {
        final File createdFile = folder.newFile(name);
        IOTool.write(content, createdFile);
        return createdFile;
    }

}
