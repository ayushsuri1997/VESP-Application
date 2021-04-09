package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Vector;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword,mRoll;
    Button mLoginBtn;
    TextView forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    DatabaseReference databaseProfileData,mref,mmref;
    Spinner batchSpinner1, courseSpinner1;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        mRoll = findViewById(R.id.roll_no_stud);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        //mCreateBtn = findViewById(R.id.createText);
        batchSpinner1 = findViewById(R.id.batchSpinner);
        courseSpinner1 = findViewById(R.id.courseSpinner);
        forgotTextLink = findViewById(R.id.forgotPassword);
        databaseProfileData = FirebaseDatabase.getInstance().getReference("All_Students");

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

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rollno = mRoll.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String course = courseSpinner1.getSelectedItem().toString().trim();
                final String batch = batchSpinner1.getSelectedItem().toString().trim();
                if(TextUtils.isEmpty(rollno)){
                    mRoll.setError("Enter Roll No");
                    return;
                }
                if(email.indexOf('@')!=-1) {
                    String check[] = email.split("@", 2);
                    if (!check[1].equals("ves.ac.in")) {
                        mEmail.setError("Sign Up with your VES e-mail");
                        mEmail.requestFocus();
                        return;
                    }
                }
                else{
                    mEmail.setError("In-Correct Email");
                    mEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                databaseProfileData.child(batch).child(course).child(rollno).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String whoisit = dataSnapshot.child("who").getValue().toString();
                        String emailrollcheck = dataSnapshot.child("email").getValue().toString();
                        if (whoisit.equals("stud")) {
                            if (emailrollcheck.equals(email)) {


                                // authenticate the user

                                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(Login.this, "Logged In Successfully ", Toast.LENGTH_SHORT).show();
                                            //final String id = fAuth.getCurrentUser().getUid();
                                            databaseProfileData.child(batch).child(course).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String IDauth = dataSnapshot.child(rollno).child("authID").getValue().toString();
                                                    mref = FirebaseDatabase.getInstance().getReference("All_Students/" + batch + "/" + course + "/" + rollno);
                                                    @SuppressLint("RestrictedApi") getpathDB pathDB = new getpathDB(mref.getPath().toString());
//                                    pathDB.setPath(mref.getPath().toString());
//                                    Toast.makeText(getApplicationContext(),pathDB.getPath(),Toast.LENGTH_SHORT).show();
                                                    //ResetPassword
                                                    if (IDauth.equals("1")) {
                                                        startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                                                        //databaseProfileData.child(id).child("password").setValue(password);
                                                        //finish();
                                                    } else if (IDauth.equals("2")) {
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        //databaseProfileData.child(id).child("password").setValue(password);
                                                        //finish();
                                                    }
                                                    // Toast.makeText(Login.this,IDauth, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "Roll No corresponding ID doesn't Exist", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Invalid ID, Please Enter Student ID",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });


        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Forgot Password ?");
                passResetDialog.setMessage("Send A Mail to the Administrator");

                passResetDialog.setPositiveButton("Send Mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gmail = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/intl/en_in/gmail/about/"));
                        startActivity(gmail);
                    }
                });


                passResetDialog.create().show();
            }
        });
    }
}
