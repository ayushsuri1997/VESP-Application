package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class ResetPassword extends AppCompatActivity {

    EditText oldpass,newpass,renewpass;
    Button reset_me;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    DatabaseReference databaseProfileData;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        oldpass = findViewById(R.id.resetpassword);
        newpass = findViewById(R.id.newpassword);
        renewpass = findViewById(R.id.reenternewpassword);
        reset_me = findViewById(R.id.resetme);
        progressBar = findViewById(R.id.reset_progress);
        String path = getpathDB.getPath();
        databaseProfileData = FirebaseDatabase.getInstance().getReference(path);
        fAuth = FirebaseAuth.getInstance();

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

        reset_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old_pass = oldpass.getText().toString().trim();
                final String new_pass = newpass.getText().toString().trim();
                String re_new = renewpass.getText().toString().trim();

                if(TextUtils.isEmpty(old_pass)) {
                    oldpass.setError("Old Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(new_pass)) {
                    newpass.setError("New Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(re_new)) {
                    renewpass.setError("Re-enter New Password.");
                    return;
                }

                if(!(new_pass.equals(re_new))) {
                    Toast.makeText(getApplicationContext(),"Password Don't Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(old_pass.length() < 6){
                    oldpass.setError("Password Must be >= 6 Characters");
                    return;
                }

                if(new_pass.length() < 6){
                    newpass.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                final FirebaseUser user = fAuth.getCurrentUser();
                //final String id = user.getUid();
                final int[] flag = {0};
                databaseProfileData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldpasss = dataSnapshot.child("password").getValue().toString();
                        if(old_pass.equals(oldpasss)){
                            user.updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        databaseProfileData.child("authID").setValue("2");
                                        databaseProfileData.child("password").setValue(new_pass);
                                        //fAuth.signOut();
                                        //startActivity(new Intent(getApplicationContext(),Login.class));
                                        Toast.makeText(getApplicationContext(),"Password Changed", Toast.LENGTH_SHORT).show();
                                        flag[0] = 1;
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Password Didn't Change ", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                    }
                                }
                            });
//                            PasswordData passData = new PasswordData(new_pass);
//                            UpdateAuthID updateAuthID = new UpdateAuthID("2");
//                            databaseProfileData.child(id).child("authID").setValue("2");
//                            databaseProfileData.child(id).setValue(passData);
//                            fAuth.signOut();
//                            startActivity(new Intent(getApplicationContext(),Login.class));
//                            Toast.makeText(getApplicationContext(),"Password Changed", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
                        }
                        else if(!(old_pass.equals(oldpasss)) && flag[0] == 0) {
                            oldpass.setError("Wrong Old Password");
                            //Toast.makeText(getApplicationContext(),"Old Password is wrong, Try Again!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
