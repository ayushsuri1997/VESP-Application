package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class StaffLogin extends AppCompatActivity {

    EditText mEmailStaff,mPasswordStaff,sTID;
    Button mLoginBtnStaff;
    TextView forgotTextLinkStaff;
    FirebaseAuth fAuthStaff;
    FirebaseUser fUserStaff;
    DatabaseReference databaseProfileDataTeacher, sref;
    ProgressBar progressBarStaff;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        sTID = findViewById(R.id.TIDStaff);
        mEmailStaff = findViewById(R.id.MailStaff);
        mPasswordStaff = findViewById(R.id.PasswordStaff);
        progressBarStaff = findViewById(R.id.progressBarStaff);
        fAuthStaff = FirebaseAuth.getInstance();
        mLoginBtnStaff = findViewById(R.id.tlogin);
        forgotTextLinkStaff = findViewById(R.id.forgotPasswordStaff);
        databaseProfileDataTeacher = FirebaseDatabase.getInstance().getReference("Teachers");


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





        fUserStaff = fAuthStaff.getCurrentUser();

        if(fUserStaff!=null){
            startActivity(new Intent(getApplicationContext(),TeacherPage.class));
            finish();
        }

        mLoginBtnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStaff = mEmailStaff.getText().toString().trim();
                final String passwordStaff = mPasswordStaff.getText().toString().trim();
                final String tid = sTID.getText().toString().trim();

                if (TextUtils.isEmpty(tid)){
                    sTID.setError("Enter TID");
                    return;
                }

                if(TextUtils.isEmpty(emailStaff)){
                    mEmailStaff.setError("Email is Required.");
                    return;
                }

                if(emailStaff.indexOf('@')!=-1) {
                    String check[] = emailStaff.split("@", 2);
                    if (!check[1].equals("ves.ac.in")) {
                        mEmailStaff.setError("Sign Up with your VES e-mail");
                        mEmailStaff.requestFocus();
                        return;
                    }
                }
                else{
                    mEmailStaff.setError("In-Correct Email");
                    mEmailStaff.requestFocus();
                    return;
                }

                if(passwordStaff.length() < 6){
                    mPasswordStaff.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBarStaff.setVisibility(View.VISIBLE);

                // authenticate the staff

                databaseProfileDataTeacher.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(tid).child("who").getValue() !=null && dataSnapshot.child(tid).child("email").getValue() !=null) {
                            String whoisit = dataSnapshot.child(tid).child("who").getValue().toString();
                            String emailcheck = dataSnapshot.child(tid).child("email").getValue().toString();

                            if (whoisit.equals("Teachers") && emailcheck.equals(emailStaff)) {
                                fAuthStaff.signInWithEmailAndPassword(emailStaff,passwordStaff).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Staff Logged In Successfully ", Toast.LENGTH_SHORT).show();
                                            databaseProfileDataTeacher.child(tid).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String IDauth = dataSnapshot.child("authID").getValue().toString();
                                                    sref = FirebaseDatabase.getInstance().getReference("Teachers/" + tid);
                                                    getpathDBStaff pathDB = new getpathDBStaff(sref.getPath().toString());

                                                    if (IDauth.equals("1")) {
                                                        startActivity(new Intent(getApplicationContext(), ResetPasswordStaff.class));
                                                        //databaseProfileData.child(id).child("password").setValue(password);
                                                        //finish();
                                                    } else if (IDauth.equals("2")) {
                                                        startActivity(new Intent(getApplicationContext(), TeacherPage.class));
                                                        //databaseProfileData.child(id).child("password").setValue(password);
                                                        //finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Error - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBarStaff.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Not a Staff, Please Enter Valid Staff Email", Toast.LENGTH_SHORT).show();
                                progressBarStaff.setVisibility(View.GONE);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Not a Staff, Please Enter valid TID", Toast.LENGTH_SHORT).show();
                            progressBarStaff.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarStaff.setVisibility(View.GONE);
                    }
                });
            }
        });

        forgotTextLinkStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Forgot Password ? or TID?");
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
