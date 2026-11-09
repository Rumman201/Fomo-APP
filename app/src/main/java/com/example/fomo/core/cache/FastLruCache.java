package com.example.fomo.core.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enterprise thread-safe, high-performance in-memory Least Recently Used (LRU) cache.
 * Implemented using pure Java LinkedHashMap with access-order ordering to ensure sub-millisecond
 * memory lookups across both Android OS runtimes and host JVM test environments.
 *
 * @param <K> Cache key type
 * @param <V> Cache value type
 */
public class FastLruCache<K, V> {

    private final int maxSize;
    private final LinkedHashMap<K, V> map;

    public FastLruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > FastLruCache.this.maxSize;
            }
        };
    }

    @Nullable
    public synchronized V get(@NonNull K key) {
        return map.get(key);
    }

    @Nullable
    public synchronized V put(@NonNull K key, @NonNull V value) {
        return map.put(key, value);
    }

    @Nullable
    public synchronized V remove(@NonNull K key) {
        return map.remove(key);
    }

    public synchronized void evictAll() {
        map.clear();
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized boolean containsKey(@NonNull K key) {
        return map.containsKey(key);
    }
}
