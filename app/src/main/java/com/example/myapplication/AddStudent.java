package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddStudent extends AppCompatActivity {

    Spinner course,batch;
    DatabaseReference dRef;
    FirebaseAuth fAuth1;
    TextView head;
    EditText name,roll_no,email,password,mobile,emergency,address;
    Button submit_form;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        head = (TextView) findViewById(R.id.head_tv);
        name = (EditText) findViewById(R.id.name_et);
        roll_no = (EditText) findViewById(R.id.roll_et);
        email = (EditText) findViewById(R.id.email_et);
        password = (EditText) findViewById(R.id.password_et);
        course = (Spinner) findViewById(R.id.course_et);
        mobile = (EditText) findViewById(R.id.mobile_et);
        emergency = (EditText) findViewById(R.id.emergence_mobile_et);
        address = (EditText) findViewById(R.id.address_et);
        submit_form = (Button) findViewById(R.id.submit_student_form);
        batch=(Spinner)findViewById(R.id.batch);
        dRef = FirebaseDatabase.getInstance().getReference("All_Students");
        fAuth1 = FirebaseAuth.getInstance();



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

        final String password1 = createPassword();
        password.setText(password1);

        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name1 = name.getText().toString().trim();
                final String roll1 = roll_no.getText().toString().trim();
                final String course1 = course.getSelectedItem().toString();
                final String email1 = email.getText().toString().trim();
                final String mobile1 = mobile.getText().toString().trim();
                final String emergency1 = emergency.getText().toString().trim();
                final String address1 = address.getText().toString().trim();
                final String batch1=batch.getSelectedItem().toString().trim();

                if(name1.isEmpty())
                {
                    name.setError("Enter Name");
                    name.requestFocus();
                    return;
                }
                if(roll1.isEmpty())
                {
                    roll_no.setError("Enter Roll Number");
                    roll_no.requestFocus();
                    return;
                }
                if(email1.isEmpty())
                {
                    email.setError("Enter E-mail");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches())
                {
                    email.setError("Enter a valid e-mail");
                    email.requestFocus();
                    return;
                }
                if(email1.indexOf('@')!=-1) {
                    String check[] = email1.split("@", 2);
                    if (!check[1].equals("ves.ac.in")) {
                        email.setError("Sign Up with your VES e-mail");
                        email.requestFocus();
                        return;
                    }
                }
                else{
                    email.setError("In-Correct Email");
                    email.requestFocus();
                    return;
                }

                password.setEnabled(false);

//                if(password1.isEmpty()){
//                    password.setError("Enter password");
//                    password.requestFocus();
//                    return;
//                }
//                if(password1.length() < 6){
//                    password.setError("Length should be greater than 5");
//                    password.requestFocus();
//                    return;
//                }
                if(mobile1.isEmpty())
                {
                    mobile.setError("Enter Mobile Number");
                    mobile.requestFocus();
                    return;
                }
                if(mobile1.length()!=10)
                {
                    mobile.setError("Enter Valid Mobile Number");
                    mobile.requestFocus();
                    return;
                }
                if(emergency1.isEmpty())
                {
                    emergency.setError("Enter Mobile Number");
                    emergency.requestFocus();
                    return;
                }
                if(emergency1.length()!=10)
                {
                    emergency.setError("Enter Valid Mobile Number");
                    emergency.requestFocus();
                    return;
                }
                if(address1.isEmpty())
                {
                    address.setError("Enter Address");
                    address.requestFocus();
                    return;
                }

                fAuth1.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(), "Student Account Created", Toast.LENGTH_SHORT).show();
                            String id = fAuth1.getCurrentUser().getUid();
                            Student_Details student_details = new Student_Details("1",id,name1,mobile1,emergency1,address1,email1,password1,roll1,batch1,"stud");
                            dRef.child(batch1).child(course1).child(roll1).setValue(student_details);
                            startActivity(new Intent(getApplicationContext(), TeacherPage.class));
                            finish();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    private String createPassword() {

        Random r = new Random();
        String pass = "";
        for(int i = 0 ; i < 8 ; i++ )
        {
            char c = (char)(r.nextInt(26) + 97);
            pass = pass + c;
        }
        return pass;
    }
}
