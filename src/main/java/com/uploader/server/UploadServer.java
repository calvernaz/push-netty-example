package com.uploader.server;

public class UploadServer {

    public static void main(String... args) throws InterruptedException {
        ServerEndpoint endpoint = new ServerEndpoint(7890);
        endpoint.run();
    }
}
