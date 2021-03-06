package com.uploader.protocol;

/**
 * Envelope type enum.
 */
public enum Type {
    SOURCE((byte) 0x01),
    PROCESSOR((byte) 0x02),
    STORE((byte) 0x03),
    UNKNOWN((byte) 0x00);

    private final byte b;

    private Type(byte b) {
        this.b = b;
    }

    public static Type fromByte(byte b) {
        for (Type code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public byte getByteValue() {
        return b;
    }
}