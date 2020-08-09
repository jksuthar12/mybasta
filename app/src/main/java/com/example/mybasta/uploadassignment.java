package com.example.mybasta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class uploadassignment extends AppCompatActivity {
                   Spinner department,semester,assignment,course;
                   ImageButton back;
                   TextView post;
                   Button selectfile;
    String urll="http://mybasta.timetablemanit.dx.am/uploadassign.php";
      String linkk;

                   EditText subject,date,marks;
                   Uri path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadassignment);
        department=(Spinner)findViewById(R.id.department);
        semester=(Spinner)findViewById(R.id.semester);
        back=(ImageButton)findViewById(R.id.back);
        subject=(EditText)findViewById(R.id.subject);
        date=(EditText)findViewById(R.id.date);
        marks=(EditText)findViewById(R.id.totalmarks);
        post=(TextView)findViewById(R.id.post);
        course=(Spinner)findViewById(R.id.course);
         selectfile=(Button)findViewById(R.id.choosefile);
        assignment=(Spinner)findViewById(R.id.assignmentno);
        String[] cours={"Course","B.Tech","M.Tech"};
        String[] sem={"semester","1","2","3","4","5","6","7","8"};
        String[] no={"assignment no.","1","2","3","4","5","6","7","8","9","10"};
        final String[] depart={"Department","Computer Science","Mechanical","Electrical","Electronics and Communication","Civil","Chemical","MSME"};
        ArrayAdapter<CharSequence>adapter=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,depart);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        department.setAdapter(adapter);
        ArrayAdapter<CharSequence>adapter1=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,cours);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        course.setAdapter(adapter1);
        ArrayAdapter<CharSequence>adapter2=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,sem);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semester.setAdapter(adapter2);
        ArrayAdapter<CharSequence>adapter3=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,no);
        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        assignment.setAdapter(adapter3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
               startActivityForResult(intent,1);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(department.getSelectedItem()=="Department")
                    Toast.makeText(getApplicationContext(),"select department",Toast.LENGTH_SHORT).show();
                else if(semester.getSelectedItem()=="semester")
                    Toast.makeText(getApplicationContext(),"select semester",Toast.LENGTH_SHORT).show();
                else if(course.getSelectedItem()=="Course")
                    Toast.makeText(getApplicationContext(),"select course",Toast.LENGTH_SHORT).show();
                else if(assignment.getSelectedItem()=="assignment no.")
                    Toast.makeText(getApplicationContext(),"select assignment no.",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(subject.toString()))
                    subject.setError("enter subject");
                else if(TextUtils.isEmpty(date.toString()))
                   date.setError("enter date");
                else if(TextUtils.isEmpty(marks.toString()))
                    marks.setError("enter total marks");
                else {
                    (new Upload(getApplicationContext(),path)).execute();
                }

            }
        });
    }
    class Upload extends AsyncTask<Void, Void, String> {
        public Context c;
        private Uri path;
        public Upload(Context c,Uri path){
            this.c=c;
            this.path=path;
        }
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog=new ProgressDialog(uploadassignment.this);
            dialog.setMessage("Uploading....");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            // Log.i("sun",s);
            dialog.cancel();
            backgroundtask2 backgroundtask=new backgroundtask2(uploadassignment.this);
            backgroundtask.execute("uploadassign",linkk,department.getSelectedItem().toString(),semester.getSelectedItem().toString()
                    ,subject.getText().toString(),course.getSelectedItem().toString(),date.getText().toString()
            ,marks.getText().toString(),assignment.getSelectedItem().toString());
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection conn=null;
            int maxsize=1024;
            try{
                URL url=new URL(urll);
                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setChunkedStreamingMode(1024);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection","Keep-alive");
                conn.setRequestProperty("Content-Type","multipart/form-data");
                OutputStream outputStream=conn.getOutputStream();
                InputStream inputStream=c.getContentResolver().openInputStream(path);
                int aail=inputStream.available();
                int busize=Math.min(aail,maxsize);
                byte[] buffer=new byte[busize];
                int read;
                while((read=inputStream.read(buffer,0,busize))!=-1){
                    outputStream.write(buffer,0,read);
                }
                outputStream.flush();
                inputStream.close();
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while((line=reader.readLine())!=null){
                    linkk=line;
                }
                reader.close();
                conn.disconnect();

                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            selectfile.setText("File Selected");
            path=data.getData();
        }
    }
    public class backgroundtask2 extends AsyncTask<String,Void,String> {

        Context context;
        String email,password;
        AlertDialog dialog;
        ProgressDialog progressDialog;
        backgroundtask2(Context c){
            this.context=c;
            progressDialog=new ProgressDialog(c);
        }
        @Override
        protected String doInBackground(String... strings) {
            String type=strings[0];
             if(type.equals("uploadassign")){
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
                shownotification("gfdgd");
                progressDialog.dismiss();
            }
            finish();
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

}
