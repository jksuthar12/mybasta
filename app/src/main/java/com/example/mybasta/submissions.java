package com.example.mybasta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class submissions extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    List<data> dat;
   String sh,d,course,semester,subject,assignment;
         androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        listView = (ListView) findViewById(R.id.submissions);
        progressBar = findViewById(R.id.pp);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
         sh = sharedPreferences.getString("email", "null");
        Intent intent = getIntent();
         d = intent.getStringExtra("department");
          course = intent.getStringExtra("course");
        semester = intent.getStringExtra("semester");
         subject = intent.getStringExtra("subject");
           assignment = intent.getStringExtra("assignment");
        setSupportActionBar(toolbar);
          new background1().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.list){
            ProgressDialog progressDialog=new ProgressDialog(submissions.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final File file=new File(Environment.getExternalStorageDirectory(),dat.get(0).getCourse()+"_"+dat.get(0).getDepartment()+"_"
            +dat.get(0).getSemester()+"_"+dat.get(0).getSubject()+".txt");
            try {
                FileOutputStream fileOutputStream=new FileOutputStream(file);
                OutputStreamWriter writer=new OutputStreamWriter(fileOutputStream);
                writer.write("Course : "+dat.get(0).getCourse());
                writer.append("\r\n");
                writer.append("Department : "+dat.get(0).getDepartment());
                writer.append("\r\n");
                writer.append("Semester : "+dat.get(0).getSemester());
                writer.append("\r\n");
                writer.append("Subject : "+dat.get(0).getSubject());
                writer.append("\r\n");
                writer.append("Assignment : "+dat.get(0).getAssignment());
                writer.append("\r\n");
               for(int i=0;i<dat.size();i++){
                   writer.append(String.valueOf(i+1)+".");
                   writer.append("  ");
                   writer.append(dat.get(i).getScholar());
                   writer.append("  ");
                   writer.append(dat.get(i).getMarks());
                   writer.append("\r\n");
               }
                Snackbar.make(findViewById(android.R.id.content), "File has been save", Snackbar.LENGTH_LONG)
                        .setAction("OPEN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent();
                                i.setDataAndType(Uri.fromFile(file), "text/plain");
                                startActivity(i);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.blue))
                        .show();

                writer.close();
                fileOutputStream.close();
                progressDialog.cancel();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class background1 extends AsyncTask<String, String, String> {


        background1() {
            dat = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/submittedassign.php";
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
                    String inertial = URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(d, "UTF-8")
                            + "&" + URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8")
                            + "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8")
                            + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(sh, "UTF-8")
                            + "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8")
                            + "&" + URLEncoder.encode("assignment", "UTF-8") + "=" + URLEncoder.encode(assignment, "UTF-8");
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            progressBar.setVisibility(View.GONE);
            if (s.trim().equals("not"))
                Toast.makeText(getApplicationContext(), "data not available", Toast.LENGTH_SHORT).show();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String scholar = jsonObject.getString("scholar");
                    String url = jsonObject.getString("url");
                    String marks = jsonObject.getString("marks");
                    String department = jsonObject.getString("department");
                    String course = jsonObject.getString("course");
                    String semester = jsonObject.getString("semester");
                    String subject = jsonObject.getString("subject");
                    String assignment = jsonObject.getString("assignment");
                    data data = new data(scholar, url, marks,department,course,semester,subject,assignment);
                   //  Toast.makeText(getApplicationContext(),scholar,Toast.LENGTH_SHORT).show();
                    // t.LENGTH_SHORT).show();
                    dat.add(data);
                }
                adapter adapter = new adapter(getApplicationContext(), dat);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);

        }


    }
}
class data{
    String scholar ,url,marks,department,course,semester,subject,assignment;

    public data(String scholar, String url, String marks, String department, String course, String semester, String subject, String assignment) {
        this.scholar = scholar;
        this.url = url;
        this.marks = marks;
        this.department = department;
        this.course = course;
        this.semester = semester;
        this.subject = subject;
        this.assignment = assignment;
    }

    public String getScholar() {
        return scholar;
    }

    public String getUrl() {
        return url;
    }

    public String getMarks() {
        return marks;
    }

    public String getDepartment() {
        return department;
    }

    public String getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public String getSubject() {
        return subject;
    }

    public String getAssignment() {
        return assignment;
    }
}
class adapter extends ArrayAdapter<data> {

    public adapter(@NonNull Context context, List<data> dat) {
        super(context, 0, dat);
    }
  TextView m;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ssubmissionlayout, parent, false);
        final data d = getItem(position);
        TextView sch = view.findViewById(R.id.scholar);
        Button button = view.findViewById(R.id.download);
        Button give = view.findViewById(R.id.submarks);
         m=view.findViewById(R.id.mar);
         m.setText(d.getMarks());
         TextView id=view.findViewById(R.id.id);
         String jk=String.valueOf(position+1);
         id.setText(jk);
        final EditText editText = view.findViewById(R.id.marks);
        sch.setText(d.getScholar());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    builder.setMessage("want to download ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloading(d.getUrl().toString());
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else{
                    ActivityCompat.requestPermissions((Activity)getContext(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},11);
                }
            }
        });
        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mark = editText.getText().toString();
                SharedPreferences sharedPreferences =getContext().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
               String sh = sharedPreferences.getString("email", "null");
                backgroundtask2 backgroundtask1=new backgroundtask2(parent.getContext());
                backgroundtask1.execute("marks",mark,d.getScholar(),sh,d.getDepartment(),d.getCourse(),d.getSemester(),d.getSubject(),d.getAssignment());
            }
        });
        return view;
    }

    public void downloading(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(url)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "hello" + ".pdf");
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }

    public class backgroundtask2 extends AsyncTask<String, Void, String> {
        Context context;
        String email, password;
        AlertDialog dialog;
        ProgressDialog progressDialog;

        backgroundtask2(Context c) {
            this.context = c;
            progressDialog = new ProgressDialog(context);
        }

        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("marks")) {
                String marks = strings[1];
                String scholar = strings[2];
                String ee = strings[3];
                String department = strings[4];
                String course = strings[5];
                String semester = strings[6];
                String subject = strings[7];
                String assignment = strings[8];

                String link = "http://mybasta.timetablemanit.dx.am/submitmarks.php";
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
                        String inertial = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(ee, "UTF-8") + "&" +
                                URLEncoder.encode("marks", "UTF-8") + "=" + URLEncoder.encode(marks, "UTF-8") + "&" +
                                URLEncoder.encode("scholar", "UTF-8") + "=" + URLEncoder.encode(scholar, "UTF-8") + "&" +
                                URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8") + "&" +
                                URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8") + "&" +
                                URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8") + "&" +
                                URLEncoder.encode("assignment", "UTF-8") + "=" + URLEncoder.encode(assignment, "UTF-8") + "&" +
                                URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
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

            }
            super.onPostExecute(s);
        }


    }
}



