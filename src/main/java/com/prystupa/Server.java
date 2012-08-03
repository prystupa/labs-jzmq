package com.prystupa;

import org.zeromq.ZMQ;

/**
 * Hello world!
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (true) {
            // Wait for the next request from client
            final byte[] request = responder.recv(0);
            System.out.println("Received request: " + new String(request));

            // Do some work
            Thread.sleep(1000L);

            final byte[] reply = ("Reply " + new String(request)).getBytes();
            responder.send(reply, 0);
        }
    }
}
