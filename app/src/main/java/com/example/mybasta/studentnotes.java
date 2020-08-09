package com.example.mybasta;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
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

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class studentnotes extends Fragment {


    public studentnotes() {
        // Required empty public constructor
    }

   ListView listView;
    ProgressBar progressBar;
    List<data>dat;
    TextView nodata;
    String semester,department,course;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_studentnotes, container, false);
        listView=view.findViewById(R.id.list);
        progressBar=view.findViewById(R.id.pr);
        nodata=view.findViewById(R.id.nodata);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
         semester = sharedPreferences.getString("semester", null);
        department = sharedPreferences.getString("department", null);
        course = sharedPreferences.getString("course", null);
        new background1().execute();
        return view;
    }
    class background1 extends AsyncTask<String, String, String> {


        background1() {
            dat=new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/studentnotes.php";
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
                    String inertial = URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8")
                            +"&"+URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8")
                            +"&"+URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8");
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
                        String subject = jsonObject.getString("subject");
                        String faculty = jsonObject.getString("email");
                        String url = jsonObject.getString("url");
                        data data = new data(subject, url, faculty);
                        // Toast.makeText(getContext(),name,Toas
                        // t.LENGTH_SHORT).show();
                        dat.add(data);
                    }
                    if (getActivity() != null) {
                        adapter adapter = new adapter(getActivity(), dat);
                        listView.setAdapter(adapter);
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

    class data {
        String  subject, url, faculty;

        public data(String subject, String url, String faculty) {
            this.subject = subject;
            this.url = url;
            this.faculty = faculty;

        }

        public String getSubject() {
            return subject;
        }

        public String getUrl() {
            return url;
        }

        public String getCourse() {
            return faculty;
        }


    }

    class adapter extends ArrayAdapter<data> {
        Context c;
        List<data> jk;
        TextView faculty, subject, assignment,url, data,marks;
        Button button;
        public adapter(@NonNull Context context, List<data> dataa) {
            // this.c=context;
            super(context, 0, dataa);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.studnetnoteslayout, parent, false);
            final data d = getItem(position);
            faculty = view.findViewById(R.id.fac);
            subject = view.findViewById(R.id.sub);
            subject.setText(d.getSubject());
            faculty.setText(d.getCourse());
            button=view.findViewById(R.id.upload);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("want to download ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloading(d.getUrl());
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
    public void downloading(String url){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(String.valueOf(url)));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"heelo"+".pdf");
        DownloadManager downloadManager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }

}
