package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Principal_page extends AppCompatActivity {

    Button addHOD,viewHOD,viewStudent,viewTeacher,logout,postPrin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String userID=""+firebaseUser.getUid();
        Toast.makeText(this, ""+userID, Toast.LENGTH_LONG).show();

        addHOD=findViewById(R.id.create_hod);
        viewHOD=findViewById(R.id.view_hod);
        viewStudent=findViewById(R.id.view_student);
        viewTeacher=findViewById(R.id.view_teacher);
        logout = findViewById(R.id.LogoutPrin);
        postPrin = findViewById(R.id.postSomething);

        postPrin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Post_prin_page.class));
            }
        });

        addHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher_Details.decider = "HOD";
                startActivity(new Intent(getApplicationContext(),Add_Teacher.class));
            }
        });
        viewHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher_Details.decider = "HOD";
                startActivity(new Intent(Principal_page.this,View_Teacher.class));
            }
        });

        viewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher_Details.decider = "Teachers";
                startActivity(new Intent(getApplicationContext(),View_Teacher.class));
            }
        });

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewStudentDetails.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Principal_Login.class));
                Toast.makeText(getApplicationContext(),"Logged Out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
