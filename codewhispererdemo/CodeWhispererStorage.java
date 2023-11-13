package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo;

import com.amazon.aws.vector.consolas.commons.exception.DependencyException;
import com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.exception.CodeWhispererPictureException;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

public class CodeWhispererStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeWhispererStorage.class);
    private final S3Client s3Client;
    private final String BUCKET_NAME = "CodeWhispererPictures";

    @Inject
    public CodeWhispererStorage(final S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String storePicture(final String pictureId, final RequestBody requestBody) {
        final PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(pictureId)
                .build();
        try {
            s3Client.putObject(objectRequest, requestBody);
            return UUID.randomUUID() + "-" + pictureId;
        } catch (Exception e) {
            String message = String.format("Exception occurred while storing metadata for pictureId: %s", pictureId);
            LOGGER.error(message, e);
            throw new CodeWhispererPictureException(message, e);
        }
    }

    public boolean doesObjectExist(final String pictureId) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(pictureId)
                .build();

        try {
            this.s3Client.headObject(headObjectRequest);
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            String message = String.format("Exception occurred while getting metadata for pictureId: %s", pictureId);
            LOGGER.error(message, e);
            throw new CodeWhispererPictureException(message, e);
        }
        return true;
    }

    public void deletePicture(final String pictureId) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(pictureId)
                .build();

        try {
            this.s3Client.deleteObject(deleteObjectRequest);
        } catch (NoSuchKeyException e) {
            // already deleted
            LOGGER.info("Already deleted");
        } catch (Exception e) {
            String message = String.format("Exception occurred while deleting metadata for pictureId: %s", pictureId);
            LOGGER.error(message, e);
            throw new CodeWhispererPictureException(message, e);
        }
    }
}
