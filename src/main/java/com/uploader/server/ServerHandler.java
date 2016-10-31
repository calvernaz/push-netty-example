package com.uploader.server;


import com.uploader.protocol.Envelope;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ServerHandler extends SimpleChannelInboundHandler<Envelope> {
    private FileOutputStream oout;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Envelope msg) throws Exception {
        FileChannel channel = oout.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getPayload());
        while (byteBuffer.hasRemaining()) {
            channel.write(byteBuffer);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        oout.flush();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        oout = new FileOutputStream("sample-copied.jar");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}