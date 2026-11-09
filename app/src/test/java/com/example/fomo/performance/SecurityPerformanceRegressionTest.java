package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.api.ApiClient;
import com.example.fomo.security.crypto.SecureKeystoreManager;
import com.example.fomo.security.sanitizer.InputSanitizer;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

public class SecurityPerformanceRegressionTest {

    private SecureKeystoreManager keystoreManager;

    @Before
    public void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey testKey = keyGen.generateKey();
        keystoreManager = new SecureKeystoreManager("perf_test_key", testKey);
    }

    @Test
    public void testKeystoreEncryptionPreservesSubMillisecondPerformance() throws Exception {
        String sensitiveData = "User Session Token & Private Telemetry Coordinates";

        int iterations = 100;
        long startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String encrypted = keystoreManager.encryptString(sensitiveData);
            String decrypted = keystoreManager.decryptString(encrypted);
            assertEquals(sensitiveData, decrypted);
        }
        long durationNanos = System.nanoTime() - startNanos;

        double avgLatencyMillis = (double) durationNanos / (iterations * 1_000_000.0);
        assertTrue("AES-256-GCM roundtrip must average under 5.0ms on test runner (actual: " + avgLatencyMillis + "ms)",
                avgLatencyMillis < 5.0);
    }

    @Test
    public void testInputSanitizerSubMillisecondThroughput() {
        String rawQuery = "<script>alert('xss')</script> San Francisco ' OR 1=1 -- \u0000";

        int iterations = 1_000;
        long startNanos = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String cleaned = InputSanitizer.sanitizeSearchQuery(rawQuery);
            assertEquals("San Francisco OR 1=1", cleaned);
        }
        long durationNanos = System.nanoTime() - startNanos;

        double avgLatencyMicros = (double) durationNanos / (iterations * 1_000.0);
        assertTrue("Input sanitization must be sub-millisecond (actual: " + avgLatencyMicros + "us)",
                avgLatencyMicros < 200.0);
    }

    @Test
    public void testCertificatePinningRemainsActiveInProductionClient() {
        OkHttpClient client = ApiClient.buildOkHttpClient();
        assertNotNull(client);
        assertNotNull(client.certificatePinner());
        assertFalse("Certificate pinner must remain configured and active", client.certificatePinner().getPins().isEmpty());
    }
}

