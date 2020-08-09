package com.example.mybasta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

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


public class otp extends AppCompatActivity {
  Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        confirm=(Button)findViewById(R.id.confirm);
        final PinView pinVie=(PinView)findViewById(R.id.firstPinView);
        Intent intent=getIntent();
        TextView ot=(TextView)findViewById(R.id.ot);
        final TextView err=(TextView)findViewById(R.id.error);
        String type=intent.getStringExtra("type");
        if(type.equals("faculty")){
           final String firstname=intent.getStringExtra("firstname");
            final String middlename=intent.getStringExtra("middlename");
            final String lastname=intent.getStringExtra("lastname");
            final String dateofbirth=intent.getStringExtra("dateofbirth");
            final String email=intent.getStringExtra("email");
            final String password=intent.getStringExtra("password");
            final String mobileno=intent.getStringExtra("mobileno");
            final String address=intent.getStringExtra("address");
            final String depart=intent.getStringExtra("depart");
            final String gender=intent.getStringExtra("gender");
            SharedPreferences sharedPreferences=getSharedPreferences("myAppPreference",MODE_PRIVATE);
            final String otp=sharedPreferences.getString("otp","000000");
            ot.setText(otp);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("otp",otp);
                    if(otp.equals(pinVie.getText().toString())){
                        backgroundtask1 backgroundtask=new backgroundtask1(otp.this);
                        backgroundtask.execute("faculty",firstname,middlename,lastname,
                                dateofbirth,email, mobileno,password,address,depart, gender);
                    }
                    else
                        err.setVisibility(View.VISIBLE);

                }
            });

          }
         if(type.equals("student")){
            final String firstname=intent.getStringExtra("firstname");
            final String middlename=intent.getStringExtra("middlename");
            final String lastname=intent.getStringExtra("lastname");
            final String dateofbirth=intent.getStringExtra("dateofbirth");
            final String email=intent.getStringExtra("email");
            final String password=intent.getStringExtra("password");
            final String mobileno=intent.getStringExtra("mobileno");
            final String address=intent.getStringExtra("address");
            final String depart=intent.getStringExtra("depart");
            final String gender=intent.getStringExtra("gender");
            final String scholar=intent.getStringExtra("scholar");
            final String semester=intent.getStringExtra("semester");
            final String course=intent.getStringExtra("course");
             SharedPreferences sharedPreferences=getSharedPreferences("myAppPreference",MODE_PRIVATE);
             final String otp=sharedPreferences.getString("otp","000000");
             ot.setText(otp);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences=getSharedPreferences("myAppPreference",MODE_PRIVATE);
                    String otp=sharedPreferences.getString("otp","000000");
                    Log.i("otp",otp);
                    if(otp.equals(pinVie.getText().toString())){

                        backgroundtask1 backgroundtask=new backgroundtask1(otp.this);
                        backgroundtask.execute("student",firstname,middlename,lastname,scholar,semester,
                                dateofbirth,email, mobileno,password,address,depart,course, gender);
                    }
                    else
                        err.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    public class backgroundtask1 extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog dialog;
        ProgressDialog progressDialog;
        backgroundtask1(Context c){
            this.context=c;
            progressDialog=new ProgressDialog(otp.this);
        }
        protected String doInBackground(String... strings) {
            String type=strings[0];
            if(type.equals("student")){
                String firstname=strings[1];
                String middlename=strings[2];
                String lastname=strings[3];
                String scholar=strings[4];
                String semester=strings[5];
                String dob=strings[6];
                String email=strings[7];
                String mobile=strings[8];
                String password=strings[9];
                String address=strings[10];
                String depart=strings[11];
                String course=strings[12];
                String gender=strings[13];
                String link="http://mybasta.timetablemanit.dx.am/reg.php";
                try {
                    URL url=new URL(link);
                    try {
                        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream=httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
                        BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
                        String inertial= URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(firstname,"UTF-8")+"&"+
                                URLEncoder.encode("middlename","UTF-8")+"="+URLEncoder.encode(middlename,"UTF-8")+"&"+
                                URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")+"&"+
                                URLEncoder.encode("scholar","UTF-8")+"="+URLEncoder.encode(scholar,"UTF-8")+"&"+
                                URLEncoder.encode("semester","UTF-8")+"="+URLEncoder.encode(semester,"UTF-8")+"&"+
                                URLEncoder.encode("dateofbirth","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                                URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                                URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(depart,"UTF-8")+"&"+
                                URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(course,"UTF-8")+"&"+
                                URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8");
                        bufferedWriter.write(inertial);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream=httpURLConnection.getInputStream();
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                        String result="";
                        String line;
                        while((line=bufferedReader.readLine())!=null)
                            result+=line;
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
          if(type.equals("faculty")){
                String firstname=strings[1];
                String middlename=strings[2];
                String lastname=strings[3];
                String dob=strings[4];
                String email=strings[5];
                String mobile=strings[6];
                String password=strings[7];
                String address=strings[8];
                String depart=strings[9];
                String gender=strings[10];
                String link="http://mybasta.timetablemanit.dx.am/facultydata.php";
                try {
                    URL url=new URL(link);
                    try {
                        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream=httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
                        BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
                        String inertial= URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(firstname,"UTF-8")+"&"+
                                URLEncoder.encode("middlename","UTF-8")+"="+URLEncoder.encode(middlename,"UTF-8")+"&"+
                                URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")+"&"+
                                URLEncoder.encode("dateofbirth","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                                URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"+
                                URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(depart,"UTF-8")+"&"+
                                URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8");
                        bufferedWriter.write(inertial);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream=httpURLConnection.getInputStream();
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                        String result="";
                        String line;
                        while((line=bufferedReader.readLine())!=null)
                            result+=line;
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
            Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            if(s!=null) {
                progressDialog.dismiss();
               startActivity(new Intent(otp.this,MainActivity.class));
               finish();
            }
            super.onPostExecute(s);
        }


    }


}
