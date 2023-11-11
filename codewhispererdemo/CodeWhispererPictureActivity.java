package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo;

import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.exception.CodeWhispererPictureException;
import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.model.CodeWhispererPictureMetadata;
import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.model.ImmutableCodeWhispererPictureMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;

import javax.inject.Inject;
import java.util.Optional;

public class CodeWhispererPictureActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeWhispererPictureActivity.class);

    private final CodeWhispererCache<String, CodeWhispererPictureMetadata> codeWhispererCache;
    private final CodeWhispererDB codeWhispererDB;
    private final CodeWhispererStorage codeWhispererStorage;

    @Inject
    public CodeWhispererPictureActivity(final CodeWhispererCache<String, CodeWhispererPictureMetadata> codeWhispererCache,
                                        final CodeWhispererDB codeWhispererDB,
                                        final CodeWhispererStorage codeWhispererStorage) {
        this.codeWhispererCache = codeWhispererCache;
        this.codeWhispererDB = codeWhispererDB;
        this.codeWhispererStorage = codeWhispererStorage;
    }

    /**
     * Persists the CodeWhisperer picture in the system.
     */
    public String savePicture(final String pictureId, final RequestBody picture) {
        Optional<CodeWhispererPictureMetadata> pictureMetadataFromCache = codeWhispererCache.get(pictureId);
        if (pictureMetadataFromCache.isPresent()) {
            String message = String.format("Picture already exists for pictureId %s", pictureId);
            LOGGER.error(message);
            throw new CodeWhispererPictureException(message);
        }
        String storageLocation = codeWhispererStorage.storePicture(pictureId, picture);

        CodeWhispererPictureMetadata pictureMetadata = ImmutableCodeWhispererPictureMetadata.builder()
                .withPictureId(pictureId)
                .withName(pictureId)
                .withStorageLocation(storageLocation)
                .build();
        codeWhispererCache.put(pictureId, pictureMetadata);
        codeWhispererDB.storePictureMetadata(pictureMetadata);
        return pictureId;
    }

    /**
     * Gets the CodeWhisperer picture location.
     */
    public String getPictureLocation(final String pictureId) {
        Optional<CodeWhispererPictureMetadata> pictureMetadataFromCache = codeWhispererCache.get(pictureId);
        if (pictureMetadataFromCache.isPresent()) {
            return pictureMetadataFromCache.get().getStorageLocation();
        } else {
            return codeWhispererDB.getPictureMetadata(pictureId).getStorageLocation();
        }
    }
}
