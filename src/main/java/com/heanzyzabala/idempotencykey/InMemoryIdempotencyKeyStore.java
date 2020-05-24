package com.heanzyzabala.idempotencykey;

import java.util.HashMap;
import java.util.Map;

public class InMemoryIdempotencyKeyStore implements IdempotencyKeyStore {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public void save(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object get(String key) {
       return map.get(key);
    }

    @Override
    public boolean exists(String key) {
       return map.containsKey(key);
    }
}
