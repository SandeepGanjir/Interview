package lld;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of LRU Cache
 */
public class LRUCache {
    public class LRU<K, V> {
        private final LinkedHashMap<K, V> cache;

        public LRU(int i) {
            cache = new LinkedHashMap(0, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > i;
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

    public static void main(String[] args) {
        LRUCache ins = new LRUCache();
        ins.testLRU();
    }

    private void testLRU() {
        LRU<Integer, Integer> cache = new LRU<>(3);
        cache.put(1,10);
        cache.put(2,20);
        cache.put(3,30);
        cache.put(4,40);
        System.out.println(cache);
        cache.get(2);
        cache.remove(3);
        System.out.println(cache);
    }
}
