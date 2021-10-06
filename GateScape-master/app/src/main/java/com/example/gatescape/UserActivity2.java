package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gatescape.daos.RequestDao;
import com.example.gatescape.daos.UserDao;
import com.example.gatescape.models.TeacherData;
import com.example.gatescape.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class UserActivity2 extends AppCompatActivity {

    Button apply_button;
    TeacherData tea_user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    TextInputLayout Reason_text;
    private String TAG = "UserActivity2";
    private int SMS_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        apply_button = findViewById(R.id.apply_button);
        Reason_text = findViewById(R.id.Reason_text);

        CollectionReference colRef = db.collection("Teachers");

        colRef.whereEqualTo("design" , "Head of Department")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                tea_user = document.toObject(TeacherData.class);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reason = Objects.requireNonNull(Reason_text.getEditText()).getText().toString();
                RequestDao requestDao = new RequestDao();
                if(!reason.isEmpty()) {
                    requestDao.addRequest(reason);
                    Notification();
                    String msg = currentUser.getDisplayName()+" student of CSE branch has sent application for gate pass.";
                    sendSMSMessage(tea_user.getPhone_no() , msg);
                    finish();
                }
                else {
                    Reason_text.setError("Reason can't be empty");
                    recreate();
                }
            }
        });
    }
    protected void sendSMSMessage(String receiver_no , String msg) {

        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);

        }else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receiver_no, null, msg,
                    null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void Notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n" , "n" , NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , "n")
                .setContentText("GateScape")
                .setSmallIcon(R.drawable.ic_user)
                .setAutoCancel(true)
                .setContentText("Your GatePass request has been sent.");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999 , builder.build());
    }
}