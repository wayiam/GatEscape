package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gatescape.daos.UserDao;
import com.example.gatescape.models.UserData;
import com.example.gatescape.util.FirebaseUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button  Login , SignUp , ForgetPass, TeacherSignUp ;
    TextInputLayout Email , Password;
    TextView welcome_text , continue_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Login = findViewById(R.id.LoginButton1);
        SignUp = findViewById(R.id.SignUpButton1);
        TeacherSignUp = findViewById(R.id.SignUpButton2);
        ForgetPass = findViewById(R.id.ForgotPassword);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password1);
        welcome_text = findViewById(R.id.welcomeText1);
        continue_text = findViewById(R.id.continue_text1);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , SignUpActivity.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this , Pair.create(welcome_text , "logo_text"),
                Pair.create(continue_text , "continue_text"), Pair.create(Email , "username_text"), Pair.create(ForgetPass , "middle_text") ,
                Pair.create(Password , "password_text") , Pair.create(Login , "login_singUp"), Pair.create(SignUp , "signUp_login"));

                startActivity(intent , options.toBundle());
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateRoll_no() | !validatePassword()){
                    return;
                }else{
                    firebaseAuth();
                }
            }
        });

        TeacherSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , Teacher_signup.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser , currentUser.getUid());
        }
    }

    private void firebaseAuth() {

        String email = Email.getEditText().getText().toString();
        String password = Password.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user , Objects.requireNonNull(user).getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null , null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser firebaseUser , String uid) {

        if(firebaseUser != null){

            DocumentReference docRef = db.collection("Users").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            startActivity(new Intent(LoginActivity.this , UserActivity3.class));
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this , TeacherActivity2.class));
                            finish();
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }else{
            return ;
        }
    }

    // validation function for Roll_no and Password , to ensure that they are not Empty.
    private Boolean validateRoll_no(){

        String val = Email.getEditText().getText().toString();

        if(val.isEmpty()){
            Email.setError("Field cannot be empty");
            return false;
        }else{
            Email.setError(null);
            Email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){

        String val = Password.getEditText().getText().toString();

        if(val.isEmpty()){
            Password.setError("Field cannot be empty");
            return false;
        }else{
            Password.setError(null);
            Password.setErrorEnabled(false);
            return true;
        }
    }
}