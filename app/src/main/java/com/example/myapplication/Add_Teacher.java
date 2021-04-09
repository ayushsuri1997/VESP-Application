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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Add_Teacher extends AppCompatActivity {

    EditText nameET,emailET,passwordET,addressET,contactET,emergencyContactET,teacherID;
    Button submit;
    TextView head;
    FirebaseAuth firebaseAuth;
    DatabaseReference dRef;
    Teacher_Details teacher_details;
    ImageButton backbutton,homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__teacher);
        backbutton = findViewById(R.id.back);
        homebutton = findViewById(R.id.home);
        init();

        final String password = createPassword();
        passwordET.setText(password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameET.getText().toString();
                final String email = emailET.getText().toString();
                final String tID=teacherID.getText().toString();
                final String address = addressET.getText().toString();
                final String contact = contactET.getText().toString();
                final String emergencyContact = emergencyContactET.getText().toString();

                if(name.isEmpty())
                {
                    nameET.setError("Enter Name");
                    nameET.requestFocus();
                    return;
                }
                if(email.isEmpty())
                {
                    emailET.setError("Enter E-mail");
                    emailET.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailET.setError("Enter a valid e-mail");
                    emailET.requestFocus();
                    return;
                }
                if(email.indexOf('@')!=-1) {
                    String check[] = email.split("@", 2);
                    if (!check[1].equals("ves.ac.in")) {
                        emailET.setError("Sign Up with your VES e-mail");
                        emailET.requestFocus();
                    }
                }
                else{
                    emailET.setError("In-Correct Email");
                    emailET.requestFocus();
                }
                passwordET.setEnabled(false);
                if(contact.isEmpty())
                {
                    contactET.setError("Enter Mobile Number");
                    contactET.requestFocus();
                    return;
                }
                if(contact.length()!=10)
                {
                    contactET.setError("Enter Valid Mobile Number");
                    contactET.requestFocus();
                    return;
                }
                if(emergencyContact.isEmpty())
                {
                    emergencyContactET.setError("Enter Mobile Number");
                    emergencyContactET.requestFocus();
                    return;
                }
                if(emergencyContact.length()!=10)
                {
                    emergencyContactET.setError("Enter Valid Mobile Number");
                    emergencyContactET.requestFocus();
                    return;
                }
                if(address.isEmpty())
                {
                    addressET.setError("Enter Address");
                    addressET.requestFocus();
                    return;
                }
                if(tID.isEmpty())
                {
                    teacherID.setError("Enter Id");
                    teacherID.requestFocus();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Add_Teacher.this,Teacher_Details.decider+" account created",Toast.LENGTH_SHORT).show();
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            dRef = FirebaseDatabase.getInstance().getReference(Teacher_Details.decider);
                            String id=dRef.push().getKey();
                            teacher_details = new Teacher_Details("1",uid,name,email,password,address,contact,emergencyContact,tID,Teacher_Details.decider);
                            dRef.child(tID).setValue(teacher_details);
                            startActivity(new Intent(getApplicationContext(), Principal_page.class));
                            finish();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                Toast.makeText(Add_Teacher.this,Teacher_Details.decider+"added",Toast.LENGTH_SHORT).show();
//                dRef = FirebaseDatabase.getInstance().getReference(Teacher_Details.decider);
//
//                String id=dRef.push().getKey();
//                teacher_details = new Teacher_Details("1",name,email,password,address,contact,emergencyContact,tID,Teacher_Details.decider);
//                dRef.child(tID).setValue(teacher_details);

            }
        });
    }

    public void init()
    {
        nameET =  (EditText) findViewById(R.id.name_et);
        emailET = (EditText) findViewById(R.id.email_et);
        passwordET = (EditText) findViewById(R.id.password_et);
        addressET = (EditText) findViewById(R.id.address_et);
        contactET = (EditText) findViewById(R.id.contact);
        emergencyContactET = (EditText) findViewById(R.id.e_contact);
        submit = (Button)findViewById(R.id.btn1);
        teacherID=(EditText)findViewById(R.id.teacher_id);
        head = (TextView) findViewById(R.id.head_tv);
        head.setText("Add " + Teacher_Details.decider);
        firebaseAuth = FirebaseAuth.getInstance();
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
