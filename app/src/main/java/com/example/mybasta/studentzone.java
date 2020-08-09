package com.example.mybasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class studentzone extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
   Toolbar toolbar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentzone);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigationvieew);
            new background1().execute();
        getSupportFragmentManager().beginTransaction().replace(R.id.fram,new studentassignment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.assignment)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram,new studentassignment()).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram,new studentnotes()).commit();
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.out){
            if(item.getItemId()==R.id.out){
                SharedPreferences myPrefs = getSharedPreferences("myAppPreference",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                Intent i=new Intent(studentzone.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        return true;
    }
    class background1 extends AsyncTask<String, String, String> {


        background1() {


        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
            String emaill = sharedPreferences.getString("email", null);
            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/getinfostudent.php";
            try {
                URL url = new URL(host);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    String inertial = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(emaill, "UTF-8");
                    bufferedWriter.write(inertial);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String line;
                    StringBuffer buffer = new StringBuffer("");
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line);
                        break;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    result = buffer.toString();
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null) {
                if (s.trim().equals("not"))
                    Toast.makeText(getApplicationContext(), "data not available", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String name = jsonObject.getString("department");
                        String r = jsonObject.getString("semester");
                        String d = jsonObject.getString("course");
                        String sh = jsonObject.getString("scholar");
                        SharedPreferences sharedPref = getSharedPreferences(
                                "myAppPreference", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("department", name);
                        editor.putString("semester", r);
                        editor.putString("course", d);
                        editor.putString("scholar", sh);
                        //  Toast.makeText(getApplicationContext(),sh,Toast.LENGTH_SHORT).show();
                        editor.commit();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);

        }

    }

    @Override
    public void onBackPressed() {
      finish();
            super.onBackPressed();
    }
}
