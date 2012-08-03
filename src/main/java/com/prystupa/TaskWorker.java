package com.prystupa;

import org.zeromq.ZMQ;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 8:33 AM
 */
public class TaskWorker {
    public static void main(String[] args) throws IOException, InterruptedException {

        ZMQ.Context context = ZMQ.context(1);

        // socket to receive messages on
        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.connect("tcp://localhost:5557");

        // socket to send messages to
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.connect("tcp://localhost:5558");

        // process tasks forever
        while (true) {
            String string = new String(receiver.recv(0));
            long msec = Long.parseLong(string);

            // Simple progress indicator
            System.out.flush();
            System.out.print(string + ".");

            // do the work
            Thread.sleep(msec);

            // Send results to sink
            sender.send("".getBytes(), 0);
        }
    }
}
