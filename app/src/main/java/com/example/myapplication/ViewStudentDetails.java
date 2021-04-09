package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Vector;

public class ViewStudentDetails extends AppCompatActivity {
    Spinner branch_sp,batch_sp;
    Button submit_btn;
    ListView student_listview;
    DatabaseReference dRef,mRef;
    ArrayList<String> roll_nos = new ArrayList();
    ArrayAdapter adapter;
    Student_Details s1;
    String branch,batch;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_details);

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        branch_sp = (Spinner) findViewById(R.id.branch);
        submit_btn = (Button) findViewById(R.id.submit_det);
        student_listview = (ListView) findViewById(R.id.student_list);
        batch_sp=(Spinner)findViewById(R.id.batch);


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


        Vector v = new Vector();
        v.add("Hello");
        v.add("Hello");
        v.add("Hello");

        adapter = new ArrayAdapter(ViewStudentDetails.this, android.R.layout.simple_list_item_1, roll_nos);
        student_listview.setAdapter(adapter);
        dRef = FirebaseDatabase.getInstance().getReference("All_Students");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branch = branch_sp.getSelectedItem().toString();
                batch = batch_sp.getSelectedItem().toString();
                roll_nos.clear();
                dRef.child(batch).child(branch).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            roll_nos.add(ds.getValue(Student_Details.class).getRoll_no());
                            //   Toast.makeText(View_Student_Details.this,ds.getValue(Student_Details.class).getRoll_no()+" ",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Error - " +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        student_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                branch = branch_sp.getSelectedItem().toString();
                batch = batch_sp.getSelectedItem().toString();
                mRef = FirebaseDatabase.getInstance().getReference("All_Students/"+batch+"/"+branch+"/"+roll_nos.get(position));
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewStudentDetails.this);
                final View rview = getLayoutInflater().inflate(R.layout.student_details_lay, null);
                final TextView name = (TextView) rview.findViewById(R.id.txt2);
                final TextView roll = (TextView) rview.findViewById(R.id.txt4);
                final TextView email = (TextView) rview.findViewById(R.id.txt6);
                final TextView password = (TextView) rview.findViewById(R.id.txt8);
                final TextView contact = (TextView) rview.findViewById(R.id.txt10);
                final TextView emergency = (TextView) rview.findViewById(R.id.txt12);
                final TextView address = (TextView) rview.findViewById(R.id.txt14);
                final TextView batch1 = (TextView) rview.findViewById(R.id.txt16);

                //Toast.makeText(getApplicationContext(), mRef.getPath()+"", Toast.LENGTH_SHORT).show();
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        s1 = dataSnapshot.getValue(Student_Details.class);
                        //Toast.makeText(View_Student_Details.this, s1.getName()+"", Toast.LENGTH_SHORT).show();
                        name.setText(s1.getName());
                        roll.setText(s1.getRoll_no());
                        email.setText(s1.getEmail());
                        password.setText(s1.getPassword());
                        contact.setText(s1.getMobile());
                        emergency.setText(s1.getEmergency_mob());
                        address.setText(s1.getAddress());
                        batch1.setText(s1.getBatch());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Error - " +databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setView(rview);
                AlertDialog alert = alertDialog.create();
                alert.show();

            }
        });



    }
}
