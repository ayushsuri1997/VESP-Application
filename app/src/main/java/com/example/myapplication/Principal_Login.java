package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Principal_Login extends AppCompatActivity {

    Button pLogin;
    TextView forgotpassprin;
    EditText pEmail,pPassword;
    ProgressBar pProgressBar;
    FirebaseAuth pfirebaseAuth;
    DatabaseReference pdref;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal__login);

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        pLogin = findViewById(R.id.plogin);
        forgotpassprin = findViewById(R.id.forgotPassPrin);
        pEmail = findViewById(R.id.mail);
        pPassword = findViewById(R.id.editText2);
        pProgressBar = findViewById(R.id.progressBarPrincipal);
        pfirebaseAuth = FirebaseAuth.getInstance();
        pdref = FirebaseDatabase.getInstance().getReference("Principal");
        final String[] whoisit = new String[1];
        final String[] emailofPrin = new String[1];

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



        pLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailp = pEmail.getText().toString().trim();
                final String passwordp = pPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailp)){
                    pEmail.setError("Email is Required.");
                    return;
                }
//                if(emailp.indexOf('@')!=-1) {
//                    String check[] = emailp.split("@", 2);
//                    if (!check[1].equals("ves.ac.in")) {
//                        pEmail.setError("Sign Up with your VES e-mail");
//                        pEmail.requestFocus();
//                        return;
//                    }
//                }
//                else{
//                    pEmail.setError("In-Correct Email");
//                    pEmail.requestFocus();
//                    return;
//                }

                if(passwordp.length() < 6){
                    pPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                pProgressBar.setVisibility(View.VISIBLE);

                pdref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        whoisit[0] = dataSnapshot.child("who").getValue().toString();
                        emailofPrin[0] = dataSnapshot.child("email").getValue().toString();

                        if (whoisit[0].equals("principal") && emailofPrin[0].equals(emailp)){
                            pfirebaseAuth.signInWithEmailAndPassword(emailp,passwordp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Principal Logged In Successfully ", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(),Principal_page.class));
                                        pProgressBar.setVisibility(View.GONE);
                                        startService(new Intent(getApplicationContext(),NotificationBackgroundService.class));
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Error - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        pProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(),"Error - This is not Principal's Email",Toast.LENGTH_SHORT).show();
                            pProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        pProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });


        forgotpassprin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Correct Mail to send Reset Link ?");

                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String  mail = resetMail.getText().toString().trim();

                        if(TextUtils.isEmpty(mail)) {
                            Toast.makeText(getApplicationContext(),"Mail ID is Empty, Try Again",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        pdref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String isPrincipal = dataSnapshot.child("who").getValue().toString();
                                String whichEmail = dataSnapshot.child("email").getValue().toString();

                                if (isPrincipal.equals("principal") && whichEmail.equals(mail)) {
                                    pfirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Reset Link Sent",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Error "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(),"Not Principal or Enter Correct Principal Email",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }
}
