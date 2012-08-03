package com.prystupa;

import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 8:15 AM
 */
public class TaskVent {

    public static void main(String[] args) throws IOException {

        ZMQ.Context context = ZMQ.context(1);

        // Socket to send messages on
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.bind("tcp://*:5557");

        ZMQ.Socket sink = context.socket(ZMQ.PUSH);
        sink.connect("tcp://localhost:5558");

        System.out.println("Press [Enter] when workers are ready...");
        System.in.read();
        System.out.println("Sending tasks to workers...");

        // The first message is empty to signal start of the batch
        sink.send("start".getBytes(), 0);

        Random random = new Random();

        long totalCost = 0;
        for (int i = 0; i < 100; i++) {

            int workload = random.nextInt(100) + 1;
            System.out.println(workload + ".");
            sender.send(String.valueOf(workload).getBytes(), 0);

            totalCost += workload;
        }

        System.out.println("Total expected cost: " + totalCost);
    }
}
