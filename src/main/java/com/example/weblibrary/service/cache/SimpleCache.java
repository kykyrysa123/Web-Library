package com.example.weblibrary.service.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCache<K, V> {
  private final int maxSize;
  private final Map<K, CacheEntry<V>> cache = new HashMap<>();
  private Consumer<K> evictionListener;

  private static class CacheEntry<V> {
    V value;
    int frequency;

    CacheEntry(V value) {
      this.value = value;
      this.frequency = 1;
    }
  }

  public SimpleCache(int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max size must be greater than 0");
    }
    this.maxSize = maxSize;
  }

  // Добавляем сеттер для evictionListener
  public void setEvictionListener(Consumer<K> evictionListener) {
    this.evictionListener = evictionListener;
  }

  public synchronized V get(K key) {
    CacheEntry<V> entry = cache.get(key);
    if (entry == null) {
      log.info("⭕ LFU Cache: Item NOT found. Key: {}", key);
      return null;
    }
    entry.frequency++;
    log.info("❗LFU Cache: Item found. Key: {}, New Frequency: {}", key, entry.frequency);
    return entry.value;
  }

  public synchronized void put(K key, V value) {
    if (cache.containsKey(key)) {
      CacheEntry<V> entry = cache.get(key);
      entry.value = value;
      entry.frequency++;
      log.info("❗ LFU Cache: Item updated. Key: {}, New Frequency: {}", key, entry.frequency);
    } else {
      if (cache.size() >= maxSize) {
        evictLeastFrequentlyUsed();
      }
      cache.put(key, new CacheEntry<>(value));
      log.info("❗ LFU Cache: New item added. Key: {}", key);
    }
  }

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
      log.info("🗑 LFU Cache: Evicted item. Key: {}, Frequency at removal: {}", lfuKey, minFrequency);
      if (evictionListener != null) {
        evictionListener.accept(lfuKey);
      }
    }
  }

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

  public synchronized void clear() {
    cache.clear();
    log.info("🗑 LFU Cache: Cache cleared.");
  }

  public synchronized int size() {
    return cache.size();
  }

  @Override
  public synchronized String toString() {
    return cache.toString();
  }
}