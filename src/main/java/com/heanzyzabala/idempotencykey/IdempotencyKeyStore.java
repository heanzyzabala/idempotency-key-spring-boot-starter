package com.heanzyzabala.idempotencykey;

public interface IdempotencyKeyStore {

    void save(String key, Object value);
    Object get(String key);
    boolean exists(String  key);
}
