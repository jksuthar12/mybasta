package com.example.mybasta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class forgotpassword extends AppCompatActivity {
        Button ii,i,iii;
        EditText ee,v,vv,vvv;
        String link;
        String code;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpasswordlayout);
        i = findViewById(R.id.i);
        ee=findViewById(R.id.email);
        ImageButton close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        vv = findViewById(R.id.code);
        vvv=findViewById(R.id.p);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new backgroundtask5(forgotpassword.this).execute("check",ee.getText().toString());

            }
        });
        ii=findViewById(R.id.ii);
        iii=findViewById(R.id.iii);

        ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.equals(vv.getText().toString())){
                    iii.setVisibility(View.VISIBLE);
                    ii.setVisibility(View.GONE);
                    vvv.setVisibility(View.VISIBLE);
                }
            }
        });
        iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p=vvv.getText().toString();
               backgroundtask4 backgroundtask4=new backgroundtask4(forgotpassword.this);
                backgroundtask4.execute("change",p,ee.getText().toString(),link);
            }
        });
    }
    public class backgroundtask4 extends AsyncTask<String, Void, String> {

        Context context;
        String email, password;
        ProgressDialog progressDialog;

        backgroundtask4(Context c) {
            this.context = c;
            progressDialog = new ProgressDialog(forgotpassword.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("change")) {
                password = strings[1];
                email = strings[2];
                String linkk=strings[3];
                String link = "http://mybasta.timetablemanit.dx.am/changepassword.php";
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
                                URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+"&"+
                                URLEncoder.encode("linkk", "UTF-8") + "=" + URLEncoder.encode(linkk, "UTF-8");
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

            }
            super.onPostExecute(s);
        }


    }

    public class backgroundtask5 extends AsyncTask<String, Void, String> {

        Context context;
        String email, password;
        AlertDialog dialog;
        ProgressDialog progressDialog;

        backgroundtask5(Context c) {
            this.context = c;
            progressDialog = new ProgressDialog(forgotpassword.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("check")) {
                email = strings[1];
                String link = "http://mybasta.timetablemanit.dx.am/checkemail.php";
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
                        String inertial = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") ;

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
                Random generator = new Random();
                int num = generator.nextInt(899999) + 100000;
                code=String.valueOf(num);
                Log.i("otp",code);
                if(s.trim().equals("student"))
                {   link="student";
                  backgroundtask3 backgroundtask3=new backgroundtask3(forgotpassword.this);
                    backgroundtask3.execute("otp",String.valueOf(num),ee.getText().toString());
                }
                else if(s.trim().equals("faculty")){
                    link="faculty";
                   backgroundtask3 backgroundtask3=new backgroundtask3(forgotpassword.this);
                    backgroundtask3.execute("otp",String.valueOf(num),ee.getText().toString());
                }
            }
            super.onPostExecute(s);
        }


    }
    public class backgroundtask3 extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog dialog;
        ProgressDialog progressDialog;

        backgroundtask3(Context c) {
            this.context = c;
            progressDialog = new ProgressDialog(forgotpassword.this);
        }

        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("otp")) {
                String otp = strings[1];
                String email = strings[2];
                String link = "http://mybasta.timetablemanit.dx.am/email.php";
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
                        String inertial = URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            if (s != null) {
                progressDialog.dismiss();
                vv.setVisibility(View.VISIBLE);
                i.setVisibility(View.GONE);
                ii.setVisibility(View.VISIBLE);
            }

            super.onPostExecute(s);
        }
    }


}
