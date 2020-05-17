package com.heanzyzabala.idempotencykey;

import lombok.Getter;

@Getter
public class MissingIdempotencyKeyException extends RuntimeException {

    private final String headerName;
    private final String uri;

    public MissingIdempotencyKeyException(String headerName, String uri) {
        super("Missing idempotency KEY: "+ headerName + " in path: " + uri);
        this.headerName = headerName;
        this.uri = uri;
    }
}
