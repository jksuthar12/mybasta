package com.example.mybasta;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class studentass extends Fragment {


    public studentass() {
        // Required empty public constructor
    }
    String ema;
    Uri path;
    String urll = "http://mybasta.timetablemanit.dx.am/submitfile.php";
    ListView listView;
    ProgressBar progressBar;
    List<data> dat;
    String linkk;
    String ass,sub;
    TextView nodata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_studentass, container, false);
        listView=view.findViewById(R.id.list);
        nodata=view.findViewById(R.id.nodata);
        progressBar=view.findViewById(R.id.pr);
        new background1().execute();
        return view;
    }
    class background1 extends AsyncTask<String, String, String> {


        background1() {
            dat=new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
            String semester = sharedPreferences.getString("semester", null);
            String department = sharedPreferences.getString("department", null);
            String course = sharedPreferences.getString("course", null);
            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/studentassignment.php";
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
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String subject = jsonObject.getString("subject");
                        String faculty = jsonObject.getString("email");
                        String url = jsonObject.getString("url");
                        String da = jsonObject.getString("date");
                        String marks = jsonObject.getString("totalmarks");
                        String assignment = jsonObject.getString("assignment");
                        data data = new data(subject, url, faculty, assignment, da, marks);
                        // Toast.makeText(getContext(),name,Toas
                        // t.LENGTH_SHORT).show();
                        dat.add(data);
                    }
                    adapter adapter = new adapter(getContext(), dat);
                    listView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(dat.size()==0 || dat.isEmpty())
                nodata.setVisibility(View.VISIBLE);
            super.onPostExecute(s);

        }

    }

    class data {
        String  subject, url, faculty,assignment,data,marks;

        public data(String subject, String url, String faculty, String assignment, String data, String marks) {
            this.subject = subject;
            this.url = url;
            this.faculty = faculty;
            this.assignment = assignment;
            this.data = data;
            this.marks = marks;
        }

        public String getSubject() {
            return subject;
        }

        public String getUrl() {
            return url;
        }

        public String getFaculty() {
            return faculty;
        }

        public String getAssignment() {
            return assignment;
        }

        public String getData() {
            return data;
        }

        public String getMarks() {
            return marks;
        }
    }

    class adapter extends ArrayAdapter<data> {
        Context c;
        List<data> jk;
        TextView faculty, subject, assignment,url, data,marks;
        Button button,submit;
        public adapter(@NonNull Context context, List<data> dataa) {
            // this.c=context;
            super(context, 0, dataa);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.studentassignment, parent, false);
            final data d = getItem(position);
            faculty = view.findViewById(R.id.fac);
            subject = view.findViewById(R.id.sub);
            assignment = view.findViewById(R.id.assn);
            data = view.findViewById(R.id.dead);
            marks = view.findViewById(R.id.mark);
            submit=view.findViewById(R.id.submission);
            subject.setText(d.getSubject());
            faculty.setText(d.getFaculty());
            assignment.setText(d.getAssignment());
            data.setText(d.getData());
            marks.setText(d.getMarks());
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
                                downloading(d.getUrl(), d.getSubject(), d.getAssignment());
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
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startDate = d.getData();
                    String endDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                    try {
                        Date start = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                                .parse(startDate);
                        Date end = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                                .parse(endDate);

                        if (start.compareTo(end) < 0) {
                            Toast.makeText(getContext(),"u missed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ass=d.getAssignment().toString();
                            sub=d.getSubject().toString();
                            ema=d.getFaculty().toString();
                            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                            i.setType("application/pdf");
                            startActivityForResult(i,2);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            });

            return view;
        }

    }
    public void downloading(String url,String su,String assi){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(String.valueOf(url)));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,su+"_"+assi+".pdf");
        DownloadManager downloadManager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
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
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Uploading....");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            // Log.i("sun",s);
            dialog.cancel();
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("myAppPreference",Context.MODE_PRIVATE);
            String emaill=sharedPreferences.getString("email",null);
            String department=sharedPreferences.getString("department",null);
            String course=sharedPreferences.getString("course",null);
            String semester=sharedPreferences.getString("semester",null);
            String scholar=sharedPreferences.getString("scholar",null);
           backgroundtask backgroundtask=new backgroundtask(getContext());
          backgroundtask.execute("submission",linkk,ass,sub,ema,department,course,semester,scholar);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && data!=null){
            path=data.getData();
            new Upload(getContext(),path).execute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
