package com.example.mybasta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class uploadnotes extends AppCompatActivity {
    Spinner department,semester,assignment,course;
    ImageButton back;
    TextView post;
    Button selectfile;
    ProgressDialog dialog;
    String linkk;

    Uri selectefile;
    String urll="http://mybasta.timetablemanit.dx.am/index.php";
    Bitmap bitmap;
    Button imageView;
    Uri path;

    EditText subject,date,marks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadnotes);
        department=(Spinner)findViewById(R.id.department);
        semester=(Spinner)findViewById(R.id.semester);
        back=(ImageButton)findViewById(R.id.back);
        subject=(EditText)findViewById(R.id.subject);
        date=(EditText)findViewById(R.id.date);
        marks=(EditText)findViewById(R.id.totalmarks);
        post=(TextView)findViewById(R.id.post);
        course=(Spinner)findViewById(R.id.course);
        selectfile=(Button)findViewById(R.id.choosefile);
        String[] cours={"Course","B.Tech","M.Tech"};
        String[] sem={"semester","1","2","3","4","5","6","7","8"};
        String[] no={"assignment no.","1","2","3","4","5","6","7","8","9","10"};
        final String[] depart={"Department","Computer Science","Mechanical","Electrical","Electronics and Communication","Civil","Chemical","MSME"};
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,depart);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        department.setAdapter(adapter);
        ArrayAdapter<CharSequence>adapter1=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,cours);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        course.setAdapter(adapter1);
        ArrayAdapter<CharSequence>adapter2=new ArrayAdapter<CharSequence>(this,R.layout.support_simple_spinner_dropdown_item,sem);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semester.setAdapter(adapter2);
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
                else if(TextUtils.isEmpty(subject.toString()))
                    subject.setError("enter subject");
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
            dialog=new ProgressDialog(uploadnotes.this);
            dialog.setMessage("Uploading....");
            dialog.setCancelable(false);
           dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            // Log.i("sun",s);
            dialog.cancel();
            backgroundtask backgroundtask=new backgroundtask(uploadnotes.this);
            backgroundtask.execute("uploadnotes",linkk,department.getSelectedItem().toString(),semester.getSelectedItem().toString()
            ,subject.getText().toString(),course.getSelectedItem().toString());
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
            selectefile =data.getData();
            path=data.getData();
        }
    }

}

