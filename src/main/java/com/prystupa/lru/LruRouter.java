package com.prystupa.lru;

import org.zeromq.ZMQ;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/4/12
 * Time: 11:39 AM
 */
public class LruRouter {

    public static void main(String[] args) throws InterruptedException {

        final int numberOfWorkers = 10;
        for (int i = 0; i < numberOfWorkers; i++) {
            new Thread(new LruWorker(i)).start();
        }

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket router = context.socket(ZMQ.ROUTER);

        router.bind("tcp://*:5555");

        for (int i = 0; i < numberOfWorkers * 10; i++) {

            byte[] worker = router.recv(0);
            router.recv(0);   // empty
            router.recv(0);  // ready

            router.send(worker, ZMQ.SNDMORE);
            router.send("".getBytes(), ZMQ.SNDMORE);
            router.send("This is the request".getBytes(), 0);
        }

        for (int i = 0; i < numberOfWorkers; i++) {
            byte[] worker = router.recv(0);
            router.recv(0);   // empty
            router.recv(0);  // ready

            router.send(worker, ZMQ.SNDMORE);
            router.send("".getBytes(), ZMQ.SNDMORE);
            router.send("END".getBytes(), 0);
        }

    }
}