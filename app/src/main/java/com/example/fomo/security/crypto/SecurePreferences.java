package com.example.fomo.security.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Enterprise encrypted SharedPreferences wrapper leveraging AES-256-GCM authenticated encryption.
 * Protects persisted security flags, user credentials, authorization tokens, and private telemetry metadata.
 */
public class SecurePreferences {

    private static final String TAG = "SecurePreferences";
    public static final String PREF_NAME = "fomo_secure_prefs";

    private final SharedPreferences sharedPreferences;
    private final SecureKeystoreManager keystoreManager;

    public SecurePreferences(@NonNull Context context) {
        this(context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE), new SecureKeystoreManager());
    }

    public SecurePreferences(@NonNull SharedPreferences sharedPreferences, @NonNull SecureKeystoreManager keystoreManager) {
        this.sharedPreferences = sharedPreferences;
        this.keystoreManager = keystoreManager;
    }

    public void putString(@NonNull String key, @Nullable String value) {
        if (value == null) {
            remove(key);
            return;
        }
        try {
            String encrypted = keystoreManager.encryptString(value);
            sharedPreferences.edit().putString(key, encrypted).apply();
        } catch (Exception e) {
            safeLogError("Failed to encrypt key: " + key, e);
        }
    }

    @Nullable
    public String getString(@NonNull String key, @Nullable String defaultValue) {
        String encrypted = sharedPreferences.getString(key, null);
        if (encrypted == null) {
            return defaultValue;
        }
        try {
            return keystoreManager.decryptString(encrypted);
        } catch (Exception e) {
            safeLogWarn("Failed to decrypt key: " + key + " (data may be tampered or master key rotated)", e);
            return defaultValue;
        }
    }

    public void putBoolean(@NonNull String key, boolean value) {
        putString(key, Boolean.toString(value));
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        String val = getString(key, null);
        if (val == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(val);
    }

    public void putLong(@NonNull String key, long value) {
        putString(key, Long.toString(value));
    }

    public long getLong(@NonNull String key, long defaultValue) {
        String val = getString(key, null);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void putInt(@NonNull String key, int value) {
        putString(key, Integer.toString(value));
    }

    public int getInt(@NonNull String key, int defaultValue) {
        String val = getString(key, null);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean contains(@NonNull String key) {
        return sharedPreferences.contains(key);
    }

    public void remove(@NonNull String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    private static void safeLogError(String message, Throwable t) {
        try {
            Log.e(TAG, message, t);
        } catch (Throwable fallback) {
            System.err.println("[" + TAG + "] ERROR: " + message);
        }
    }

    private static void safeLogWarn(String message, Throwable t) {
        try {
            Log.w(TAG, message, t);
        } catch (Throwable fallback) {
            System.err.println("[" + TAG + "] WARN: " + message);
        }
    }
}
