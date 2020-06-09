package com.heanzyzabala.idempotencykey;

public interface IdempotencyKeyStore {

    void save(String key, Response value);
    Response get(String key);
    boolean exists(String  key);
}
