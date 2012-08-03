package com.prystupa;

import org.zeromq.ZMQ;

import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 8/2/12
 * Time: 7:03 PM
 */
public class WeatherUpdateClient {

    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);

        System.out.println("Collecting updates from weather server…");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5556");

        // Subscribe to ZIP code, default to 10001 - NYC
        String filter = (args.length > 0) ? args[0] : "10001";
        subscriber.subscribe(filter.getBytes());

        long totalTemp = 0;
        // Process 100 updates
        for (int i = 0; i < 100; i++) {
            String string = new String(subscriber.recv(0));
            int zipcode, temperature, relHumidity;

            StringTokenizer sscanf = new StringTokenizer(string, " ");
            String t = sscanf.nextToken();
            zipcode = Integer.valueOf(t);

            t = sscanf.nextToken();
            temperature = Integer.valueOf(t);

            t = sscanf.nextToken();
            relHumidity = Integer.valueOf(t);

            totalTemp += temperature;
        }

        System.out.println("Total temperature for ZIP " + filter + " was " + (totalTemp / 100));
    }
}
