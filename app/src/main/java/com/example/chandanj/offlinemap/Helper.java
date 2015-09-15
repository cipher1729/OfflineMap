package com.example.chandanj.offlinemap;

/**
 * Created by cipher1729 on 9/14/2015.
 */
public class Helper {

    public class Step
    {
        public String distance;
        public String duration;
    }
    public class Leg
    {
        public Step [] steps;
    }
    public class Route
    {
        public Leg [] legs;
    }

}
