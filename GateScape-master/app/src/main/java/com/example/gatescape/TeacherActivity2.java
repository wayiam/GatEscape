package com.example.gatescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gatescape.Adapters.RequestAdapter;
import com.example.gatescape.Adapters.T_RequestAdapter;
import com.example.gatescape.models.RequestInfo;
import com.example.gatescape.util.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TeacherActivity2 extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private T_RequestAdapter adapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teacher2);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);

        View header = navigationView.getHeaderView(0);
        TextView nav_Username = header.findViewById(R.id.nav_username);
        TextView nav_userEmail = header.findViewById(R.id.nav_userEmail);

        nav_Username.setText(currentUser.getDisplayName());
        nav_userEmail.setText(currentUser.getEmail());

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout ,toolbar , R.string.navigation_drawer_close , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.setting) {
                    Toast.makeText(TeacherActivity2.this, "Currently no settings are available", Toast.LENGTH_LONG).show();
                } else if (itemId == R.id.SignOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(TeacherActivity2.this, LoginActivity.class));
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        setUpRecyclerAdapter();
    }
    private void setUpRecyclerAdapter() {
        Query query = db.collection("Requests").orderBy("createdAt");

        FirestoreRecyclerOptions<RequestInfo> options = new FirestoreRecyclerOptions.Builder<RequestInfo>()
                .setQuery(query , RequestInfo.class)
                .build();

        adapter = new T_RequestAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.T_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemCLickListener(new T_RequestAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String RequestId = documentSnapshot.getId();
                Intent intent = new Intent(TeacherActivity2.this , TeacherActivity3.class);
                intent.putExtra("T_RequestId" , RequestId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(999);
    }
}