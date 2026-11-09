package com.example.fomo.security;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Pure Java in-memory implementation of SharedPreferences for JVM unit testing.
 */
public class InMemorySharedPreferences implements SharedPreferences {

    private final Map<String, Object> storage = new HashMap<>();

    @Override
    public Map<String, ?> getAll() {
        return new HashMap<>(storage);
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        Object val = storage.get(key);
        return (val instanceof String) ? (String) val : defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        Object val = storage.get(key);
        return (val instanceof Set) ? (Set<String>) val : defValues;
    }

    @Override
    public int getInt(String key, int defValue) {
        Object val = storage.get(key);
        return (val instanceof Integer) ? (Integer) val : defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        Object val = storage.get(key);
        return (val instanceof Long) ? (Long) val : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        Object val = storage.get(key);
        return (val instanceof Float) ? (Float) val : defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        Object val = storage.get(key);
        return (val instanceof Boolean) ? (Boolean) val : defValue;
    }

    @Override
    public boolean contains(String key) {
        return storage.containsKey(key);
    }

    @Override
    public Editor edit() {
        return new InMemoryEditor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    private class InMemoryEditor implements Editor {
        private final Map<String, Object> pending = new HashMap<>(storage);

        @Override
        public Editor putString(String key, @Nullable String value) {
            pending.put(key, value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            pending.put(key, values);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            pending.put(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            pending.put(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            pending.put(key, value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            pending.put(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            pending.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            pending.clear();
            return this;
        }

        @Override
        public boolean commit() {
            apply();
            return true;
        }

        @Override
        public void apply() {
            storage.clear();
            storage.putAll(pending);
        }
    }
}
