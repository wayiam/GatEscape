package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gatescape.models.RequestInfo;
import com.example.gatescape.util.TimeAgo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity4 extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ReqCollection = db.collection("Requests");
    private final String TAG = "UserActivity4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user4);

        TextView creator_name = findViewById(R.id.creator_name);
        TextView creator_branch = findViewById(R.id.creator_branch);
        TextView creator_sem = findViewById(R.id.creator_Sem);
        TextView creator_roll_no = findViewById(R.id.creator_roll_no);
        TextView creation_time = findViewById(R.id.creation_time);
        TextView creator_reason = findViewById(R.id.creator_reason);
        ImageView approve = findViewById(R.id.approve_image);


        String ReqDocId = getIntent().getStringExtra("RequestId");

        DocumentReference docRef = ReqCollection.document(ReqDocId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.i(TAG , "document INfo => "+ documentSnapshot.getData());
                RequestInfo creator_Info = documentSnapshot.toObject(RequestInfo.class);
                if(creator_Info != null) {
                    creator_name.setText("Name : "+creator_Info.getUser().getName());
                    creator_branch.setText("Branch : "+creator_Info.getUser().getBranch());
                    creator_sem.setText("Semester : "+creator_Info.getUser().getSem());
                    creator_roll_no.setText("Roll No. : "+creator_Info.getUser().getRoll_no());
                    creation_time.setText("Created at : "+ TimeAgo.getTimeAgo(creator_Info.getCreatedAt()));
                    creator_reason.setText(creator_Info.getReason());

                    creator_reason.setMovementMethod(new ScrollingMovementMethod());

                    if(creator_Info.getApprove() == true){
                        approve.setImageResource(R.drawable.fapproved);
                    }else{
                        approve.setImageResource(R.drawable.wait1);
                    }
                }
            }
        });
    }
}