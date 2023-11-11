package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo;


import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.sql.Timestamp;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;

/**
 * Custom implementation of a cache using Hashmap to store CodeWhispererPictureMetadata
 */
public class CodeWhispererCache<String, CodeWhispererPictureMetadata> {
    private final HashMap<String, Pair<CodeWhispererPictureMetadata, Timestamp>> cacheMap;
    private final long EXPIRATION_TIME_IN_SECONDS = 10 * 60;

    public CodeWhispererCache() {
        this.cacheMap = new HashMap<>();
    }

    /**
     * Stores the metadata in the cache
     */
    public synchronized void put(String pictureId, CodeWhispererPictureMetadata metadata) {
        cacheMap.put(pictureId, Pair.of(metadata, Timestamp.from(Instant.now())));
    }

    /**
     * Gets the metadata from the cache
     */
    public synchronized Optional<CodeWhispererPictureMetadata> get(String key) {
        Optional<CodeWhispererPictureMetadata> metadata = Optional.empty();
        Pair<CodeWhispererPictureMetadata, Timestamp> cache = cacheMap.get(key);
        if (cache != null) {
            if (isExpired(cache.getRight())) {
                cacheMap.remove(key);
            } else{
                metadata = Optional.of(cache.getLeft());
            }
        }
        return metadata;
    }

    private boolean isExpired(Timestamp timestamp) {
        return timestamp.after(Timestamp.from(Instant.now().plus(EXPIRATION_TIME_IN_SECONDS, ChronoUnit.SECONDS)));
    }
}
