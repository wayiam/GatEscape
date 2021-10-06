package com.example.gatescape.daos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gatescape.models.RequestInfo;
import com.example.gatescape.models.UserData;
import com.example.gatescape.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class UserDao {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CollectionReference userCollection = db.collection("Users");
    UserData user;
    FirebaseUser fireUser = mAuth.getCurrentUser();
    private String TAG = "UserDao";

    public void addUser(UserData user, String uid) {
        if (user != null) {
            userCollection.document(uid).set(user);
        }
    }

    public void getUserById(String text) {

        DocumentReference documentReference = userCollection.document(fireUser.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(UserData.class);
                if (user != null) {
                    Log.i(TAG, "User is not NULL !");
                    Log.i(TAG, "Operation Successful");

                    Long currentTime = System.currentTimeMillis();
                    CollectionReference ReqCollection = db.collection("Requests");
                    RequestInfo gp = new RequestInfo(text, user, currentTime, false);
                    ReqCollection.document().set(gp);
                }
            }

        });
    }
}