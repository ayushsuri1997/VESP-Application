package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPasswordHOD extends AppCompatActivity {

    EditText oldpassHOD,newpassHOD,renewpassHOD;
    Button reset_meHOD;
    ProgressBar progressBarHOD;
    FirebaseAuth fAuthHOD;
    DatabaseReference databaseProfileDataHOD;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_h_o_d);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        oldpassHOD = findViewById(R.id.resetpasswordHOD);
        newpassHOD = findViewById(R.id.newpasswordHOD);
        renewpassHOD = findViewById(R.id.reenternewpasswordHOD);
        reset_meHOD = findViewById(R.id.resetmeHOD);
        progressBarHOD = findViewById(R.id.reset_progressHOD);
        String pathHOD = getPathDBHOD.getPath();
        databaseProfileDataHOD = FirebaseDatabase.getInstance().getReference(pathHOD);
        fAuthHOD = FirebaseAuth.getInstance();

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

        reset_meHOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old_pass = oldpassHOD.getText().toString().trim();
                final String new_pass = newpassHOD.getText().toString().trim();
                String re_new = renewpassHOD.getText().toString().trim();

                if(TextUtils.isEmpty(old_pass)) {
                    oldpassHOD.setError("Old Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(new_pass)) {
                    newpassHOD.setError("New Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(re_new)) {
                    renewpassHOD.setError("Re-enter New Password.");
                    return;
                }

                if(!(new_pass.equals(re_new))) {
                    Toast.makeText(getApplicationContext(),"Password Don't Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(old_pass.length() < 6){
                    oldpassHOD.setError("Password Must be >= 6 Characters");
                    return;
                }

                if(new_pass.length() < 6){
                    newpassHOD.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBarHOD.setVisibility(View.VISIBLE);

                final FirebaseUser user = fAuthHOD.getCurrentUser();
                final int[] flag = {0};

                databaseProfileDataHOD.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldpasss = dataSnapshot.child("password").getValue().toString();
                        if (old_pass.equals(oldpasss)) {
                            user.updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseProfileDataHOD.child("authID").setValue("2");
                                        databaseProfileDataHOD.child("password").setValue(new_pass);
                                        //fAuth.signOut();
                                        //startActivity(new Intent(getApplicationContext(),Login.class));
                                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                        flag[0] = 1;
                                        progressBarHOD.setVisibility(View.GONE);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password Didn't Change ", Toast.LENGTH_SHORT).show();
                                        progressBarHOD.setVisibility(View.GONE);
                                        finish();
                                    }
                                }
                            });
                        }
                        else if(!(old_pass.equals(oldpasss)) && flag[0] == 0) {
                            oldpassHOD.setError("Wrong Old Password");
                            //Toast.makeText(getApplicationContext(),"Old Password is wrong, Try Again!", Toast.LENGTH_SHORT).show();
                            progressBarHOD.setVisibility(View.GONE);
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarHOD.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
