package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.gatescape.daos.UserDao;
import com.example.gatescape.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Button Login, SignUp;
    TextInputLayout firstname, lastname, roll_no, email, phone_no, password , parent_no;
    AutoCompleteTextView branch , sem;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    CollectionReference userCollection;
    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Login = findViewById(R.id.LoginButton);
        SignUp = findViewById(R.id.SignUpButton);
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        branch = findViewById(R.id.Branch);
        sem = findViewById(R.id.Sem);
        roll_no = findViewById(R.id.roll_no);
        email = findViewById(R.id.Email);
        phone_no = findViewById(R.id.phone_no);
        parent_no = findViewById(R.id.parent_phone_no);
        password = findViewById(R.id.Password);

        String[] semOptions = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th"};
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(SignUpActivity.this, R.layout.option_item, semOptions);
        sem.setAdapter(semAdapter);

        String[] branchOptions = {"CSE" , "CE" , "ME" , "EEE" , "ET&T"};
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SignUpActivity.this, R.layout.option_item, branchOptions);
        branch.setThreshold(2);
        branch.setAdapter(branchAdapter);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateFirstName() | !validateLastName() | !validateBranch() | !validateSem() |
                        !validateRoll_no() | !validateEmail() | !validatePhone_no() | !validateParent_Phone_no()
                | !validatePassword()){
                    return;
                }else{
                    CreateUserAuth();
                }
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void CreateUserAuth() {

        String first_name = firstname.getEditText().getText().toString();
        String last_name = lastname.getEditText().getText().toString();
        String user_branch = branch.getText().toString();
        String user_sem = sem.getText().toString();
        String user_roll_no = roll_no.getEditText().getText().toString();
        String user_email = email.getEditText().getText().toString();
        String user_phone_no = phone_no.getEditText().getText().toString();
        String parent_phone_no = parent_no.getEditText().getText().toString();
        String user_password = password.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String user_type = "User";
                            UserData userData = new UserData(first_name+" "+last_name, user_branch ,user_sem,
                                    user_roll_no, user_email, user_phone_no, parent_phone_no, user_password ,
                                    user_type);
                            UserDao userDao = new UserDao();
                            userDao.addUser(userData , Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            startActivity(new Intent(SignUpActivity.this , LoginActivity.class));
            finish();
        }else{
            return;
        }
    }

    //Validate function for all the field , to ensure that they are not empty before SignUp.
    private Boolean validateFirstName() {
        String val = firstname.getEditText().getText().toString();

        if(val.isEmpty()){
            firstname.setError("Field cannot be empty");
            return (false);
        }
        else{
            firstname.setError(null);
            return true;
        }
    }
    private Boolean validateLastName() {
        String val = lastname.getEditText().getText().toString();

        if(val.isEmpty()){
            lastname.setError("Field cannot be empty");
            return (false);
        }
        else{
            lastname.setError(null);
            return true;
        }
    }

    private Boolean validateBranch() {
        String val = branch.getText().toString();

        if(val.isEmpty()){
            branch.setError("Field cannot be empty");
            return (false);
        }
        else{
            branch.setError(null);
            return true;
        }
    }

    private Boolean validateSem() {
        String val = sem.getText().toString();

        if(val.isEmpty()){
            sem.setError("Field cannot be empty");
            return (false);
        }
        else{
            sem.setError(null);
            return true;
        }
    }

    private Boolean validateRoll_no() {
        String val = roll_no.getEditText().getText().toString();

//        Boolean check_roll ;
//        CollectionReference colRef = db.collection("users");
//        colRef.whereEqualTo("roll_no" , val)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    check_roll = false;
//                }
//            }
//        });

        if(val.isEmpty()){
            roll_no.setError("Field cannot be empty");
            return (false);
        } else{
            roll_no.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return (false);
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePhone_no() {
        String val = phone_no.getEditText().getText().toString();

        if(val.isEmpty()){
            phone_no.setError("Field cannot be empty");
            return (false);
        }
        else{
            phone_no.setError(null);
            return true;
        }
    }

    private Boolean validateParent_Phone_no() {
        String val = parent_no.getEditText().getText().toString();

        if(val.isEmpty()){
            parent_no.setError("Field cannot be empty");
            return (false);
        }
        else{
            parent_no.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return (false);
        }
        else{
            password.setError(null);
            return true;
        }
    }
}