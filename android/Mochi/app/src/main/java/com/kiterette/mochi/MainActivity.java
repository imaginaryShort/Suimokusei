package com.kiterette.mochi;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity implements StartFragment.OnFragmentInteractionListener, SetupSuimokuseiFragment.OnFragmentInteractionListener, SetupUserFragment.OnFragmentInteractionListener, SensorEventListener {

    private StartFragment startFragment;
    private SetupSuimokuseiFragment setupSuimokuseiFragment;
    private SetupUserFragment setupUserFragment;

    private String murl = "http://imaginaryshort.com:7000";
    private SensorManager manager;
    private int AccelThreshold = 5;
    private float old_sensor_x, old_sensor_y, old_sensor_z;
    private String UserId;
    private boolean sensorEnable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        startFragment = new StartFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, startFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(this);
    }

    private void setTreeName(final String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(murl + "/home/add?name=" + name);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    String str = InputStreamToString(con.getInputStream());
                    Log.d("Log", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }

    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private void setUserName(final String name, final String order, final String homeid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(murl + "/user/add?name=" + name + "&homeid=" + homeid + "&order=" + order);
                    Log.d("TEST", url.toString());
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    String str = InputStreamToString(con.getInputStream());

                    JSONObject json = new JSONObject(str);
                    JSONArray users = json.getJSONArray("results");
                    JSONObject user = users.getJSONObject(0);
                    UserId = String.valueOf(user.optInt("last_insert_id()"));
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (old_sensor_x != 0 || old_sensor_y != 0 || old_sensor_z != 0) {
                float dx = Math.abs(old_sensor_x - sensorEvent.values[0]);
                float dy = Math.abs(old_sensor_x - sensorEvent.values[0]);
                float dz = Math.abs(old_sensor_x - sensorEvent.values[0]);
                if(sensorEnable && (dx > AccelThreshold || dy > AccelThreshold || dz > AccelThreshold)){
                    Log.d("Sensor", "Send!!!!!!!!!!!!!!");
                    sendUpdate(UserId, "still");
                }
            }
            old_sensor_x = sensorEvent.values[0];
            old_sensor_y = sensorEvent.values[1];
            old_sensor_z = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void sendUpdate(final String id, final String status){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(murl + "/add?id=" + id + "&status=" + status);
                    Log.d("URL", url.toString());
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    String str = InputStreamToString(con.getInputStream());
                    Log.d("HTTP", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }


    @Override
    public void onClickSetup() {
        setupSuimokuseiFragment = new SetupSuimokuseiFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, setupSuimokuseiFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onNext() {
        setupUserFragment = new SetupUserFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, setupUserFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
