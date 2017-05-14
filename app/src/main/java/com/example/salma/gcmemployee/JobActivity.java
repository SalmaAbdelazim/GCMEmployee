package com.example.salma.gcmemployee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class JobActivity extends AppCompatActivity {

    Button accept;
    Button deny;

    String API_KEY = "AAAAVOSQtCg:APA91bH4ndvHqWqFPg0ChQXGK7Uv0_ID_oJ0qWGJpKyVxgnLFtMDciCw-knMJFHbOab0oVHE4t6V9imFuw0piHRn23LJhOW3f7Q8k9w_RYy-MmxitDj3BLSEXjD_jMRXZ8UeYVT-gxqM";


    String to ="/topics/manager";
    String time;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();


        time=intent.getExtras().getString("time");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Log.i("result","on create");
        accept=(Button)findViewById(R.id.accept);
        deny=(Button)findViewById(R.id.deny);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap jobs = GCMReceiverService.jobs;

                if (jobs.get(time).equals("available")) {

                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("result", "on run thread");

                            try {
                                // Prepare JSON containing the GCM message content. What to send and where to send.
                                JSONObject jGcmData = new JSONObject();
                                JSONObject jData = new JSONObject();
                                jData.put("message", "accept");
                                jData.put("acceptedToken", GCMRegistration.token);
                                Log.i("time in accept thread", time);
                                jData.put("time", time);

                                jGcmData.put("to", to);


                                jGcmData.put("data", jData);

                                // Create connection to send GCM Message request.
                                URL url = new URL("https://android.googleapis.com/gcm/send");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestProperty("Authorization", "key=" + API_KEY);
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);

                                Log.i("result", "made conn");
                                // Send GCM message content.
                                OutputStream outputStream = conn.getOutputStream();
                                outputStream.write(jGcmData.toString().getBytes());

                                // Read GCM response.
                                InputStream inputStream = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                                StringBuilder str = new StringBuilder();
                                String line = null;


                                while ((line = reader.readLine()) != null) {

                                    str.append(line);

                                }

                                String resultFromWs = str.toString();
                                Log.i("result", resultFromWs);
                            } catch (IOException e) {

                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    });
                    th.start();

                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"sorry ride has been already token",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}


