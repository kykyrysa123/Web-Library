package com.example.weblibrary.service.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * LFU (Least Frequently Used) Cache Implementation with Database
 * Synchronization. When an item is removed from the cache, it will also be
 * removed from the database.
 *
 * @param <K>
 *     The type of the key.
 * @param <V>
 *     The type of the value.
 */
@Slf4j
public class SimpleCache<K, V> {
  private final int maxSize;
  private final Map<K, CacheEntry<V>> cache = new HashMap<>();
  private Consumer<K> evictionListener; // Слушатель удаления

  /**
   * Inner class representing a cache entry with a frequency counter.
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
   * @param maxSize the maximum number of items the cache can hold
   * @throws IllegalArgumentException if maxSize is less than or equal to 0
   */
  public SimpleCache(int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max size must be greater than 0");
    }
    this.maxSize = maxSize;
  }

  /**
   * Retrieves the value associated with the specified key and increases its
   * frequency.
   */
  public synchronized V get(K key) {
    CacheEntry<V> entry = cache.get(key);
    if (entry == null) {
      log.info("⭕ LFU Cache: Item NOT found. Key: {}", key);
      return null;
    }
    entry.frequency++;
    log.info("❗LFU Cache: Item found. Key: {}, New Frequency: {}", key,
        entry.frequency);
    return entry.value;
  }

  /**
   * Adds a new key-value pair to the cache or updates an existing one.
   */
  public synchronized void put(K key, V value) {
    if (cache.containsKey(key)) {
      CacheEntry<V> entry = cache.get(key);
      entry.value = value;
      entry.frequency++;
      log.info("❗ LFU Cache: Item updated. Key: {}, New Frequency: {}", key,
          entry.frequency);
    } else {
      if (cache.size() >= maxSize) {
        evictLeastFrequentlyUsed();
      }
      cache.put(key, new CacheEntry<>(value));
      log.info("❗ LFU Cache: New item added. Key: {}", key);
    }
  }

  /**
   * Removes the least frequently used item from the cache and triggers the
   * database removal.
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
      log.info(
          "🗑 LFU Cache: Evicted item. Key: {}, Frequency at "
              + "removal: {}",
          lfuKey, minFrequency);
      if (evictionListener != null) {
        evictionListener.accept(lfuKey); // Удаляем из БД
      }
    }
  }

  /**
   * Removes a specific key from the cache and database.
   */
  public synchronized V remove(K key) {
    CacheEntry<V> removed = cache.remove(key);
    if (removed != null) {
      log.info("🗑 LFU Cache: Item removed. Key: {}", key);
      if (evictionListener != null) {
        evictionListener.accept(key); // Удаляем из БД
      }
      return removed.value;
    }
    return null;
  }

  /**
   * Clears all items from the cache.
   */
  public synchronized void clear() {
    cache.clear();
    log.info("🗑 LFU Cache: Cache cleared.");
  }

  /**
   * Returns the number of items currently in the cache.
   *
   * @return the number of items in the cache
   */
  public synchronized int size() {
    return cache.size();
  }

  @Override
  public synchronized String toString() {
    return cache.toString();
  }
}