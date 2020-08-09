package com.example.mybasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class facultyzone extends AppCompatActivity {
      BottomNavigationView bottomNavigationView;
      FloatingActionButton fab;
      Dialog dialog;
      ImageButton button;
      Button assignment;
      androidx.appcompat.widget.Toolbar t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyzone);
        t=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigationvieew);
        fab=(FloatingActionButton)findViewById(R.id.add);
        getSupportFragmentManager().beginTransaction().replace(R.id.fram,new facultyassignment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.assignment)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram,new facultyassignment()).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram,new facultynotes()).commit();
                   return true;
            }
        });
        dialog.setContentView(R.layout.dialoglayout);
        assignment=dialog.findViewById(R.id.uploadass);
        Button button1=dialog.findViewById(R.id.not);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(facultyzone.this,uploadassignment.class));
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(facultyzone.this,uploadnotes.class));
                dialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.out){
            SharedPreferences myPrefs = getSharedPreferences("myAppPreference",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.clear();
            editor.commit();
            Intent i=new Intent(facultyzone.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
         finish();
        super.onBackPressed();
    }
}
