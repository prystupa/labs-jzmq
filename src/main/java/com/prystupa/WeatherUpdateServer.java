package com.prystupa;

import org.zeromq.ZMQ;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/2/12
 * Time: 6:58 PM
 */
public class WeatherUpdateServer {

    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5556");
        publisher.bind("ipc://weather");

        final Random random = new Random();
        while (true) {
            int zipcode, temperature, relHumidity;
            zipcode = random.nextInt(100000) + 1;
            temperature = random.nextInt(215) - 80 + 1;
            relHumidity = random.nextInt(50) + 10 + 1;

            // Send message to all subscribers
            String update = String.format("%05d %d %d", zipcode, temperature, relHumidity);

            publisher.send(update.getBytes(), 0);
        }
    }
}
