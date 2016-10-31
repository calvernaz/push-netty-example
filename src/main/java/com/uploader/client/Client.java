package com.uploader.client;

import java.io.FileNotFoundException;

public class Client {

    public static void main(String... args) throws FileNotFoundException {
        ClientEndpoint client = new ClientEndpoint(7890);
        client.upload(args[0]);
    }
}
