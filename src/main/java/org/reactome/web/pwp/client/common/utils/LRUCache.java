package org.reactome.web.pwp.client.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */

/**
 * LRUCache with two possible ctr (standard-size of Cache is 5).
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int MAX_SIZE;

    public LRUCache() {
        MAX_SIZE = 5;
    }
    public LRUCache(int size) {
        this.MAX_SIZE = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }
}
