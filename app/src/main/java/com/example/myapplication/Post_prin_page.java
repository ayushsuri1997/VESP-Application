package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post_prin_page extends AppCompatActivity {


    EditText postDetailsPrin;
    Button shareBTN;
    Spinner selectSharemode;
    DatabaseReference databaseReference;
    Date currentTime = Calendar.getInstance().getTime();
    private NotificationHelper notificationHelper;
    public static final String DATE_FORMAT_2 = "yyyy:MM:dd HH:mm:ss EEE";
    private static final int notification_one = 1;
    String Title = "You've posted the message.";
    String Message= "Check it...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_prin_page);


        postDetailsPrin = (EditText) findViewById(R.id.postDetail);
        shareBTN = (Button) findViewById(R.id.sharePrin);
        selectSharemode = (Spinner) findViewById(R.id.spinnerModeSelector);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);



        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postData = postDetailsPrin.getText().toString().trim();
                String mode = selectSharemode.getSelectedItem().toString();
                if(TextUtils.isEmpty(postData)){
                    postDetailsPrin.setError("Enter data here");
                    return;
                }
                String dbName = mode.replaceAll(" ","_");
                databaseReference = FirebaseDatabase.getInstance().getReference(dbName);
                PostData postData1 = new PostData("Principal",postData);
                databaseReference.child(dateFormat.format(currentTime).toString()).setValue(postData1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notificationHelper = new NotificationHelper(Post_prin_page.this);
                        postNotification(notification_one, Title, Message);
                    }
                });
                //Once data is posted by principal
                Toast.makeText(getApplicationContext(),"Posted Data to " +mode,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void postNotification(int id, String title,String message) {
        Notification.Builder notificationBuilder = notificationHelper.getNotification1(title, message);

        if (notificationBuilder != null) {
            notificationHelper.notify(id, notificationBuilder);
        }
    }

}
