package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class View_Staff_Notice extends AppCompatActivity {

    RecyclerView recyclerViewNotice;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference databaseReference,databaseReference1;
    ImageButton backbutton,homebutton;
    ArrayList<String> arrayListStaff = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__staff__notice);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EntryApp.class));
            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TeacherPage.class));

            }
        });


        recyclerViewNotice = (RecyclerView) findViewById(R.id.noticeListStaff);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshStaff);
        databaseReference = FirebaseDatabase.getInstance().getReference("Principal_to_HOD_and_Staff");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Principal_to_HOD,Staff_and_Students");
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(this));

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayListStaff.clear();
                recyclerViewNotice.setAdapter(new NoticeAdapter(arrayListStaff));
                getData();
            }
        });
    }

    public void getData() {
        swipeRefreshLayout.setRefreshing(true);
        final String[] s = new String[1];

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayListStaff.add(s[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayListStaff.add(s[0]);
                }
                NoticeAdapter noticeAdapter = new NoticeAdapter(arrayListStaff);
                recyclerViewNotice.setAdapter(noticeAdapter);
                noticeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
