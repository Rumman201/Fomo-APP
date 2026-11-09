package com.example.fomo.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.core.cache.FastLruCache;
import org.junit.Test;

public class FastLruCacheTest {

    @Test
    public void testBasicPutAndGet() {
        FastLruCache<String, String> cache = new FastLruCache<>(3);
        cache.put("k1", "v1");
        cache.put("k2", "v2");

        assertEquals("v1", cache.get("k1"));
        assertEquals("v2", cache.get("k2"));
        assertEquals(2, cache.size());
    }

    @Test
    public void testLruEvictionOnCapacityExceeded() {
        FastLruCache<String, Integer> cache = new FastLruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);

        // Access "a" to make it most recently used, making "b" the eldest
        cache.get("a");

        // Insert "c" - should evict "b"
        cache.put("c", 3);

        assertEquals(2, cache.size());
        assertEquals(Integer.valueOf(1), cache.get("a"));
        assertEquals(Integer.valueOf(3), cache.get("c"));
        assertNull(cache.get("b"));
    }

    @Test
    public void testEvictAllClearsCache() {
        FastLruCache<Integer, String> cache = new FastLruCache<>(5);
        cache.put(1, "one");
        cache.put(2, "two");
        assertEquals(2, cache.size());

        cache.evictAll();
        assertEquals(0, cache.size());
        assertNull(cache.get(1));
    }

    @Test
    public void testContainsKeyAndRemove() {
        FastLruCache<String, String> cache = new FastLruCache<>(5);
        cache.put("city", "Tokyo");
        assertTrue(cache.containsKey("city"));

        String removed = cache.remove("city");
        assertEquals("Tokyo", removed);
        assertFalse(cache.containsKey("city"));
    }
}

