package com.example.chandanj.offlinemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    Button startButton, stopButton;
    public static TextView textView;
    public static List<Route> routeList;
    public static long dist, trackerDist;
    public static long time, trackerTime;
    long currentTime;
    boolean keepScheduling= true;

    //to keep track of which leg is currrently being processed
    int stepTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        textView = (TextView) findViewById(R.id.textView);
        dist = 0;
        time = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpManager.getData("https://maps.googleapis.com/maps/api/directions/json?origin=33.308625,-111.942314&destination=33.305269,-111.944652&key=AIzaSyC9M0AmyxoajATobugixlWFd26f7kUKhkc&units=metric&mode=driving");
                routeList = JSONParser.parse(jsonData);
                stepTracker=0;
                trackerDist= Utils.parseDistance(routeList.get(0).legs[0].steps[0].distance);
                trackerTime = Utils.parseDuration(routeList.get(0).legs[0].steps[0].duration);
                textView.setText(routeList.get(0).legs[0].steps[0].duration);
                Timer timer= new Timer();
                timer.schedule(new MapTimerTask(getApplicationContext(),stepTracker, timer, keepScheduling),trackerTime);

            }
        };

        Thread routeThread = new Thread(runnable);
        routeThread.start();
        try {
            routeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        Totals
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
        }*/
        float timeInHours = time / 60;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime = System.currentTimeMillis();
                startTracking();
            }
        });
    }

    private void startTracking() {

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
