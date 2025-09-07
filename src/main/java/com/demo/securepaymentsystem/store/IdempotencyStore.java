package com.demo.securepaymentsystem.store;

import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyStore {
    private final ConcurrentHashMap<String, Boolean> store = new ConcurrentHashMap<>();

    public boolean exists(String key) {
        return store.containsKey(key);
    }

    public void save(String key) {
        store.put(key, true);
    }
}
