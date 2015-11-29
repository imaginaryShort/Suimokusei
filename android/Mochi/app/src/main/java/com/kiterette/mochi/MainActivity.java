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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener, ShakeFragment.OnFragmentInteractionListener, SensorEventListener {
    private MainFragment mainFragment;
    private UserFragment userFragment;
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
        mainFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, mainFragment);
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

    @Override
    public void onNewButtonClick(String str) {
        setTreeName(str);
        nextFragment();
    }

    @Override
    public void onAddButtonClick(String str) {
        setTreeName(str);
        nextFragment();
    }

    private void nextFragment(){
        userFragment = new UserFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, userFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
                    Log.d("Log", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }

    @Override
    public void onNextUserButtonClick(String name, String order) {
        setUserName(name, order, "1");
        ShakeFragment fragment = new ShakeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        sensorEnable = true;
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
    public void onFragmentInteraction(Uri uri) {

    }
}
