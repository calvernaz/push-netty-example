package com.uploader.client;

import com.google.common.io.ByteStreams;
import com.uploader.protocol.Encoder;
import com.uploader.protocol.Envelope;
import com.uploader.protocol.Type;
import com.uploader.protocol.Version;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;

public class ClientEndpoint {
    private final int port;

    public ClientEndpoint(int port) {
        this.port = port;
    }

    public void upload(String fileName) throws FileNotFoundException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.remoteAddress(new InetSocketAddress(port));
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("encode", new Encoder());
                    socketChannel.pipeline().addLast("handler", new ClientHandler());
                }
            });
            Channel channel = b.connect().syncUninterruptibly().channel();
            try {

            File file  = new File(fileName);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            InputStream is = Channels.newInputStream(raf.getChannel());
            byte[] bytes = ByteStreams.toByteArray(is);

            Envelope request = new Envelope();
            request.setType(Type.SOURCE);
            request.setVersion(Version.VERSION_1);
            request.setPayload(bytes);
            channel.write(request);
            /*
            ChunkedFile chunkedFile = null;
            try {
                chunkedFile = new ChunkedFile(raf, 8192);
                chunkedFile.
                //DefaultFileRegion fileRegion = new DefaultFileRegion(file, 0, file.length());
                ChannelFuture channelFuture = channel.write(chunkedFile, channel.newProgressivePromise());
                channelFuture.addListener(new ChannelProgressiveFutureListener() {
                    @Override
                    public void operationComplete(ChannelProgressiveFuture future) throws Exception {}

                    @Override
                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                        System.out.println("progress=" + progress + ", total=" + total);
                        System.out.flush();
                    }
                });*/
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                channel.flush();
                //System.out.println("File size: " + chunkedFile.length());
            }
            channel.close().syncUninterruptibly();
        } finally {

            workerGroup.shutdownGracefully();
        }
    }
}
