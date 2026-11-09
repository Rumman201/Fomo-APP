package com.example.fomo.security.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Enterprise cryptographic manager using hardware-backed AndroidKeyStore.
 * Enforces AES-256-GCM authenticated encryption with 128-bit authentication tags
 * and random initialization vectors (IV).
 */
public class SecureKeystoreManager {

    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH_BYTES = 12; // 96-bit standard GCM IV
    private static final int GCM_TAG_LENGTH_BITS = 128; // 128-bit authentication tag
    private static final int KEY_SIZE_BITS = 256;

    public static final String DEFAULT_MASTER_KEY_ALIAS = "fomo_master_keystore_key";

    private final String keyAlias;
    private final SecureRandom secureRandom;
    private final SecretKey injectedKey; // Non-null in testing environments

    public SecureKeystoreManager() {
        this(DEFAULT_MASTER_KEY_ALIAS);
    }

    public SecureKeystoreManager(@NonNull String keyAlias) {
        this(keyAlias, null);
    }

    @VisibleForTesting
    public SecureKeystoreManager(@NonNull String keyAlias, @Nullable SecretKey testKey) {
        this.keyAlias = keyAlias;
        this.secureRandom = new SecureRandom();
        this.injectedKey = testKey;
    }

    /**
     * Retrieves or creates the hardware-backed AES-256 master key in AndroidKeyStore.
     * If an injected test key was provided, returns that key.
     */
    @NonNull
    public SecretKey getOrCreateKey() throws GeneralSecurityException, IOException {
        if (injectedKey != null) {
            return injectedKey;
        }

        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(keyAlias)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    ANDROID_KEYSTORE
            );

            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(KEY_SIZE_BITS)
                    .setRandomizedEncryptionRequired(true)
                    .build();

            keyGenerator.init(spec);
            return keyGenerator.generateKey();
        }

        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(keyAlias, null);
        if (entry == null || entry.getSecretKey() == null) {
            throw new GeneralSecurityException("Failed to retrieve secret key for alias: " + keyAlias);
        }
        return entry.getSecretKey();
    }

    /**
     * Encrypts plaintext string with AES-256-GCM and returns a Base64 encoded payload [IV + Ciphertext + Tag].
     */
    @NonNull
    public String encryptString(@NonNull String plaintext) throws GeneralSecurityException, IOException {
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = encryptBytes(plaintextBytes);
        return encodeBase64(encryptedBytes);
    }

    /**
     * Encrypts byte array with AES-GCM and returns [12-byte IV || Ciphertext + Tag].
     */
    @NonNull
    public byte[] encryptBytes(@NonNull byte[] plaintext) throws GeneralSecurityException, IOException {
        SecretKey secretKey = getOrCreateKey();
        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);

        byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] ciphertextWithTag = cipher.doFinal(plaintext);

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertextWithTag.length);
        byteBuffer.put(iv);
        byteBuffer.put(ciphertextWithTag);
        return byteBuffer.array();
    }

    /**
     * Decrypts Base64 encoded payload [IV + Ciphertext + Tag] and returns plaintext string.
     */
    @NonNull
    public String decryptString(@NonNull String base64Payload) throws GeneralSecurityException, IOException {
        byte[] encryptedBytes = decodeBase64(base64Payload);
        byte[] decryptedBytes = decryptBytes(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Decrypts byte array envelope [12-byte IV || Ciphertext + Tag].
     */
    @NonNull
    public byte[] decryptBytes(@NonNull byte[] envelope) throws GeneralSecurityException, IOException {
        if (envelope.length < GCM_IV_LENGTH_BYTES) {
            throw new GeneralSecurityException("Corrupt ciphertext envelope: payload too short.");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(envelope);
        byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
        byteBuffer.get(iv);

        byte[] ciphertextWithTag = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertextWithTag);

        SecretKey secretKey = getOrCreateKey();
        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return cipher.doFinal(ciphertextWithTag);
    }

    /**
     * Deletes the master key from AndroidKeyStore (e.g. on account reset or emergency wipe).
     */
    public void deleteKey() throws GeneralSecurityException, IOException {
        if (injectedKey != null) {
            return;
        }
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias);
        }
    }

    /**
     * Dual-environment Base64 encoder compatible with Android API 24+ and JVM host unit tests.
     */
    public static String encodeBase64(@NonNull byte[] data) {
        try {
            return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP);
        } catch (Throwable t) {
            try {
                Class<?> clazz = Class.forName("java.util.Base64");
                Method getEncoderMethod = clazz.getMethod("getEncoder");
                Object encoder = getEncoderMethod.invoke(null);
                Method encodeMethod = encoder.getClass().getMethod("encodeToString", byte[].class);
                return (String) encodeMethod.invoke(encoder, data);
            } catch (Exception e) {
                throw new RuntimeException("Base64 encoding failed across both Android and JVM providers", e);
            }
        }
    }

    /**
     * Dual-environment Base64 decoder compatible with Android API 24+ and JVM host unit tests.
     */
    public static byte[] decodeBase64(@NonNull String base64) {
        try {
            return android.util.Base64.decode(base64, android.util.Base64.NO_WRAP);
        } catch (Throwable t) {
            try {
                Class<?> clazz = Class.forName("java.util.Base64");
                Method getDecoderMethod = clazz.getMethod("getDecoder");
                Object decoder = getDecoderMethod.invoke(null);
                Method decodeMethod = decoder.getClass().getMethod("decode", String.class);
                return (byte[]) decodeMethod.invoke(decoder, base64);
            } catch (Exception e) {
                throw new RuntimeException("Base64 decoding failed across both Android and JVM providers", e);
            }
        }
    }
}
