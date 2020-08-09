package com.example.mybasta;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class facultynotes extends Fragment {

    ListView listnotes;
       ProgressBar progressBar;
       TextView nodata;
    public facultynotes() {
        // Required empty public constructor
    }

    List<data> dat ;
String emaill;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facultynotes, container, false);
        listnotes = view.findViewById(R.id.listnotes);
        progressBar=view.findViewById(R.id.progress);
        nodata=view.findViewById(R.id.nodata);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
        emaill = sharedPreferences.getString("email", null);
        new background1().execute();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    class background1 extends AsyncTask<String, String, String> {


        background1() {
           dat=new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/retrievenotes.php";
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
               progressBar.setVisibility(View.GONE);
               if(s!=null) {
                   if (s.trim().equals("not"))
                       Toast.makeText(getContext(), "data not available", Toast.LENGTH_SHORT).show();
                   try {
                       JSONObject object = new JSONObject(s);
                       JSONArray array = object.getJSONArray("data");
                       for (int i = 0; i < array.length(); i++) {
                           JSONObject jsonObject = array.getJSONObject(i);
                           String name = jsonObject.getString("department");
                           String dp = jsonObject.getString("subject");
                           String r = jsonObject.getString("semester");
                           String d = jsonObject.getString("course");
                           String url = jsonObject.getString("url");
                           data data = new data(name, r, dp, url, d);
                           // Toast.makeText(getContext(),name,Toas
                           // t.LENGTH_SHORT).show();
                           dat.add(data);
                       }
                       Log.i("daaaaaata", "jk");
                       if(getActivity()!=null) {
                           adapter adapter = new adapter(getActivity(), dat);
                           listnotes.setAdapter(adapter);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               if(dat.isEmpty() || dat.size()==0)
                 nodata.setVisibility(View.VISIBLE);
                super.onPostExecute(s);

        }

    }

    @Override
    public void onPause() {
        new background1().cancel(true);
        super.onPause();
    }

    class data {
        String department, semester, subject, url, course;

        public data(String department, String semester, String subject, String url, String course) {
            this.department = department;
            this.semester = semester;
            this.subject = subject;
            this.url = url;
            this.course = course;
        }

        public String getDepartment() {
            return department;
        }

        public String getSemester() {
            return semester;
        }

        public String getSubject() {
            return subject;
        }

        public String getUrl() {
            return url;
        }

        public String getCourse() {
            return course;
        }
    }

    class adapter extends ArrayAdapter<data> {
        Context c;
        List<data> jk;
        TextView department, semester, subject, url, course;
          Button button;
        public adapter(@NonNull Context context, List<data> dataa) {
            super(context, 0, dataa);

        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.facultynoteslayout, parent, false);
            final data d = getItem(position);
            department = view.findViewById(R.id.dep);
            subject = view.findViewById(R.id.sub);
            semester = view.findViewById(R.id.sem);
            course = view.findViewById(R.id.cou);
            department.setText(d.getDepartment());
            subject.setText(d.getSubject());
            course.setText(d.getCourse());
            semester.setText(d.getSemester());
            button=view.findViewById(R.id.upload);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("want to download this file");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloading(d.getUrl(), "Notes", d.getDepartment(), d.course, d.getSubject());
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
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},11);
                    }

                }
            });
            return view;
        }
    }
    public void downloading(String url,String t,String depart,String cour,String sub){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(String.valueOf(url)));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,cour +"_"+depart+"_"+sub+"_"+t+".pdf");
        DownloadManager downloadManager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }

}



