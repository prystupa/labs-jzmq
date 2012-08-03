package com.prystupa;

import org.zeromq.ZMQ;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 8:39 AM
 */
public class TaskSink {

    public static void main(String[] args) throws IOException, InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.bind("tcp://*:5558");

        // Wait for the start of batch
        receiver.recv(0);

        // Start our clock now
        long start = System.currentTimeMillis();

        // Process 100 confirmations
        for (int i = 0; i < 100; i++) {
            String string = new String(receiver.recv(0));
            if ((i / 10) * 10 == i) {
                System.out.print(":");
            } else {
                System.out.print(".");
            }
            System.out.flush();
        }

        //  Calculate and report duration of batch
        long tend = System.currentTimeMillis();

        System.out.println("Total elapsed time: " + (tend - start) + " msec");
        receiver.close();
        context.term();
    }
}
