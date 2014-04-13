package com.github.hippotool.zip;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class ZipSimpleTest {

    @Test
    public void testZipUnzipArray() throws IOException {
        final Map<String, byte[]> inputMap = new HashMap<String, byte[]>();
        inputMap.put("data1", new byte[] { 1, 2, 3 });

        final byte[] archive = ZipSimple.zip(inputMap);
        final Map<String, byte[]> outputMap = ZipSimple.unzip(archive);

        assertEquals(inputMap.size(), outputMap.size());
        for (Entry<String, byte[]> entry : inputMap.entrySet()) {
            assertArrayEquals(entry.getValue(), outputMap.get(entry.getKey()));
        }
    }

}
