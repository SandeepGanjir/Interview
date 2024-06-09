package lld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Create a cache system with MAX_CAPACITY with eviction policy of
 * least frequently used and then by least recently used.
 */
public class LFRUCache {
    public static void main(String[] args) {
        System.out.println("Least Frequently Recently Used cache system");
        testLRFU();
    }

    private static void testLRFU() {
        LRFU<String, Integer> cache = new LRFU<>(3);
        /*cache.put("One", 1);
        cache.put("Two", 2);
        cache.put("Three", 3);
        cache.get("One");
        cache.put("Four", 4);
        cache.get("One");
        cache.put("Four", 5);
        System.out.println(cache);

        cache = new LRFU<>(3);*/
        Random r = new Random();
        for (int i=0; i<30; i++) {
            String key = "K:" + r.nextInt(5);
            int type = r.nextInt(80) % 8;
            if (type == 0 && cache.get(key) != null) {
                System.out.println(" Removing : " + key);
                cache.remove(key);
            } else if (type < 4) {
                System.out.println(" Adding : " + key);
                cache.put(key, r.nextInt(5));
            } else {
                System.out.println(" Getting : " + key);
                cache.get(key);
            }
            System.out.println(cache);
        }
    }

    static class LRFU<K, V> {
        class Node<K, V> {
            final K key;
            V value;
            int freq;
            transient Node prev, next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.freq = 0;
                prev = next = null;
            }
        }

        private final int MAX_CAPACITY;
        private final Map<K, Node<K, V>> cache = new HashMap<>();
        private final Map<Integer, Node<K, V>> freqHead = new HashMap<>();
        private Node<K, V> head, tail;

        public LRFU(int capacity) {
            MAX_CAPACITY = capacity;
        }

        public void put(K key, V value) {
            if (cache.containsKey(key)) {
                cache.get(key).value = value;
            } else {
                while (cache.size() >= MAX_CAPACITY) {
                    // Evicting from tail which is Least Freq and Oldest Used
                    remove(tail.key);
                }
                // add new node to cache
                Node<K, V> temp = new Node<>(key, value);
                placeAtRightPosition(temp);
            }
        }

        public V get(K key) {
            Node<K, V> temp = cache.get(key);
            if (temp == null) {
                return null;
            }
            if (temp.prev != null && temp.prev.freq <= (temp.freq + 1)) {
                // reordering required
                remove(temp.key);
                temp.freq++;
                placeAtRightPosition(temp);
            } else {
                temp.freq++;
            }
            return temp.value;
        }

        public V remove(K key) {
            Node<K, V> temp = cache.remove(key);
            if (temp == null) return null;
            if (temp.prev != null) temp.prev.next = temp.next;
            if (temp.next != null) temp.next.prev = temp.prev;
            if (head == temp) head = temp.next;
            if (tail == temp) tail = temp.prev;
            if (freqHead.get(temp.freq) == temp) {
                if (temp.next != null && temp.freq == temp.next.freq) {
                    freqHead.put(temp.next.freq, temp.next);
                } else {
                    freqHead.remove(temp.freq);
                }
            }
            return temp.value;
        }

        @Override
        public String toString() {
            Gson gson = new GsonBuilder().create();
            StringBuffer sb = new StringBuffer();
            Node n = head;
            while (n != null) {
                sb.append(gson.toJson(n));
                n = n.next;
                if (n != null) sb.append(" -> ");
            }
            return sb.toString();
        }

        private void placeAtRightPosition(Node<K, V> node) {
            if (cache.containsKey(node.key))
                remove(node.key);
            cache.put(node.key, node);

            // find and place node correctly
            if (tail != null) {
                if (tail.freq > node.freq) {
                    node.next = null;
                    node.prev = tail;
                } else {
                    node.next = findRightNextNode(node);
                    node.prev = node.next.prev;
                }
            }

            if (node.next == null) tail = node;
            else node.next.prev = node;

            if (node.prev == null) head = node;
            else node.prev.next = node;

            freqHead.put(node.freq, node);
        }

        private Node findRightNextNode(Node<K, V> node) {
            int nextNodeFreq = node.freq;
            if (!freqHead.containsKey(nextNodeFreq)) {
                if (!freqHead.containsKey(--nextNodeFreq)) {
                    if (node.next != null && freqHead.containsKey(node.next.freq)) {
                        nextNodeFreq = node.next.freq;
                    } else {
                        for (nextNodeFreq = node.freq; nextNodeFreq >= tail.freq &&
                                !freqHead.containsKey(nextNodeFreq); nextNodeFreq--);
                        throw new RuntimeException("Should never reach here");
                    }
                }
            }
            return freqHead.get(nextNodeFreq);
        }
    }
}
