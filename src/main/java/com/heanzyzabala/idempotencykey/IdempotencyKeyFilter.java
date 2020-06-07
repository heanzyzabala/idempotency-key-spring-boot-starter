package com.heanzyzabala.idempotencykey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private IdempotencyKeyFilterProperties properties;
    private PathMatcher matcher;
    private IdempotencyKeyStore idempotencyKeyStore;

    public IdempotencyKeyFilter(IdempotencyKeyFilterProperties properties,
                                PathMatcher matcher,
                                IdempotencyKeyStore idempotencyKeyStore) {
        this.properties = properties;
        this.matcher = matcher;
        this.idempotencyKeyStore = idempotencyKeyStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestIdHeader = request.getHeader(properties.getHeaderName());
        String uri = request.getRequestURI();
        if (isRequired(uri) && requestIdHeader == null) {
            log.error("Missing idempotency KEY: {} in path: {}", properties.getHeaderName(), uri);
            throw new MissingIdempotencyKeyException(properties.getHeaderName(), uri);
        } else {
            if (idempotencyKeyStore.exists(requestIdHeader)) {
            } else {
                idempotencyKeyStore.save(requestIdHeader, );
                filterChain.doFilter(request, response);
            }
        }
    }

    private boolean isRequired(String uri) {
        return properties.getRequiredPaths().stream()
                .anyMatch(u -> matcher.match(uri, u));
    }
}
