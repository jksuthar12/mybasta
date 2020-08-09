package com.example.mybasta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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

public class studentregistraion extends AppCompatActivity {
       Spinner depart,course;
       TextInputEditText firstname,lastname,middlename,dateofbirth,email,password,address,mobileno,semester,scholarno;
       Button signup;
       ImageButton back;
       String gender;
       RadioGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentregistraion);
        depart=(Spinner)findViewById(R.id.department);
        course=(Spinner)findViewById(R.id.course);
        back=(ImageButton)findViewById(R.id.back);
        group=(RadioGroup)findViewById(R.id.radio);
        firstname=(TextInputEditText)findViewById(R.id.firstname);
        middlename=(TextInputEditText)findViewById(R.id.middlename);
        lastname=(TextInputEditText)findViewById(R.id.lastname);
        dateofbirth=(TextInputEditText)findViewById(R.id.dateofbirth);
        email=(TextInputEditText)findViewById(R.id.email);
        mobileno=(TextInputEditText)findViewById(R.id.mobileno);
        address=(TextInputEditText)findViewById(R.id.address);
        password=(TextInputEditText)findViewById(R.id.password);
        semester=(TextInputEditText)findViewById(R.id.semester);
        scholarno=(TextInputEditText)findViewById(R.id.scholarno);
        signup=(Button)findViewById(R.id.signup);
        final String[] departmentt = {null};
        final String[] department={"Department","Computer Science","Mechanical","Electrical","Electronics and Communication","Civil","Chemical","MSME"};
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item, department );
        langAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        depart.setAdapter(langAdapter);
        String[] cours={"Course","B.Tech","M.Tech"};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item, cours );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        course.setAdapter(adapter);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                if(cm.getActiveNetworkInfo()==null || !cm.getActiveNetworkInfo().isConnected())
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                else {
                    if (TextUtils.isEmpty(firstname.getText()))
                        firstname.setError("enter firstname");
                    else if (TextUtils.isEmpty(middlename.getText()))
                        middlename.setError("enter middlename");
                    else if (TextUtils.isEmpty(lastname.getText()))
                        lastname.setError("enter lastname");
                    else if (TextUtils.isEmpty(scholarno.getText()))
                        scholarno.setError("enter scholar");
                    else if (TextUtils.isEmpty(dateofbirth.getText()))
                        dateofbirth.setError("enter DOB");
                    else if (TextUtils.isEmpty(email.getText()))
                        email.setError("enter email");
                    else if (TextUtils.isEmpty(mobileno.getText()))
                        mobileno.setError("enter phone no.");
                    else if (TextUtils.isEmpty(password.getText()))
                        password.setError("enter Password");
                    else if (TextUtils.isEmpty(address.getText()))
                        address.setError("enter address");
                    else if (TextUtils.isEmpty(semester.getText()))
                        semester.setError("enter semester");
                    else if(depart.getSelectedItem()=="Department")
                        Toast.makeText(getApplicationContext(),"select department",Toast.LENGTH_SHORT).show();
                    else if(course.getSelectedItem()=="Course")
                        Toast.makeText(getApplicationContext(),"select course",Toast.LENGTH_SHORT).show();
                    else{

                        if(group.getCheckedRadioButtonId()==R.id.male){
                            Log.w("check","male");
                            gender="Male";
                        }
                        else{
                            Log.w("check","fmale");
                            gender="Female";
                        }
                        Random generator = new Random();
                        int num = generator.nextInt(899999) + 100000;
                        SharedPreferences sharedPreferences = getSharedPreferences("myAppPreference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("otp", String.valueOf(num));
                        editor.commit();

                        backgroundtask1 backgroundtask = new backgroundtask1(studentregistraion.this);
                        backgroundtask.execute("otp", String.valueOf(num), email.getText().toString().trim());
                    }


                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }
    public class backgroundtask1 extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog dialog;
        ProgressDialog progressDialog;
        backgroundtask1(Context c){
            this.context=c;
            progressDialog=new ProgressDialog(studentregistraion.this);
        }
        protected String doInBackground(String... strings) {
            String type=strings[0];
            if(type.equals("otp")){
                String otp=strings[1];
                String email=strings[2];
                String link="http://mybasta.timetablemanit.dx.am/email.php";
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
                                URLEncoder.encode("otp","UTF-8")+"="+URLEncoder.encode(otp,"UTF-8");
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
                if(s.trim().equals("otp")){
                    Intent intent=new Intent(studentregistraion.this,otp.class);
                    intent.putExtra("firstname",firstname.getText().toString());
                    intent.putExtra("type","student");
                    intent.putExtra("middlename",middlename.getText().toString());
                    intent.putExtra("lastname",lastname.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("dateofbirth",dateofbirth.getText().toString());
                    intent.putExtra("password",password.getText().toString());
                    intent.putExtra("mobileno",mobileno.getText().toString());
                    intent.putExtra("depart",depart.getSelectedItem().toString());
                    intent.putExtra("address",address.getText().toString());
                    intent.putExtra("scholar",scholarno.getText().toString());
                    intent.putExtra("course",course.getSelectedItem().toString());
                    intent.putExtra("semester",semester.getText().toString());
                    intent.putExtra("gender",gender);
                    startActivity(intent);
                    finish();
                }

            }
            super.onPostExecute(s);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
