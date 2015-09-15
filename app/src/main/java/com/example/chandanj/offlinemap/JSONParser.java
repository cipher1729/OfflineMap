package com.example.chandanj.offlinemap;

import com.example.chandanj.offlinemap.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cipher1729 on 8/29/2015.
 */
public class JSONParser {

    public static List<Helper.Route> parse(String content)
    {
        List<Helper.Route> routesList= new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(content);
            for(int i=0;i<arr.length();i++)
            {
                Helper.Route newRoute= new Helper().new Route();

                JSONObject routeJSON = arr.getJSONObject(i);
                JSONArray legsJSON = routeJSON.getJSONArray("legs");
                for(int j=0;j<legsJSON.length();j++)
                {
                        JSONObject legJSON = legsJSON.getJSONObject(i);
                        JSONArray stepsJSON = legJSON.getJSONArray("steps");
                        for(int k=0;k<stepsJSON.length();k++)
                        {
                            JSONObject stepJSON = stepsJSON.getJSONObject(i);
                            JSONObject distanceJSON = stepJSON.getJSONObject("distance");
                            JSONObject durationJSON = stepJSON.getJSONObject("duration");

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
