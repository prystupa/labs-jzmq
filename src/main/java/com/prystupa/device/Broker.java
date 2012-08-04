package com.prystupa.device;

import org.zeromq.ZMQ;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/3/12
 * Time: 9:57 PM
 */
public class Broker {

    public static void main(String[] args) throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend = context.socket(ZMQ.ROUTER);
        ZMQ.Socket backend = context.socket(ZMQ.DEALER);

        frontend.bind("tcp://*:5559");
        backend.bind("tcp://*:5560");

        ZMQ.Poller poller = context.poller(2);
        poller.register(frontend);
        poller.register(backend);

        while (true) {
            poller.poll();

            boolean more = poller.pollin(0);

            while (more) {
                byte[] message = frontend.recv(0);
                more = frontend.hasReceiveMore();
                backend.send(message, more ? ZMQ.SNDMORE : 0);
            }

            more = poller.pollin(1);
            while (more) {
                byte[] message = backend.recv(0);
                more = backend.hasReceiveMore();
                frontend.send(message, more ? ZMQ.SNDMORE : 0);
            }
        }

    }
}