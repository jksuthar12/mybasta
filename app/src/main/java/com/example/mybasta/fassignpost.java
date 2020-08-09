package com.example.mybasta;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
public class fassignpost extends Fragment {


    public fassignpost() {
        // Required empty public constructor
    }

    ListView listView;
    ProgressBar progressBar;
    List<data> dat;
    TextView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fassignpost, container, false);
        listView = view.findViewById(R.id.assign);
        nodata=view.findViewById(R.id.nodata);
        progressBar = view.findViewById(R.id.progresss);

        return view;
    }

    @Override
    public void onStart() {
        new background1().execute();
        super.onStart();
    }

    class background1 extends AsyncTask<String, String, String> {


        background1() {
            dat = new ArrayList<>();

        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("myAppPreference", Context.MODE_PRIVATE);
            String emaill = sharedPreferences.getString("email", null);
            String result = "";
            String host = "http://mybasta.timetablemanit.dx.am/retrieveassignment.php";
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
            dat.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            if(s!=null)
            {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("data");


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String department = jsonObject.getString("department");
                        String subject = jsonObject.getString("subject");
                        String semester = jsonObject.getString("semester");
                        String course = jsonObject.getString("course");
                        String date = jsonObject.getString("date");
                        String marks = jsonObject.getString("totalmarks");
                        String assignment = jsonObject.getString("assignment");
                        String url = jsonObject.getString("url");
                        data data = new data(url, department, subject, semester, marks, date, assignment, course);
                        // Toast.makeText(getContext(),name,Toas
                        // t.LENGTH_SHORT).show();
                        dat.add(data);
                    }


                    Log.i("daaaaaata", "jk");
                    if (getActivity() != null) {
                        adapter adapter = new adapter(getActivity(), dat);
                        listView.setAdapter(adapter);
                    }

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
        String url, department, subject, semester, marks, date, assignment, course;

        public data(String url, String department, String subject, String semester, String marks, String date, String assignment, String course) {
            this.url = url;
            this.department = department;
            this.subject = subject;
            this.semester = semester;
            this.marks = marks;
            this.date = date;
            this.assignment = assignment;
            this.course = course;
        }

        public String getUrl() {
            return url;
        }

        public String getDepartment() {
            return department;
        }

        public String getSubject() {
            return subject;
        }

        public String getSemester() {
            return semester;
        }

        public String getMarks() {
            return marks;
        }

        public String getDate() {
            return date;
        }

        public String getAssignment() {
            return assignment;
        }

        public String getCourse() {
            return course;
        }
    }

    class adapter extends ArrayAdapter<data> {

        public adapter(@NonNull Context context, List<data> jk) {
            super(context, 0, jk);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.uploadassignmentlayout, parent, false);
            final data d = getItem(position);
            TextView department = view.findViewById(R.id.dep);
            TextView subject = view.findViewById(R.id.sub);
            TextView semester = view.findViewById(R.id.sem);
            TextView course = view.findViewById(R.id.cou);
            TextView assignment = view.findViewById(R.id.assn);
            Button dd = view.findViewById(R.id.d);
            Button delete=view.findViewById(R.id.delete);
            TextView marks = view.findViewById(R.id.mark);
            Button button = view.findViewById(R.id.upload);
            TextView dead = view.findViewById(R.id.dead);
            LinearLayout linearLayout = view.findViewById(R.id.main);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), submissions.class);
                    intent.putExtra("department", d.getDepartment());
                    intent.putExtra("course", d.getCourse());
                    intent.putExtra("semester", d.getSemester());
                    intent.putExtra("subject", d.getSubject());
                    intent.putExtra("assignment", d.getAssignment());
                    startActivity(intent);
                }
            });
            department.setText(d.getDepartment());
            subject.setText(d.getSubject());
            semester.setText(d.getSemester());
            course.setText(d.getCourse());
            assignment.setText(d.getAssignment());
            marks.setText(d.getMarks());
            dead.setText(d.getDate());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                }
            });
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.changedate);
            dd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
            Button change = dialog.findViewById(R.id.ok);
            final EditText editText = dialog.findViewById(R.id.date);
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ddd = editText.getText().toString();
                    Log.i("date",ddd);
                    backgroundtask2 backgroundtask2=new backgroundtask2(getActivity());
                    backgroundtask2.execute("changedata",ddd,d.getDepartment(),d.getCourse(),d.getSubject(),d.getSemester(),
                            d.getAssignment());
                    dialog.dismiss();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("want to delete ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new delassign(getActivity()).execute("delete",d.getDepartment(),d.getCourse(),d.getSubject(),d.getSemester(),d.getAssignment());
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
            });
            return view;
        }
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
        AlertDialog dialog;
        ProgressDialog progressDialog;

        backgroundtask2(Context c) {
            this.context = c;
            progressDialog=new ProgressDialog(c);
        }

        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("changedata")) {
                String ddd = strings[1];
                String department = strings[2];
                String course = strings[3];
                String subject = strings[4];
                String semester = strings[5];
                String assignment = strings[6];
                String link = "http://mybasta.timetablemanit.dx.am/changedate.php";
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
                        String inertial = URLEncoder.encode("ddd", "UTF-8") + "=" + URLEncoder.encode(ddd, "UTF-8") + "&" +
                                URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8") + "&" +
                                URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8") + "&" +
                                URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8") + "&" +
                                URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" +
                                URLEncoder.encode("assignment", "UTF-8") + "=" + URLEncoder.encode(assignment, "UTF-8");
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
            progressDialog.dismiss();
            if (s != null) {
                new background1().execute();
            }
            super.onPostExecute(s);
        }


    }
    public class delassign extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog dialog;
        ProgressDialog progressDialog;

        delassign(Context c) {
            this.context = c;
            progressDialog=new ProgressDialog(c);
        }

        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("delete")) {
                String department = strings[1];
                String course = strings[2];
                String subject = strings[3];
                String semester = strings[4];
                String assignment = strings[5];
                String link = "http://mybasta.timetablemanit.dx.am/deleteassignment.php";
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
                        String inertial = URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8") + "&" +
                                URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8") + "&" +
                                URLEncoder.encode("semester", "UTF-8") + "=" + URLEncoder.encode(semester, "UTF-8") + "&" +
                                URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" +
                                URLEncoder.encode("assignment", "UTF-8") + "=" + URLEncoder.encode(assignment, "UTF-8");
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
              progressDialog.setMessage("Deleting...");
              progressDialog.setCancelable(false);
              progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            dat.clear();
            new background1().execute();
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            if (s != null) {
            }
            super.onPostExecute(s);
        }


    }
}
