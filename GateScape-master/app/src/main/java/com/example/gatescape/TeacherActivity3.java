package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gatescape.models.RequestInfo;
import com.example.gatescape.models.UserData;
import com.example.gatescape.util.FirebaseUtil;
import com.example.gatescape.util.TimeAgo;
import com.example.gatescape.util.Ver_code;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TeacherActivity3 extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ReqCollection = db.collection("Requests");
    private final String TAG = "TeacherActivity3";
    Boolean approve_check;
    String applicant_no;
    String guardian_no;
    String username;
    private final String guard_no = "+919907606659";
    String code = String.valueOf(Ver_code.OTP(06));
    public static final int SMS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher3);

        TextView T_creator_name = findViewById(R.id.T_creator_name);
        TextView T_creator_branch = findViewById(R.id.T_creator_branch);
        TextView T_creator_sem = findViewById(R.id.T_creator_Sem);
        TextView T_creator_roll_no = findViewById(R.id.T_creator_roll_no);
        TextView T_creation_time = findViewById(R.id.T_creation_time);
        TextView T_creator_reason = findViewById(R.id.T_creator_reason);
        Button T_isApprove = findViewById(R.id.T_approve_button);


        String ReqDocId = getIntent().getStringExtra("T_RequestId");

        DocumentReference docRef = ReqCollection.document(ReqDocId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                approve_check = documentSnapshot.getBoolean("approve");
                applicant_no = documentSnapshot.getString("user.phone_no");
                guardian_no = documentSnapshot.getString("user.parent_no");
                username = documentSnapshot.getString("user.name");

                if (!approve_check) {
                    T_isApprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            docRef.update("approve", true);

                            String guard_msg = "Gate pass request of "+username+" has been accepted verify his/her approved request with the verification code and let them go.\n Verification code : "+code;
                            String applicant_msg = "Your gate pass request has been approved \n"+"Verification code : "+code;
                            String parent_msg = "Your child "+username+ " had applied for gate pass and his/her request has been accepted";
                            sendSMSMessage(guard_no , guard_msg);
                            sendSMSMessage(applicant_no , applicant_msg);
                            sendSMSMessage(guardian_no , parent_msg);
                            recreate();
                        }
                    });
                }else{
                    T_isApprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            docRef.update("approve" , false);
                            recreate();
                        }
                    });
                }
            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                RequestInfo creator_Info = documentSnapshot.toObject(RequestInfo.class);
                if(creator_Info != null) {
                    T_creator_name.setText("Name : "+creator_Info.getUser().getName());
                    T_creator_branch.setText("Branch : "+creator_Info.getUser().getBranch());
                    T_creator_sem.setText("Semester : "+creator_Info.getUser().getSem());
                    T_creator_roll_no.setText("Roll No. : "+creator_Info.getUser().getRoll_no());
                    T_creation_time.setText("Created at : "+ TimeAgo.getTimeAgo(creator_Info.getCreatedAt()));
                    T_creator_reason.setText(creator_Info.getReason());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_REQUEST_CODE) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG , "SMS permission granted ");

                String guard_msg = "Gate pass request of "+username+" has been accepted verify his/her approved request with the" +
                        " verification code and allow them to go home.\nVerification code : "+code;
                String applicant_msg = "Your gate pass request has been approved \n"+"Verification code : "+code;
                String parent_msg = "Your child "+username+ " had applied for gate pass and his/her request has been accepted";
                sendSMSMessage(guard_no , guard_msg);
                sendSMSMessage(applicant_no , applicant_msg);
                sendSMSMessage(guardian_no , parent_msg);
            } else {
                Toast.makeText(getApplicationContext(), "SMS failed, please try again.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}