package com.example.weblibrary.service.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * A simple LFU (Least Frequently Used) cache implementation.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
@Slf4j
public class SimpleCache<K, V> {
  private final int maxSize;
  private final Map<K, CacheEntry<V>> cache = new HashMap<>();
  private Consumer<K> evictionListener;

  /**
   * Internal class representing a cache entry with value and frequency count.
   *
   * @param <V> the type of the cached value
   */
  private static class CacheEntry<V> {
    V value;
    int frequency;

    CacheEntry(V value) {
      this.value = value;
      this.frequency = 1;
    }
  }

  /**
   * Constructs a new SimpleCache with the specified maximum size.
   *
   * @param maxSize the maximum number of entries the cache can hold
   * @throws IllegalArgumentException if maxSize is less than or equal to 0
   */
  public SimpleCache(int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max size must be greater than 0");
    }
    this.maxSize = maxSize;
  }

  /**
   * Sets the eviction listener that will be notified when items are evicted.
   *
   * @param evictionListener the listener to be notified
   */
  public void setEvictionListener(Consumer<K> evictionListener) {
    this.evictionListener = evictionListener;
  }

  /**
   * Retrieves a value from the cache by key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the key, or null if not found
   */
  public synchronized V get(K key) {
    CacheEntry<V> entry = cache.get(key);
    if (entry == null) {
      log.info("⭕ LFU Cache: Item NOT found. Key: {}", key);
      return null;
    }
    entry.frequency++;
    log.info("❗LFU Cache: Item found. Key: {}, New Frequency: {}",
        key, entry.frequency);
    return entry.value;
  }

  /**
   * Adds or updates a value in the cache.
   *
   * @param key the key with which the specified value is to be associated
   * @param value the value to be associated with the specified key
   */
  public synchronized void put(K key, V value) {
    if (cache.containsKey(key)) {
      CacheEntry<V> entry = cache.get(key);
      entry.value = value;
      entry.frequency++;
      log.info("❗ LFU Cache: Item updated. Key: {}, New Frequency: {}",
          key, entry.frequency);
    } else {
      if (cache.size() >= maxSize) {
        evictLeastFrequentlyUsed();
      }
      cache.put(key, new CacheEntry<>(value));
      log.info("❗ LFU Cache: New item added. Key: {}", key);
    }
  }

  /**
   * Evicts the least frequently used item from the cache.
   */
  private void evictLeastFrequentlyUsed() {
    K lfuKey = null;
    int minFrequency = Integer.MAX_VALUE;

    for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
      if (entry.getValue().frequency < minFrequency) {
        minFrequency = entry.getValue().frequency;
        lfuKey = entry.getKey();
      }
    }

    if (lfuKey != null) {
      cache.remove(lfuKey);
      log.info("🗑 LFU Cache: Evicted item. Key: {}, Frequency at removal: {}",
          lfuKey, minFrequency);
      if (evictionListener != null) {
        evictionListener.accept(lfuKey);
      }
    }
  }

  /**
   * Removes the value associated with the specified key.
   *
   * @param key the key whose mapping is to be removed
   * @return the removed value, or null if no mapping existed
   */
  public synchronized V remove(K key) {
    CacheEntry<V> removed = cache.remove(key);
    if (removed != null) {
      log.info("🗑 LFU Cache: Item removed. Key: {}", key);
      if (evictionListener != null) {
        evictionListener.accept(key);
      }
      return removed.value;
    }
    return null;
  }

  /**
   * Clears all entries from the cache.
   */
  public synchronized void clear() {
    cache.clear();
    log.info("🗑 LFU Cache: Cache cleared.");
  }

  /**
   * Returns the number of entries in the cache.
   *
   * @return the number of entries in the cache
   */
  public synchronized int size() {
    return cache.size();
  }

  /**
   * Returns a string representation of the cache contents.
   *
   * @return a string representation of the cache
   */
  @Override
  public synchronized String toString() {
    return cache.toString();
  }
}