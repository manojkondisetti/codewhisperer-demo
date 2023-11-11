package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.model;

import org.immutables.value.Value;
@Value.Immutable
@Value.Style(
        get = {"is*", "get*"},
        init = "with*",
        validationMethod = Value.Style.ValidationMethod.NONE,
        depluralize = true
)
public interface CodeWhispererPictureMetadata {

    String getPictureId();
    String getStorageLocation();
    String getName();
}
