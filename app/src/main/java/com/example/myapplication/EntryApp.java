package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class EntryApp extends AppCompatActivity {
    private static final String TAG = "ExampleJobService";
    SliderLayout sliderLayout;

    Button pButton, hButton, sButton, studButton;
    DatabaseReference hodRefLogin,staffRefLogin,studRefLogin;
    FirebaseAuth hfirebaseAuth,pfirebaseAuth,sfirebaseAuth,studfirebaseAuth;
    FirebaseUser hfirebaseUser,pfirebaseUser,sfirebaseUser,studfirebaseUser;
    String currentUID,currentUIDStud;
    ArrayList<String> HODuid = new ArrayList<String>();
    ArrayList<String> Staffuid = new ArrayList<String>();
    ArrayList<String> Studuid = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_app);
        BroadcastRec receiver = new BroadcastRec();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        this.registerReceiver(receiver,intentFilter);
        sButton = findViewById(R.id.Staff);
        studButton = findViewById(R.id.Student);
        hButton = findViewById(R.id.HOD);
        pButton = findViewById(R.id.Principal);
//        startService(new Intent(getApplicationContext(),NotificationBackgroundService.class));
        scheduleJob();

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfirebaseAuth = FirebaseAuth.getInstance();
                pfirebaseUser = pfirebaseAuth.getCurrentUser();
                if (pfirebaseUser!=null) {
                    if (pfirebaseUser.getUid().equals("ZEF3eL8YHdU2p31IEtufgPylh1M2")) {
                        startActivity(new Intent(getApplicationContext(), Principal_page.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Already Logged in, Please logout from other account",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), Principal_Login.class));
                }
            }
        });

        hButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hfirebaseAuth = FirebaseAuth.getInstance();
                hfirebaseUser = hfirebaseAuth.getCurrentUser();
                if(hfirebaseUser!=null) {
                    hodRefLogin = FirebaseDatabase.getInstance().getReference("HOD");
                    hodRefLogin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String uid = Objects.requireNonNull(ds.child("uID").getValue()).toString();
                                HODuid.add(uid);
                                currentUID = hfirebaseUser.getUid();
                            }
                            if (HODuid.contains(currentUID)){
                                startActivity(new Intent(getApplicationContext(),HOD_page.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Already Logged in, Please logout from other account",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    startActivity(new Intent(getApplicationContext(), HOD_login.class));
                }
            }
        });

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfirebaseAuth = FirebaseAuth.getInstance();
                sfirebaseUser = sfirebaseAuth.getCurrentUser();
                if(sfirebaseUser!=null) {
                    staffRefLogin = FirebaseDatabase.getInstance().getReference("Teachers");
                    staffRefLogin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String uid = Objects.requireNonNull(ds.child("uID").getValue()).toString();
                                Staffuid.add(uid);
                                currentUID = sfirebaseAuth.getUid();
                            }
                            if (Staffuid.contains(currentUID)){
                                startActivity(new Intent(getApplicationContext(),TeacherPage.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Already Logged in, Please logout from other account",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    startActivity(new Intent(getApplicationContext(), StaffLogin.class));
                }
            }
        });

        studButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studfirebaseAuth = FirebaseAuth.getInstance();
                studfirebaseUser = studfirebaseAuth.getCurrentUser();
                if(studfirebaseUser!=null) {
                    studRefLogin = FirebaseDatabase.getInstance().getReference("All_Students");
                    studRefLogin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                for (DataSnapshot ds1 : ds.getChildren()){
                                    for (DataSnapshot ds2 : ds1.getChildren()) {
                                        String uid = Objects.requireNonNull(ds2.child("uniqueID").getValue()).toString();
                                        Studuid.add(uid);
                                        currentUIDStud = studfirebaseAuth.getUid();
                                    }
                                }
                            }
                            if (Studuid.contains(currentUIDStud)){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Already Logged in, Please logout from other account",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        });
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(3);
        setSliderViews();
    }

    private void setSliderViews() {
        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.background);
                    sliderView.setDescription(" 1" );
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.background2);
                    sliderView.setDescription(" 2" );
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.background3);
                    sliderView.setDescription(" 3" );
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.background);
                    sliderView.setDescription(" 4" );
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            /* sliderView.setDescription("setDescription " + (i + 1)); */
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(EntryApp.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();

                }
            });


            sliderLayout.addSliderView(sliderView);

        }
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, JobSchedularClass.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }
}