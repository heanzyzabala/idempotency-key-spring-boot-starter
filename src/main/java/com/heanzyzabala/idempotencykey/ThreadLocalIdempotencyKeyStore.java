package com.heanzyzabala.idempotencykey;

import java.util.Map;

public class ThreadLocalIdempotencyKeyStore implements IdempotencyKeyStore {

    private ThreadLocal<Map<String, Object>> map = new ThreadLocal<>();

    @Override
    public void save(String key, Object value) {
        map.get().put(key, value);
    }

    @Override
    public Object get(String key) {
       return map.get().get(key);
    }

    @Override
    public boolean exists(String key) {
       return map.get().containsKey(key);
    }
}
