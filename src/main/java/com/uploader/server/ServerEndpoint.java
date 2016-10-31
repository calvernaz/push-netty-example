package com.uploader.server;

import com.uploader.protocol.Decoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ServerEndpoint {
    private final int port;

    ServerEndpoint(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("decode", new Decoder());
                            socketChannel.pipeline().addLast("handler", new ServerHandler());
                        }
                    });

            Channel ch = bootstrap.bind(new InetSocketAddress(port)).sync().channel();
            ch.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
