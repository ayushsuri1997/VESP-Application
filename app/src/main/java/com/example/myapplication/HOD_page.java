package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HOD_page extends AppCompatActivity {

    Button add_teacher,view_teacher,hLOGout,noticeView;
    ImageButton backbutton,homebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_o_d_page);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String userID=""+firebaseUser.getUid();
        Toast.makeText(this, ""+userID, Toast.LENGTH_LONG).show();

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        add_teacher=findViewById(R.id.add_teacherHOD);
        view_teacher=findViewById(R.id.view_techerHOD);
        hLOGout = findViewById(R.id.LogoutHOD);
        noticeView = findViewById(R.id.noticeHOD);


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

        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),View_HOD_Notice.class));
            }
        });

        add_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher_Details.decider = "Teachers";
                startActivity(new Intent(getApplicationContext(),Add_Teacher.class));
            }
        });

        view_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher_Details.decider = "Teachers";
                startActivity(new Intent(getApplicationContext(),View_Teacher.class));
            }
        });
        hLOGout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),HOD_login.class));
                Toast.makeText(getApplicationContext(),"Logged Out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
