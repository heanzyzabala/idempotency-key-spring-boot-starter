package com.heanzyzabala.idempotencykey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private IdempotencyKeyFilterProperties properties;
    private ObjectMapper objectMapper;
    private PathMatcher matcher;
    private IdempotencyKeyStore idempotencyKeyStore;

    public IdempotencyKeyFilter(IdempotencyKeyFilterProperties properties,
                                ObjectMapper objectMapper,
                                PathMatcher matcher,
                                IdempotencyKeyStore idempotencyKeyStore) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.matcher = matcher;
        this.idempotencyKeyStore = idempotencyKeyStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestIdHeader = request.getHeader(properties.getHeaderName());
        String uri = request.getRequestURI();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        if (isRequired(uri)) {
            if (requestIdHeader == null) {
                log.error("Missing idempotency KEY: {} in path: {}", properties.getHeaderName(), uri);
                throw new MissingIdempotencyKeyException(properties.getHeaderName(), uri);
            }
            if (idempotencyKeyStore.exists(requestIdHeader)) {
                log.info("Key exists: {}", requestIdHeader);
                Response r = idempotencyKeyStore.get(requestIdHeader);
                response.setStatus(r.getStatus());
                for (Map.Entry<String, String> e : r.getHeaders().entrySet()) {
                    response.setHeader(e.getKey(), e.getValue());
                }
                response.setContentType(r.getContentType());
                response.getOutputStream().write(r.getContent());
                response.setContentLength(r.getContentLength());
            } else {
                log.info("Key does not exists: {}", requestIdHeader);
                filterChain.doFilter(request, responseWrapper);
                responseWrapper.copyBodyToResponse();
                idempotencyKeyStore.save(requestIdHeader, toResponse(responseWrapper));
                log.info("saving with status: {}", responseWrapper.getStatus());
            }
        } else {
            filterChain.doFilter(request, responseWrapper);
        }
    }

    private Response toResponse(ContentCachingResponseWrapper response) {
        Response r = new Response();
        r.setStatus(response.getStatus());
        Map<String, String> headerMap = response.getHeaderNames().stream()
                .collect(Collectors.toMap(h -> h, response::getHeader));
        r.setHeaders(headerMap);
        r.setContentType(response.getContentType());
        r.setContent(response.getContentAsByteArray());
        r.setContentLength(response.getContentSize());
        return r;
    }

    private boolean isRequired(String uri) {
        return properties.getRequiredPaths().stream()
                .anyMatch(u -> matcher.match(uri, u));
    }
}
