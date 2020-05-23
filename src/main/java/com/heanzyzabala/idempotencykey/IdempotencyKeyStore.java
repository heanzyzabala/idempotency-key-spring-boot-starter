package com.heanzyzabala.idempotencykey;

public interface IdempotencyKeyStore<K, V> {

    void save(K key, V value);
    V get(K key);
    boolean exists(K key);
}
