package com.example.chandanj.offlinemap;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cipher1729 on 8/29/2015.
 */
public class JSONParser {

    public static List<Route> parse(String content)
    {
        List<Route> routesList= new ArrayList<>();
        try {
            JSONObject arr = new JSONObject(content);
            JSONArray routes = arr.getJSONArray("routes");
            for(int i=0;i<routes.length();i++)
            {
                Route newRoute= new Route();
                JSONObject routeJSON = routes.getJSONObject(i);
                JSONArray legsJSON = routeJSON.getJSONArray("legs");
                newRoute.legs = new Leg[legsJSON.length()];
                for(int j=0;j<legsJSON.length();j++)
                {
                        JSONObject legJSON = legsJSON.getJSONObject(j);
                        JSONArray stepsJSON = legJSON.getJSONArray("steps");
                        newRoute.legs[j] = new Leg();
                        newRoute.legs[j].steps = new Step [stepsJSON.length()];
                        for(int k=0;k<stepsJSON.length();k++)
                        {
                            JSONObject stepJSON = stepsJSON.getJSONObject(k);
                            JSONObject distanceJSON = stepJSON.getJSONObject("distance");
                            JSONObject durationJSON = stepJSON.getJSONObject("duration");
                            newRoute.legs[j].steps[k]= new Step();
                            newRoute.legs[j].steps[k].distance = distanceJSON.getString("text");
                            newRoute.legs[j].steps[k].duration = durationJSON.getString("text");
                        }
                }
                routesList.add(newRoute);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routesList;
    }
}
