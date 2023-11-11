package com.amazon.aws.vector.consolas.controlplane.service.codewhispererdemo.exception;

public class CodeWhispererPictureException extends RuntimeException {
    public CodeWhispererPictureException(String message, Exception cause) {
        super(message, cause);
    }

    public CodeWhispererPictureException(String message) {
        super(message);
    }
}
