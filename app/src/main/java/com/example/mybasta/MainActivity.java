package com.example.mybasta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button studnet, faculty, login,i,ii,iii;
    TextInputEditText email, password;
    String code;
    EditText vv,vvv;
    String link;
    Dialog dialog;
    EditText ee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studnet = (Button) findViewById(R.id.student);
        faculty = (Button) findViewById(R.id.faculty);
        login = (Button) findViewById(R.id.login);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        studnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected())
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(getApplicationContext(), studentregistraion.class));
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected())
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(getApplicationContext(), facultyregistraion.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected())
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(email.getText().toString()))
                     email.setError("enter email");
                else if(TextUtils.isEmpty(password.getText().toString()))
                    password.setError("enter password");
                else {
                    backgroundtask1 backgroundtask = new backgroundtask1(MainActivity.this);
                    backgroundtask.execute("login", email.getText().toString(), password.getText().toString());
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("myAppPreference", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Active");
        String passwor = sharedPreferences.getString("password", null);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected())
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        if (!email.equals("Active")) {
            backgroundtask1 backgroundtask = new backgroundtask1(MainActivity.this);
            backgroundtask.execute("login", email, passwor);
        }
        TextView forgot = (TextView) findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });


    }


        public class backgroundtask1 extends AsyncTask<String, Void, String> {

            Context context;
            String email, password;
            AlertDialog dialog;
            ProgressDialog progressDialog;

            backgroundtask1(Context c) {
                this.context = c;
                progressDialog = new ProgressDialog(MainActivity.this);
            }

            @Override
            protected String doInBackground(String... strings) {
                String type = strings[0];
                if (type.equals("login")) {
                    email = strings[1];
                    password = strings[2];
                    String link = "http://mybasta.timetablemanit.dx.am/login.php";
                    try {
                        URL url = new URL(link);
                        try {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                            String inertial = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                            bufferedWriter.write(inertial);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                            String result = "";
                            String line;
                            while ((line = bufferedReader.readLine()) != null)
                                result += line;
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            return result;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }


                return null;
            }

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();
            }

            private final String PREFERENCE_FILE_KEY = "myAppPreference";

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                if (s != null) {
                    progressDialog.dismiss();
                    if (s.trim().equals("success")) { //progressDialog.dismiss();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();
                        Intent intent = new Intent(context, facultyzone.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    } else if (s.trim().equals("ssucess")) {
                        progressDialog.dismiss();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();
                        Intent intent = new Intent(context, studentzone.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
                super.onPostExecute(s);
            }


        }



}

