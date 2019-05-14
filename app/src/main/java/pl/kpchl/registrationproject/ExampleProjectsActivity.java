package pl.kpchl.registrationproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import pl.kpchl.registrationproject.adapters.ProjectListAdapter;
import pl.kpchl.registrationproject.models.ProjectClass;

public class ExampleProjectsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ProjectClass> projectArray;
    private ProjectListAdapter projectListAdapter;
    private Toolbar appBar;
    private DatabaseReference mDatabase;
    private EditText search;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_projects);
        deleteInfoBar();
        compontents();
        setupActionBar();
        setupDatabase();
        readDataFromDatabase();
        search();


    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void search() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    projectArray.clear();
                    readSearchingDataFromDatabase(s.toString());
                    Toast.makeText(ExampleProjectsActivity.this, category, Toast.LENGTH_SHORT).show();
                } else {
                    projectArray.clear();
                    readDataFromDatabase();
                }
            }
        });
    }


    //setup firebase database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void readCategories(final String searchString) {
        mDatabase.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue(String.class).toLowerCase().contains(searchString.toLowerCase())) {
                    //String category = dataSnapshot.getValue(String.class);
                    category = dataSnapshot.getKey().toString();
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

    private void readSearchingDataFromDatabase(final String searchString) {
        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("projectName").getValue(String.class).toLowerCase().contains(searchString.toLowerCase())) {
                    projectArray.add(dataSnapshot.getValue(ProjectClass.class));

                } else if (dataSnapshot.child("projectOrganisation").getValue(String.class).toLowerCase().contains(searchString.toLowerCase())) {
                    projectArray.add(dataSnapshot.getValue(ProjectClass.class));
                } else if (dataSnapshot.child("projectCategory").getValue(String.class).equals(category)) {
                    projectArray.add(dataSnapshot.getValue(ProjectClass.class));
                }
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

    private void readDataFromDatabase() {

        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                projectArray.add(dataSnapshot.getValue(ProjectClass.class));
                Collections.shuffle(projectArray);
                setupRecyclerView();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                projectArray.add(dataSnapshot.getValue(ProjectClass.class));
                Collections.shuffle(projectArray);
                setupRecyclerView();
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

    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Take a look at projects");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Configuration recycler view
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectListAdapter = new ProjectListAdapter(this, projectArray,null, 1);
        recyclerView.setAdapter(projectListAdapter);
    }

    //setup components
    private void compontents() {
        recyclerView = findViewById(R.id.recyclerViewProjects);
        appBar = findViewById(R.id.appSearchBar);
        search = findViewById(R.id.search);
        projectArray = new ArrayList<>();

    }


}
