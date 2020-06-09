package com.heanzyzabala.idempotencykey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapKeyStore implements IdempotencyKeyStore {

    private Map<String, Response> map = new ConcurrentHashMap<>();

    @Override
    public void save(String key, Response value) {
        map.put(key, value);
    }

    @Override
    public Response get(String key) {
       return map.get(key);
    }

    @Override
    public boolean exists(String key) {
       return map.containsKey(key);
    }
}
