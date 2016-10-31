package com.uploader.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import static com.uploader.protocol.DecodingState.VERSION;

public class Decoder extends ReplayingDecoder<DecodingState> {

    private Envelope message = new Envelope();

    public Decoder() {
        super(VERSION);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch (state()) {
            case VERSION:
                this.message.setVersion(Version.fromByte(buffer.readByte()));
                checkpoint(DecodingState.TYPE);
            case TYPE:
                this.message.setType(Type.fromByte(buffer.readByte()));
                checkpoint(DecodingState.PAYLOAD_LENGTH);
            case PAYLOAD_LENGTH:
                int size = buffer.readInt();
                if (size <= 0) {
                    throw new Exception("Invalid content size");
                }
                byte[] content = new byte[size];
                this.message.setPayload(content);
                checkpoint(DecodingState.PAYLOAD);
            case PAYLOAD:
                buffer.readBytes(this.message.getPayload(), 0,
                        this.message.getPayload().length);
                checkpoint(VERSION);
                out.add(message);
                break;
            default:
                throw new Error("Shouldn't reach here.");
        }
    }

}
