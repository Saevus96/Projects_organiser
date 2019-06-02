package pl.kpchl.registrationproject.useractivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.ProjectListAdapter;
import pl.kpchl.registrationproject.adapters.RequestListAdapter;
import pl.kpchl.registrationproject.models.RequestClass;

public class CheckYourApplicationsActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private RecyclerView recyclerViewRequests;
    private RequestListAdapter requestListAdapter;
    private ArrayList<RequestClass> requestList = new ArrayList<>();
    private ArrayList<String> groupId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_your_applications);
        components();
        setupDatabase();
        setupActionBar();
        deleteInfoBar();
        readRequests();
    }

    private void readRequests() {
        mDatabase.child("users")
                .child(getUser())
                .child("requests")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        RequestClass requestClass = dataSnapshot.getValue(RequestClass.class);
                        requestList.add(requestClass);
                        setupRecyclerView();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your applications");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Funtction to find components
    private void components() {
        toolbar = findViewById(R.id.appBar);
        recyclerViewRequests = findViewById(R.id.recyclerViewRequest);
    }
    //Configuration recycler view
    private void setupRecyclerView() {
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));
        requestListAdapter = new RequestListAdapter(this, requestList);
        recyclerViewRequests.setAdapter(requestListAdapter);

    }
    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
