package com.kiterette.mochi;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private String murl = "http://imaginaryshort.com:7000";
    private SensorManager manager;
    private int AccelThreshold = 5;
    private float old_sensor_x, old_sensor_y, old_sensor_z;
    private TextView tid, tname, tstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        tid = (TextView)findViewById(R.id.id_editText);
        tname = (TextView)findViewById(R.id.name_editText);
        tstatus = (TextView)findViewById(R.id.status_editText);

        //Button b = (Button)findViewById(R.id.button);
        //b.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                try {
        //                    URL url = new URL(murl);
        //                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
        //                    String str = InputStreamToString(con.getInputStream());
        //                    JSONObject json = new JSONObject(str);
        //                    JSONArray users = json.getJSONArray("users");
        //                    for(int i=0; i<users.length()-1; i++){
        //                        JSONObject user = users.getJSONObject(0);
        //                        Log.d("Log", user.optString("Name"));
        //                        Log.d("Log", user.optString("Status"));
        //                    }
        //                } catch(Exception ex) {
        //                    System.out.println(ex);
        //                }
        //            }
        //        }).start();
        //    }
        //});


        Button b2 = (Button)findViewById(R.id.register_button);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(murl + "/add?id=100&name=teseeeeet");
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            String str = InputStreamToString(con.getInputStream());
                        } catch(Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }).start();
            }
        });
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (old_sensor_x != 0 || old_sensor_y != 0 || old_sensor_z != 0) {
                float dx = Math.abs(old_sensor_x - sensorEvent.values[0]);
                float dy = Math.abs(old_sensor_x - sensorEvent.values[0]);
                float dz = Math.abs(old_sensor_x - sensorEvent.values[0]);
                if(dx > AccelThreshold || dy > AccelThreshold || dz > AccelThreshold){
                    Log.d("Sensor", "Send!!!!!!!!!!!!!!");
                    sendUpdate();
                }
            }
            old_sensor_x = sensorEvent.values[0];
            old_sensor_y = sensorEvent.values[1];
            old_sensor_z = sensorEvent.values[2];
        }
    }

    public void sendUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String id = tid.getText().toString();
                    String name =tname.getText().toString();
                    String status = tstatus.getText().toString();
                    URL url = new URL(murl + "/add?id=" + id + "&name=" + name + "&status=" + status);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    String str = InputStreamToString(con.getInputStream());
                    Log.d("TAG", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
