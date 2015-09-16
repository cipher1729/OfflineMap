package com.example.chandanj.offlinemap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    List<Route> routeList;
    float dist;
    float time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.queryButton);
        textView = (TextView) findViewById(R.id.textView);
        dist = 0;
        time = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpManager.getData("https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyC9M0AmyxoajATobugixlWFd26f7kUKhkc");
                routeList = JSONParser.parse(jsonData);
                Log.d("", "");
            }
        };

        Thread routeThread = new Thread(runnable);
        routeThread.start();
        try {
            routeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < routeList.size(); i++) {
            for (int j = 0; j < routeList.get(i).legs.length; j++) {
                for (int k = 0; k < routeList.get(i).legs[j].steps.length; k++) {
                    String distString = routeList.get(i).legs[j].steps[k].distance;
                    String durationString = routeList.get(i).legs[j].steps[k].duration;
                    String[] routeSplit = distString.split("[ ]");
                    dist += Float.parseFloat(routeSplit[0]);
                    routeSplit = durationString.split("[ ]");
                    time += Float.parseFloat(routeSplit[0]);
                }
            }
        }
        float timeInHours = time / 60;
        
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
