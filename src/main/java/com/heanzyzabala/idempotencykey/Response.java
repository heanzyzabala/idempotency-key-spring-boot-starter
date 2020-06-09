package com.heanzyzabala.idempotencykey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class Response {

    private int status;
    private String contentType;
    private int contentLength;
    private byte[] content;
    private Map<String, String> headers;
}
