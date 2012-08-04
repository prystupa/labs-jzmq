package com.prystupa.lru;

import org.zeromq.ZMQ;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/4/12
 * Time: 11:46 AM
 */
public class LruWorker implements Runnable {

    private final int id;

    public LruWorker(int id) {
        this.id = id;
    }

    @Override
    public void run() {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket worker = context.socket(ZMQ.REQ);
        worker.setIdentity(String.valueOf(id).getBytes());

        worker.connect("tcp://localhost:5555");

        int requestCount = 0;
        String request;
        do {
            worker.send("Ready".getBytes(), 0);
            request = new String(worker.recv(0));
            requestCount++;

            // do the work
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (request.compareTo("END") != 0);

        System.out.println("Workload handled: " + requestCount);
    }
}
