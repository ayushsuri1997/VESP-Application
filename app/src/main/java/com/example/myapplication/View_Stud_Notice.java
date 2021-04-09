package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class View_Stud_Notice extends AppCompatActivity {

    RecyclerView recyclerViewNotice;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference databaseReference;
    ArrayList<String> arrayListStud = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__stud__notice);

        recyclerViewNotice = (RecyclerView) findViewById(R.id.noticeListStud);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshStud);
        databaseReference = FirebaseDatabase.getInstance().getReference("Principal_to_HOD,Staff_and_Students");
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(this));

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayListStud.clear();
                recyclerViewNotice.setAdapter(new NoticeAdapter(arrayListStud));
                getData();
            }
        });
    }

    public void getData() {
        swipeRefreshLayout.setRefreshing(true);
        final String[] s = new String[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayListStud.add(s[0]);
                }
                NoticeAdapter noticeAdapter = new NoticeAdapter(arrayListStud);
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
