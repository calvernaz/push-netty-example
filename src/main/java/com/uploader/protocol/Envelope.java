package com.uploader.protocol;

public class Envelope {
    private Version version;
    private Type type;
    private byte[] payload;

    public Envelope() {
    }

    public Envelope(Version version, Type type, byte[] payload) {
        this.version = version;
        this.type = type;
        this.payload = payload;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
