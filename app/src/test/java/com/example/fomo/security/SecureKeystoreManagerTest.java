package com.example.fomo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.example.fomo.security.crypto.SecureKeystoreManager;
import java.security.GeneralSecurityException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.Before;
import org.junit.Test;

public class SecureKeystoreManagerTest {

    private SecureKeystoreManager keystoreManager;
    private SecretKey testKey;

    @Before
    public void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        testKey = keyGen.generateKey();
        keystoreManager = new SecureKeystoreManager("test_alias", testKey);
    }

    @Test
    public void testEncryptionAndDecryptionRoundtrip() throws Exception {
        String plaintext = "Sensitive API Key & User Telemetry Coordinate";
        String ciphertext = keystoreManager.encryptString(plaintext);

        assertNotNull(ciphertext);
        assertFalse(ciphertext.isEmpty());
        assertFalse(ciphertext.equals(plaintext));

        String decrypted = keystoreManager.decryptString(ciphertext);
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void testRandomizedIvProducesDistinctCiphertexts() throws Exception {
        String plaintext = "Repeated Observation Invariant";

        String ciphertext1 = keystoreManager.encryptString(plaintext);
        String ciphertext2 = keystoreManager.encryptString(plaintext);

        assertNotNull(ciphertext1);
        assertNotNull(ciphertext2);
        assertFalse("GCM must use fresh random IV per encryption", ciphertext1.equals(ciphertext2));

        assertEquals(plaintext, keystoreManager.decryptString(ciphertext1));
        assertEquals(plaintext, keystoreManager.decryptString(ciphertext2));
    }

    @Test
    public void testTamperedCiphertextThrowsSecurityException() throws Exception {
        String plaintext = "Original Authentic Payload";
        byte[] envelope = keystoreManager.encryptBytes(plaintext.getBytes());

        // Flip a single bit in the ciphertext / authentication tag portion
        envelope[envelope.length - 1] ^= 0x01;

        try {
            keystoreManager.decryptBytes(envelope);
            fail("Decryption of tampered ciphertext must throw GeneralSecurityException");
        } catch (GeneralSecurityException expected) {
            assertTrue(expected.getMessage() != null || expected instanceof javax.crypto.AEADBadTagException);
        }
    }

    @Test
    public void testCorruptedShortEnvelopeThrowsException() throws Exception {
        byte[] shortEnvelope = new byte[] { 1, 2, 3, 4, 5 }; // Less than 12-byte IV

        try {
            keystoreManager.decryptBytes(shortEnvelope);
            fail("Short envelope must throw GeneralSecurityException");
        } catch (GeneralSecurityException expected) {
            assertTrue(expected.getMessage().contains("too short"));
        }
    }

    @Test
    public void testBase64EncodingHelpers() {
        byte[] data = "Hello Security World".getBytes();
        String encoded = SecureKeystoreManager.encodeBase64(data);
        assertNotNull(encoded);

        byte[] decoded = SecureKeystoreManager.decodeBase64(encoded);
        assertEquals("Hello Security World", new String(decoded));
    }
}

