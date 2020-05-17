package com.heanzyzabala.idempotencykey;

public interface IdempotencyKeyStore {

    void save(String key, Object value);
    boolean exists(String key);
}
