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


public class ResetPasswordStaff extends AppCompatActivity {

    EditText oldpassSatff,newpassStaff,renewpassStaff;
    Button reset_meStaff;
    ProgressBar progressBarStaff;
    FirebaseAuth fAuthStaff;
    DatabaseReference databaseProfileDataStaff;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_staff);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        oldpassSatff = findViewById(R.id.resetpasswordStaff);
        newpassStaff = findViewById(R.id.newpasswordStaff);
        renewpassStaff = findViewById(R.id.reenternewpasswordStaff);
        reset_meStaff = findViewById(R.id.resetmeStaff);
        progressBarStaff = findViewById(R.id.reset_progressStaff);
        String path = getpathDBStaff.getPath();
        databaseProfileDataStaff = FirebaseDatabase.getInstance().getReference(path);
        fAuthStaff = FirebaseAuth.getInstance();

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

        reset_meStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old_pass = oldpassSatff.getText().toString().trim();
                final String new_pass = newpassStaff.getText().toString().trim();
                String re_new = renewpassStaff.getText().toString().trim();

                if(TextUtils.isEmpty(old_pass)) {
                    oldpassSatff.setError("Old Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(new_pass)) {
                    newpassStaff.setError("New Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(re_new)) {
                    renewpassStaff.setError("Re-enter New Password.");
                    return;
                }

                if(!(new_pass.equals(re_new))) {
                    Toast.makeText(getApplicationContext(),"Password Don't Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(old_pass.length() < 6){
                    oldpassSatff.setError("Password Must be >= 6 Characters");
                    return;
                }

                if(new_pass.length() < 6){
                    newpassStaff.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBarStaff.setVisibility(View.VISIBLE);

                final FirebaseUser user = fAuthStaff.getCurrentUser();
                //final String id = user.getUid();
                final int[] flag = {0};

                databaseProfileDataStaff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oldpasss = dataSnapshot.child("password").getValue().toString();
                        if(old_pass.equals(oldpasss)){
                            user.updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        databaseProfileDataStaff.child("authID").setValue("2");
                                        databaseProfileDataStaff.child("password").setValue(new_pass);
                                        //fAuth.signOut();
                                        //startActivity(new Intent(getApplicationContext(),Login.class));
                                        Toast.makeText(getApplicationContext(),"Password Changed", Toast.LENGTH_SHORT).show();
                                        flag[0] = 1;
                                        progressBarStaff.setVisibility(View.GONE);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Password Didn't Change ", Toast.LENGTH_SHORT).show();
                                        progressBarStaff.setVisibility(View.GONE);
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
                            oldpassSatff.setError("Wrong Old Password");
                            //Toast.makeText(getApplicationContext(),"Old Password is wrong, Try Again!", Toast.LENGTH_SHORT).show();
                            progressBarStaff.setVisibility(View.GONE);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarStaff.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
}
