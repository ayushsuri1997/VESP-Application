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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.BindException;
import java.util.ArrayList;
import java.util.Objects;

public class HOD_login extends AppCompatActivity {

    EditText hmaill,hpassword,hTID;
    TextView forgotpasshod;
    Button hLogin;
    ImageButton backbutton,homebutton;
    FirebaseAuth hfirebaseAuth;
    DatabaseReference hDref,hhref;
    ProgressBar hprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_o_d_login);
        homebutton =findViewById(R.id.home);
        backbutton =findViewById(R.id.back);
        hTID = findViewById(R.id.TIDh);
        forgotpasshod = findViewById(R.id.forgotPassHOD);
        hmaill = findViewById(R.id.hmail);
        hpassword = findViewById(R.id.hpass);
        hLogin = findViewById(R.id.hlogin);
        hfirebaseAuth = FirebaseAuth.getInstance();
        hprogressBar = findViewById(R.id.progressBarHOD);
        hDref = FirebaseDatabase.getInstance().getReference("HOD");


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






        hLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailh = hmaill.getText().toString().trim();
                final String passh = hpassword.getText().toString().trim();
                final String tid = hTID.getText().toString().trim();

                if (TextUtils.isEmpty(tid)){
                    hTID.setError("Enter TID");
                    return;
                }

                if(TextUtils.isEmpty(emailh)){
                    hmaill.setError("Enter Mail");
                    return;
                }
                if(emailh.indexOf('@')!=-1) {
                    String check[] = emailh.split("@", 2);
                    if (!check[1].equals("ves.ac.in")) {
                        hmaill.setError("Sign Up with your VES e-mail");
                        hmaill.requestFocus();
                        return;
                    }
                }
                else{
                    hmaill.setError("In-Correct Email");
                    hmaill.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(passh)){
                    hpassword.setError("Password is Required.");
                    return;
                }

                if(passh.length() < 6){
                    hpassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                hprogressBar.setVisibility(View.VISIBLE);


                    hDref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(tid).child("who").getValue() !=null && dataSnapshot.child(tid).child("email").getValue() !=null) {

                                String whoisit = dataSnapshot.child(tid).child("who").getValue().toString();
                                String emailcheck = dataSnapshot.child(tid).child("email").getValue().toString();

                                if (whoisit.equals("HOD") && emailcheck.equals(emailh)) {
                                    hfirebaseAuth.signInWithEmailAndPassword(emailh, passh).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Logged In Successfully ", Toast.LENGTH_SHORT).show();
//                                                startService(new Intent(getApplicationContext(),NotificationBackgroundService.class));
                                                hDref.child(tid).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String IDauth = dataSnapshot.child("authID").getValue().toString();
                                                        hhref = FirebaseDatabase.getInstance().getReference("HOD/" + tid);
                                                        @SuppressLint("RestrictedApi") getPathDBHOD pathDB = new getPathDBHOD(hhref.getPath().toString());

                                                        if (IDauth.equals("1")) {
                                                            startActivity(new Intent(getApplicationContext(), ResetPasswordHOD.class));
                                                            //databaseProfileData.child(id).child("password").setValue(password);
                                                            //finish();
                                                        } else if (IDauth.equals("2")) {
                                                            startActivity(new Intent(getApplicationContext(), HOD_page.class));
                                                            //databaseProfileData.child(id).child("password").setValue(password);
                                                            //finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                hprogressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(getApplicationContext(), "Not a HOD, Please Enter Valid HOD Email", Toast.LENGTH_SHORT).show();
                                    hprogressBar.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Not a HOD, Please Enter Valid TID", Toast.LENGTH_SHORT).show();
                                hprogressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            hprogressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });

        forgotpasshod.setOnClickListener(new View.OnClickListener() {
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
