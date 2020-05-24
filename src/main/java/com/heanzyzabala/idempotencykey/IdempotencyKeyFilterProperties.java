package com.heanzyzabala.idempotencykey;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "idempotency-key")
public class IdempotencyKeyFilterProperties {

    private String headerName = "X-Idempotency-Key";
    private List<String> requiredPaths = new ArrayList<>();
}
