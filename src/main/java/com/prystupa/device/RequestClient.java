package com.prystupa.device;

import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 10:12 PM
 */
public class RequestClient {

    public static void main(String[] args) throws IOException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket client = context.socket(ZMQ.REQ);

        client.connect("tcp://localhost:5559");

        while (true) {
            System.out.println("Enter something to send request...");
            String request = new Scanner(System.in).nextLine();
            client.send(request.getBytes(), 0);
            byte[] reply = client.recv(0);
            System.out.println("Received reply: " + new String(reply));
        }
    }
}