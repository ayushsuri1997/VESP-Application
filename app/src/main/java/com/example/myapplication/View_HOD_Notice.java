package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class View_HOD_Notice extends AppCompatActivity {

    RecyclerView recyclerViewNotice;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> arrayList1 = new ArrayList<String>();
    ArrayList<String> arrayList2 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__h_o_d__notice);

        recyclerViewNotice = (RecyclerView) findViewById(R.id.noticeList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        databaseReference = FirebaseDatabase.getInstance().getReference("Principal_to_HOD");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Principal_to_HOD_and_Staff");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Principal_to_HOD,Staff_and_Students");
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(this));

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                recyclerViewNotice.setAdapter(new NoticeAdapter(arrayList));
                getData();
            }
        });
    }

    public void getData() {
        swipeRefreshLayout.setRefreshing(true);
        final String[] s = new String[1];

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayList.add(s[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayList.add(s[0]);
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
                for (DataSnapshot ds : snapshot.getChildren()) {
                    s[0] = Objects.requireNonNull(ds.child("data").getValue()).toString();
                    arrayList.add(s[0]);
                }
                NoticeAdapter noticeAdapter = new NoticeAdapter(arrayList);
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
