package com.example.myapplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationBackgroundService extends Service {
    DatabaseReference databaseReference, hodRef,staffRef,studRef;
    DatabaseReference prin_to_hod_staff,prin_to_hod_staff_student;
    NotificationHelper notificationHelper;
    FirebaseUser firebaseUser;

    int not_1_i =1;
    int not_2_i =1;
    int not_3_i =1;
    int not_4_i =1;
    int not_5_i =1;
    int not_6_i =1;

    private static final int notification_one = 1;
    private static final int notification_two = 2;
    private static final int notification_three = 3;
    private static final int notification_four = 4;
    private static final int notification_five = 5;
    private static final int notification_six = 6;

    private static long pri_hod_count = 0;
    private static long pri_hod_staff_count = 0;
    private static long pri_hod_staff_student_count = 0;
    String currentUID;
    ArrayList<String> uidList = new ArrayList<String>();
    ArrayList<String> staffUidList = new ArrayList<String>();
    ArrayList<String> studUidList = new ArrayList<String>();


    public NotificationBackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ENTER","ENTERING");
        onTaskRemoved(intent);

        uidList.clear();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null) {
            hodRef = FirebaseDatabase.getInstance().getReference("HOD");
            staffRef = FirebaseDatabase.getInstance().getReference("Teachers");
            studRef = FirebaseDatabase.getInstance().getReference("All_Students");

            databaseReference = FirebaseDatabase.getInstance().getReference("Principal_to_HOD");
            prin_to_hod_staff = FirebaseDatabase.getInstance().getReference("Principal_to_HOD_and_Staff");
            prin_to_hod_staff_student = FirebaseDatabase.getInstance().getReference("Principal_to_HOD,Staff_and_Students");

            currentUID = firebaseUser.getUid();

            notificationHelper = new NotificationHelper(getApplicationContext());

            hodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String uid = Objects.requireNonNull(ds.child("uID").getValue()).toString();
                        uidList.add(uid);

                        if (uidList.contains(currentUID)) {
                            //hod notification
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long var = snapshot.getChildrenCount();
                                    String Title = "";
                                    String Message = "";

                                    if (var > pri_hod_count) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_1_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_1_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_one, Title, Message);
                                        }
                                        pri_hod_count = var;
                                    }
                                    if (var < pri_hod_count) {
                                        pri_hod_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            prin_to_hod_staff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String Message="";
                                    String Title="";
                                    long var = snapshot.getChildrenCount();
                                    if (var > pri_hod_staff_count) {

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_2_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_2_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_two, Title, Message);
                                        }
                                        pri_hod_staff_count = var;
                                    }
                                    if (var < pri_hod_staff_count) {
                                        pri_hod_staff_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            prin_to_hod_staff_student.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long var = snapshot.getChildrenCount();
                                    String Message="";
                                    String Title="";

                                    if (var > pri_hod_staff_student_count) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_3_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_3_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_three, Title, Message);
                                        }
                                        pri_hod_staff_student_count = var;
                                    }
                                    if (var < pri_hod_staff_student_count) {
                                        pri_hod_staff_student_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            staffRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String uid = Objects.requireNonNull(ds.child("uID").getValue()).toString();
                        staffUidList.add(uid);
                        if (staffUidList.contains(currentUID)) {
                            //staff notification
                            prin_to_hod_staff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long var = snapshot.getChildrenCount();
                                    String Message="";
                                    String Title="";
                                    if (var > pri_hod_staff_count) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_4_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_4_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_four, Title, Message);
                                        }
                                        pri_hod_staff_count = var;
                                    }
                                    if (var < pri_hod_staff_count) {
                                        pri_hod_staff_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            prin_to_hod_staff_student.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long var = snapshot.getChildrenCount();
                                    String Message="";
                                    String Title="";
                                    if (var > pri_hod_staff_student_count) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_5_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_5_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_five, Title, Message);
                                        }
                                        pri_hod_staff_student_count = var;
                                    }
                                    if (var < pri_hod_staff_student_count) {
                                        pri_hod_staff_student_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            studRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()){
                            for (DataSnapshot ds2 : ds1.getChildren()) {
                                String uid = Objects.requireNonNull(ds2.child("uniqueID").getValue()).toString();
                                studUidList.add(uid);
                            }
                        }
                        if (studUidList.contains(currentUID)) {
                            //student notification
                            prin_to_hod_staff_student.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long var = snapshot.getChildrenCount();
                                    String Message="";
                                    String Title="";
                                    if (var > pri_hod_staff_student_count) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (not_6_i == var) {
                                                Title = Objects.requireNonNull(dataSnapshot.child("by").getValue()).toString();
                                                Message = Objects.requireNonNull(dataSnapshot.child("data").getValue()).toString();
                                            } else {
                                                not_6_i++;
                                            }
                                        }
                                        if(currentUID!="NOLOGGEDINUSER") {
                                            postNotification(notification_six, Title, Message);
                                        }
                                        pri_hod_staff_student_count = var;
                                    }
                                    if (var < pri_hod_staff_student_count) {
                                        pri_hod_staff_student_count = var;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            currentUID = "NOLOGGEDINUSER";
            Log.d("USER","null "+ currentUID);

        }
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);
        Intent restartServiceIntent = new Intent(getApplicationContext(),NotificationBackgroundService.class);
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
    }

    @Override
    public void onDestroy() {
        firebaseUser=null;
        Log.d("TAg","SErvice Stopped");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void postNotification(int id, String title,String message) {
        Notification.Builder notificationBuilder = notificationHelper.getNotification1(title, message);

        if (notificationBuilder != null) {
            notificationHelper.notify(id, notificationBuilder);
        }
    }
}
