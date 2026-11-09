package com.example.fomo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.security.crypto.SecureKeystoreManager;
import com.example.fomo.security.crypto.SecurePreferences;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.Before;
import org.junit.Test;

public class SecurePreferencesTest {

    private SecurePreferences securePreferences;
    private InMemorySharedPreferences rawStorage;
    private SecureKeystoreManager keystoreManager;

    @Before
    public void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey testKey = keyGen.generateKey();
        keystoreManager = new SecureKeystoreManager("pref_test_alias", testKey);
        rawStorage = new InMemorySharedPreferences();
        securePreferences = new SecurePreferences(rawStorage, keystoreManager);
    }

    @Test
    public void testStringEncryptionInStorage() {
        securePreferences.putString("user_token", "Bearer secret-token-xyz");

        // The raw underlying storage must NOT contain plaintext
        String rawValue = rawStorage.getString("user_token", null);
        assertNotNull(rawValue);
        assertFalse("Raw preference storage must contain ciphertext, not plaintext",
                rawValue.contains("secret-token-xyz"));

        // Reading via SecurePreferences must return decrypted plaintext
        String decrypted = securePreferences.getString("user_token", null);
        assertEquals("Bearer secret-token-xyz", decrypted);
    }

    @Test
    public void testPrimitiveTypesRoundtrip() {
        securePreferences.putBoolean("opt_in_telemetry", true);
        assertTrue(securePreferences.getBoolean("opt_in_telemetry", false));

        securePreferences.putLong("last_sync_timestamp", 1729000000000L);
        assertEquals(1729000000000L, securePreferences.getLong("last_sync_timestamp", 0L));

        securePreferences.putInt("retry_count", 3);
        assertEquals(3, securePreferences.getInt("retry_count", 0));
    }

    @Test
    public void testTamperedDataFallbackGracefully() {
        // Store valid data
        securePreferences.putString("auth_hash", "valid-signature");
        String raw = rawStorage.getString("auth_hash", null);
        assertNotNull(raw);

        // Tamper with the raw encrypted ciphertext
        rawStorage.edit().putString("auth_hash", raw.substring(0, raw.length() - 4) + "AAAA").apply();

        // Must return default value instead of crashing or throwing unhandled exception
        String fallback = securePreferences.getString("auth_hash", "default_safe_value");
        assertEquals("default_safe_value", fallback);
    }

    @Test
    public void testRemovalAndClear() {
        securePreferences.putString("key1", "val1");
        securePreferences.putString("key2", "val2");
        assertTrue(securePreferences.contains("key1"));
        assertTrue(securePreferences.contains("key2"));

        securePreferences.remove("key1");
        assertFalse(securePreferences.contains("key1"));
        assertTrue(securePreferences.contains("key2"));

        securePreferences.clear();
        assertFalse(securePreferences.contains("key2"));
    }
}

