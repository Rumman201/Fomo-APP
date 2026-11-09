package com.example.fomo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.data.remote.api.ApiClient;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class CertificatePinnerConfigTest {

    @Test
    public void testCertificatePinnerBuildsExpectedPins() {
        CertificatePinner pinner = ApiClient.buildCertificatePinner();
        assertNotNull(pinner);

        assertTrue(ApiClient.PIN_ISRG_ROOT_X1.startsWith("sha256/"));
        assertTrue(ApiClient.PIN_LE_R3_BACKUP.startsWith("sha256/"));
        assertTrue(ApiClient.PIN_ISRG_ROOT_X2.startsWith("sha256/"));
    }

    @Test
    public void testOkHttpClientHasPinningEnabledByDefault() {
        OkHttpClient client = ApiClient.buildOkHttpClient();
        assertNotNull(client);
        assertNotNull(client.certificatePinner());
        assertFalse(client.certificatePinner().getPins().isEmpty());
    }

    @Test
    public void testOkHttpClientDisablingPinningForTesting() {
        OkHttpClient client = ApiClient.buildOkHttpClient(false);
        assertNotNull(client);
        assertNotNull(client.certificatePinner());
        assertTrue(client.certificatePinner().getPins().isEmpty());
    }
}

