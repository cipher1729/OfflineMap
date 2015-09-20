package com.example.chandanj.offlinemap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    Button startButton, stopButton, fromButton, toButton;
    ListView listView;
    public TextView fromTextView, toTextView;
    public static TextView statusTextView;
    public static List<Route> routeList;
    public static long dist, trackerDist;
    public static long time, trackerTime;
    long currentTime;
    boolean keepScheduling= true;
    int REQUEST_FROM_PLACE_PICKER=1;
    int REQUEST_TO_PLACE_PICKER=2;
    String fromPlace, toPlace, fromLat, fromLong, toLat, toLong;
    ArrayAdapter<String> arrayAdapter;
    Runnable runnable;
    //to keep track of which leg is currrently being processed
    int stepTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        fromButton = (Button) findViewById(R.id.fromBtn);
        toButton = (Button) findViewById(R.id.toBtn);
        listView = (ListView) findViewById(R.id.listView);
        fromTextView = (TextView) findViewById(R.id.fromTextView);
        toTextView = (TextView) findViewById(R.id.toTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        //textView = (TextView) findViewById(R.id.textView);
        dist = 0;
        time = 0;
        addGUIListeners();

        runnable = new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpManager.getData("https://maps.googleapis.com/maps/api/directions/json?origin="+fromLat+","+fromLong+"&destination="+toLat+","+toLong+"&key=AIzaSyC9M0AmyxoajATobugixlWFd26f7kUKhkc&units=metric&mode=driving");
                routeList = JSONParser.parse(jsonData);
                stepTracker=0;
                trackerDist= Utils.parseDistance(routeList.get(0).legs[0].steps[0].distance);
                trackerTime = Utils.parseDuration(routeList.get(0).legs[0].steps[0].duration);
                //generate strings list
                ArrayList<String> valueList= new ArrayList<>();

                for(int i=0;i<routeList.get(0).legs[0].steps.length;i++)
                {
                    valueList.add(routeList.get(0).legs[0].steps[i].distance + "   " + routeList.get(0).legs[0].steps[i].duration);
                }

                arrayAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1, valueList);
                Runnable listViewRunnable = new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(arrayAdapter);
                        statusTextView.setText("Timer " +String.valueOf(keepScheduling) + " with next values "+ routeList.get(0).legs[0].steps[0].distance + " "+ routeList.get(0).legs[0].steps[0].duration );
                    }
                };
                runOnUiThread(listViewRunnable);
                //textView.setText(routeList.get(0).legs[0].steps[0].duration);
                Timer timer= new Timer();
                long alertTime = Utils.alertTime(MainActivity.routeList.get(0).legs[0].steps[0].duration);
                timer.schedule(new MapTimerTask(getApplicationContext(),stepTracker, timer, keepScheduling),trackerTime-alertTime);
            }
        };


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
        }
        float timeInHours = time / 60;
        */


    }

    private void startTracking() {

    }

    private void addGUIListeners()
    {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime = System.currentTimeMillis();
                startTracking();
            }
        });
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, REQUEST_FROM_PLACE_PICKER);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, REQUEST_TO_PLACE_PICKER);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_FROM_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
            fromPlace = String.valueOf(place.getName());
            fromLat = String.valueOf(place.getLatLng().latitude);
            fromLong = String.valueOf(place.getLatLng().longitude);
            /*final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }*/
           /* mViewName.setText(name);
            mViewAddress.setText(address);
            mViewAttributions.setText(Html.fromHtml(attributions));*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


        if (requestCode == REQUEST_TO_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
            toPlace = String.valueOf(place.getName());
            toLat = String.valueOf(place.getLatLng().latitude);
            toLong = String.valueOf(place.getLatLng().longitude);

            if(fromPlace!=null && toPlace!=null) {
                Thread routeThread = new Thread(runnable);
                fromTextView.setText(fromPlace);
                toTextView.setText(toPlace);
                routeThread.start();
                try {
                    routeThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /*final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }*/
           /* mViewName.setText(name);
            mViewAddress.setText(address);
            mViewAttributions.setText(Html.fromHtml(attributions));*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
