package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class  MainActivity extends AppCompatActivity {

    Button NoticeStud;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        NoticeStud = findViewById(R.id.StudentNotice);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EntryApp.class));
            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EntryApp.class));

            }
        });

        NoticeStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),View_Stud_Notice.class));
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        Toast.makeText(getApplicationContext(),"Logged Out Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
