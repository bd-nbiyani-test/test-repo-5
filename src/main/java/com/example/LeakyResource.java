package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Demo class with a deliberate resource leak (CWE-404).
 * Used to validate Polaris SAST detection via bridge-service PR comments flow.
 */
public class LeakyResource {

    public String readConfig(String path) throws IOException {
        // Improper: stream is opened but never closed on any code path.
        // A proper implementation would use try-with-resources.
        FileInputStream input = new FileInputStream(path);
        byte[] buf = new byte[1024];
        int n = input.read(buf);
        if (n <= 0) {
            return "";
        }
        // Below is the exact line Polaris would flag (line 25).
        String result = new String(buf, 0, n);
        return result;
    }

    public InputStream openNoClose(String path) throws IOException {
        return new FileInputStream(path);
    }
}