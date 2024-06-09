package lld;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of LRU Cache
 */
public class LRUCache {
    public class LRU<K, V> {
        private final LinkedHashMap<K, V> cache;

        public LRU(int capacity) {
            cache = new LinkedHashMap(0, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > capacity;
                }
            };
        }

        public void put(K key, V value) {
            cache.put(key, value);
        }

        public V get(K key) {
            return cache.get(key);
        }

        public V remove(K key) {
            return cache.remove(key);
        }

        @Override
        public String toString() {
            return cache.toString();
        }
    }

    public static class LRU2<K, V> extends LinkedHashMap {
        final int capacity;
        public LRU2(int capacity) {
            super(0, 0.50f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > capacity;
        }
    }

    public static void main(String[] args) {
        LRUCache ins = new LRUCache();
        ins.testLRU();
    }

    private void testLRU() {
        LRU<Integer, Integer> cache = new LRU<>(3);
        //LRU2<Integer, Integer> cache = new LRU2<>(3);
        cache.put(1,10);
        cache.put(2,20);
        cache.put(3,30);
        cache.put(4,40);
        System.out.println(cache);
        cache.get(2);
        cache.put(5,50);
        cache.remove(4);
        System.out.println(cache);
    }
}
