package pl.kpchl.registrationproject.useractivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.ExampleProjectsActivity;
import pl.kpchl.registrationproject.MainMenuActivity;
import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.ProjectListAdapter;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.models.ProjectClass;

public class ManageProjectsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ProjectClass> projectArray;
    private ProjectListAdapter projectListAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private Toolbar appBar;
    private ItemTouchHelper itemTouchHelper;
    private ArrayList<String> projectId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_projects);
        deleteInfoBar();
        compontents();
        setupDatabase();
        readDataFromDatabase();
        setupActionBar();
        swipeDelete();

    }
    //delete information bar
    private void deleteInfoBar(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }

    //setup function swipe to delete data from firebase and recyclerView
    private void swipeDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                projectArray.remove(position);
                projectListAdapter.notifyDataSetChanged();
                projectListAdapter.notifyDataSetChanged();
                mDatabase.child("projects").child(projectId.get(position)).removeValue();
                projectId.remove(position);
                if(projectId.size()==0){
                    startActivity(new Intent(ManageProjectsActivity.this, MainMenuActivity.class));
                    finish();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }



    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Your projects");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Configuration recycler view
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectListAdapter = new ProjectListAdapter(this, projectArray,projectId, 2);
        recyclerView.setAdapter(projectListAdapter);
    }

    //setup components
    private void compontents() {
        recyclerView = findViewById(R.id.recyclerViewProjects);
        appBar = findViewById(R.id.appBar);

    }

    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    private void readDataFromDatabase() {
        projectArray = new ArrayList<>();
        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String projectAdmin = dataSnapshot.child("projectAdmin").getValue(String.class);
                if (projectAdmin.equals(getUser())) {
                    projectArray.add(dataSnapshot.getValue(ProjectClass.class));
                    projectId.add(dataSnapshot.getKey());

                    setupRecyclerView();
                }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

}
