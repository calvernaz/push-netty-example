package com.uploader.protocol;

public enum DecodingState {
    VERSION,
    TYPE,
    PAYLOAD_LENGTH,
    PAYLOAD
}
