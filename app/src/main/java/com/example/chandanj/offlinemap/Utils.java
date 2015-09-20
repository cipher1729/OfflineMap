package com.example.chandanj.offlinemap;

/**
 * Created by cipher1729 on 9/19/2015.
 */
public class Utils {
    public static long parseDistance(String distance)
    {   long distanceInMetres = -1;
        String[] routeSplit = distance.split("[ ]");
        if(routeSplit[1].equals("m"))
            distanceInMetres=(long)Float.parseFloat(routeSplit[0]);
        if(routeSplit[1].equals("km"))
            distanceInMetres=(long)(Float.parseFloat(routeSplit[0])* 1000);
        return distanceInMetres;
    }

    public static long parseDuration(String duration)
    {
        long durationInMilliseconds=-1;
        String[] routeSplit = duration.split("[ ]");
        if(routeSplit.length==2) {
            //30 mins
            if (routeSplit[1].equals("min")|| routeSplit[1].equals("mins"))
                durationInMilliseconds = (long) (Float.parseFloat(routeSplit[0]) * 60 * 1000);
            //2 hours
            if (routeSplit[1].equals("hours") || routeSplit[1].equals("hour"))
                durationInMilliseconds = (long) (Float.parseFloat(routeSplit[0]) * 60 * 60 * 1000);
        }
        //5 hours 2 mins
        else if(routeSplit.length==4)
        {
            durationInMilliseconds = (long) ( (Float.parseFloat(routeSplit[0]) * 60 +Float.parseFloat(routeSplit[1]))*60*1000 );
        }
            return durationInMilliseconds;
    }

    public static long alertTime(String duration) {
        //no alert if 1 minute
        long alertTime = 0;
        String[] routeSplit = duration.split("[ ]");
        if (routeSplit.length == 2) {
            //30 mins
            if (routeSplit[1].equals("mins"))
                alertTime = 1 * 60 * 1000;      //1 min alert time
            //2 hours
            if (routeSplit[1].equals("hours") || routeSplit[1].equals("hour"))
                alertTime = 5 * 60 * 1000;      //5 minute alert time
        }
        //5 hours 2 mins
        else if (routeSplit.length == 4) {
            alertTime = 5 * 60 * 1000;      //5 minute alert time }
        }
        return alertTime;
    }
}
