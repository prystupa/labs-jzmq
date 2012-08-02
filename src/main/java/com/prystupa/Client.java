package com.prystupa;

import org.zeromq.ZMQ;

/**
 * Hello world!
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REQ);

        System.out.println(String.format("Version string: %s, Version int: %d",
                ZMQ.getVersionString(),
                ZMQ.getFullVersion()));
        System.out.println("Connecting to hello world server...");
        socket.connect("tcp://localhost:5555");

        // Do 10 requests waiting each time for a response
        for (int i = 0; i < 10; i++) {
            // Create a Hello message
            // Ensure that the last byte of our message is 0
            String requestString = "Hello";
            byte[] request = requestString.getBytes();

            // Send the message
            System.out.println("Sending request " + i + "...");
            socket.send(request, 0);

            // Get the reply
            byte[] reply = socket.recv(0);
            // When displaying reply as String omit the last byte
            // because our server sent us 0-terminated string
            System.out.println("Received reply " + i + ": [" + new String(reply) + "]");
        }
    }
}
