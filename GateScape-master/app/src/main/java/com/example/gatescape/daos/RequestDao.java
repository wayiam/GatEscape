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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class RequestDao {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ReqCollection = db.collection("Requests");
    CollectionReference userCollection = db.collection("users");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser fireUser = auth.getCurrentUser();
    private final String TAG = "RequestDao";
    UserData user;

    public void addRequest(String text) {

        UserDao userDao = new UserDao();
        userDao.getUserById(text);
    }
}
