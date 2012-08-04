package com.prystupa.device;

import org.zeromq.ZMQ;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 10:16 PM
 */
public class ReplyServer {

    public static void main(String[] args) throws IOException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket server = context.socket(ZMQ.REP);

        server.connect("tcp://localhost:5560");

        while (true) {
//            System.out.println("[Enter] to serve request...");
//            new Scanner(System.in).nextLine();
            String request = new String(server.recv(0));
            System.out.println("Received: " + request);
            String reply = "* " + request + " *";
            server.send(reply.getBytes(), 0);
        }
    }
}