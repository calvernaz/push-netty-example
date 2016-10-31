package com.uploader.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Envelope> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Envelope message, ByteBuf buffer) throws Exception {
        buffer.writeByte(message.getVersion().getByteValue());
        buffer.writeByte(message.getType().getByteValue());
        buffer.writeInt(message.getPayload().length);
        buffer.writeBytes(message.getPayload());
    }
}
