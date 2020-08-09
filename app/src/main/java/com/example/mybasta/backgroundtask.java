package com.example.mybasta;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

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

public class backgroundtask extends AsyncTask<String,Void,String> {

    Context context;
    String email,password;
    AlertDialog dialog;
    ProgressDialog progressDialog;
    backgroundtask(Context c){
        this.context=c;
        progressDialog=new ProgressDialog(c);
    }
    @Override
    protected String doInBackground(String... strings) {
        String type=strings[0];
        if(type.equals("login")){
          email=strings[1];
             password=strings[2];
            String link="http://mybasta.timetablemanit.dx.am/login.php";
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
                    String inertial= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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
        else if(type.equals("uploadnotes")){
            String urlll=strings[1];
            String department=strings[2];
            String semester=strings[3];
            String subject=strings[4];
            String course=strings[5];
            SharedPreferences sharedPreferences=context.getSharedPreferences("myAppPreference",Context.MODE_PRIVATE);
            String emaill=sharedPreferences.getString("email",null);
            String link="http://mybasta.timetablemanit.dx.am/uploadnotes.php";
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
                    String inertial= URLEncoder.encode("url","UTF-8")+"="+URLEncoder.encode(urlll,"UTF-8")+"&"+
                            URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(department,"UTF-8")
                            +"&"+
                            URLEncoder.encode("semester","UTF-8")+"="+URLEncoder.encode(semester,"UTF-8")
                            +"&"+
                            URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")
                            +"&"+
                            URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(course,"UTF-8") +"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emaill,"UTF-8");

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
        else if(type.equals("uploadassign")){
            String urlll=strings[1];
            String department=strings[2];
            String semester=strings[3];
            String subject=strings[4];
            String course=strings[5];
            String date=strings[6];
            String marks=strings[7];
            String assignment=strings[8];
            SharedPreferences sharedPreferences=context.getSharedPreferences("myAppPreference",Context.MODE_PRIVATE);
            String emaill=sharedPreferences.getString("email",null);
            String link="http://mybasta.timetablemanit.dx.am/uploadassignment.php";
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
                    String inertial= URLEncoder.encode("url","UTF-8")+"="+URLEncoder.encode(urlll,"UTF-8")+"&"+
                            URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(department,"UTF-8")
                            +"&"+
                            URLEncoder.encode("semester","UTF-8")+"="+URLEncoder.encode(semester,"UTF-8")
                            +"&"+
                            URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")
                            +"&"+
                            URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(course,"UTF-8") +"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emaill,"UTF-8")
                            +"&"+
                            URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")
                            +"&"+
                            URLEncoder.encode("marks","UTF-8")+"="+URLEncoder.encode(marks,"UTF-8")+"&"+
                            URLEncoder.encode("assignment","UTF-8")+"="+URLEncoder.encode(assignment,"UTF-8");
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
        else if(type.equals("submission")){
            String urlll=strings[1];
            String assignment=strings[2];
            String subject=strings[3];
            String emaill=strings[4];
            String department=strings[5];
            String course=strings[6];
            String semester=strings[7];
            String scholar=strings[8];
            String link="http://mybasta.timetablemanit.dx.am/studentsubmission.php";
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
                    String inertial= URLEncoder.encode("url","UTF-8")+"="+URLEncoder.encode(urlll,"UTF-8")+"&"+
                            URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(department,"UTF-8")
                            +"&"+
                            URLEncoder.encode("semester","UTF-8")+"="+URLEncoder.encode(semester,"UTF-8")
                            +"&"+
                            URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")
                            +"&"+
                            URLEncoder.encode("course","UTF-8")+"="+URLEncoder.encode(course,"UTF-8") +"&"+
                            URLEncoder.encode("faculty","UTF-8")+"="+URLEncoder.encode(emaill,"UTF-8")
                            +"&"+
                            URLEncoder.encode("assignment","UTF-8")+"="+URLEncoder.encode(assignment,"UTF-8")
                            +"&"+
                            URLEncoder.encode("scholar","UTF-8")+"="+URLEncoder.encode(scholar,"UTF-8");
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
             progressDialog.setMessage("Uploading...");
             progressDialog.show();
        super.onPreExecute();
    }
    private final String PREFERENCE_FILE_KEY = "myAppPreference";
    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    if(s!=null) {
       progressDialog.dismiss();
       shownotification(s);
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
           // progressDialog.dismiss();
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
    public void shownotification(String msg){
        Uri notification_sound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes attributes=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
             attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
        }
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("1" , "notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(notification_sound,attributes);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);}

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context,"1")
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.ic_add_black_24dp)
                        .setContentTitle("Notification")
                        .setContentText("file uploaded successfully");

        // Add as notification
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


}
