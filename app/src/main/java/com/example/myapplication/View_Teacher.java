package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class View_Teacher extends AppCompatActivity {

    ListView listView;
    DatabaseReference dRef;
    Teacher_Details teacher_details = new Teacher_Details();
    Teacher_Details newObject;
    private ArrayAdapter arrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__teacher);
        teacher_details.clearList();
        listView = (ListView) findViewById(R.id.t_listview);
        dRef = FirebaseDatabase.getInstance().getReference(Teacher_Details.decider);
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    newObject = ds.getValue(Teacher_Details.class);
                    //teacher_detailsArrayList.add(ds.getValue(Teacher_Details.class));
                    teacher_details.addObject(newObject);
                    Toast.makeText(getApplicationContext(),"List of Teachers Here",Toast.LENGTH_SHORT).show();
                }
                teacher_details.compareNames(teacher_details.returnArrayList());
                arrayAdapter = new ArrayAdapter(View_Teacher.this,android.R.layout.simple_list_item_1,teacher_details.getListName());
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newObject = teacher_details.returnArrayList().get(i);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(View_Teacher.this);
                final View rview = getLayoutInflater().inflate(R.layout.view_teacher_lay, null);
                TextView name =(TextView) rview.findViewById(R.id.t_name);
                TextView mail=(TextView)rview.findViewById(R.id.t_email);
                TextView contact=(TextView)rview.findViewById(R.id.t_contact);
                TextView emergencyContact=(TextView)rview.findViewById(R.id.t_emergency_contact);
                TextView address=(TextView)rview.findViewById(R.id.t_address);
                TextView teacherID=(TextView)rview.findViewById(R.id.t_id);

                name.setText(newObject.getName());
                mail.setText(newObject.getEmail());
                contact.setText(newObject.getContact());
                emergencyContact.setText(newObject.getEmergencyContact());
                address.setText(newObject.getAddress());
                teacherID.setText(newObject.getTeacher_id());

                alertDialog.setView(rview);
                AlertDialog alert = alertDialog.create();
                alert.show();



            }
        });
    }
}
