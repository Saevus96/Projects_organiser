package pl.kpchl.registrationproject.useractivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.ProjectClass;

public class CreateProjectActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar appBar;
    private Spinner categorySpinner;
    private ArrayList<String> catergoryArrayList;
    private DatabaseReference mDatabase;
    private ArrayAdapter categoryAdapter;
    private String categoryName;
    private TextInputLayout projectName;
    private TextInputLayout projectOrganisation;
    private TextInputLayout projectCustomers;
    private TextInputLayout projectDescription;
    private FirebaseAuth mAuth;
    private String currentUser;
    private Button addProject;
    private String spinnerActiveItem;
    private int spinnerActivePosition;
    private String name;
    private String organisation;
    private String customers;
    private String description;
    private RelativeLayout layout;
    private ProgressBar progressBar;
    private ArrayList<String> projectArray;
    private int arraySize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        deleteInfoBar();
        createToast("Remember you can add only three projects", R.drawable.ic_alert);
        components();
        setupActionBar();
        setupDatabase();
        setupSpinner();
        countProjects();
        catergoryArrayList = new ArrayList<>();
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void countProjects() {
        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String projectAdmin = dataSnapshot.child("projectAdmin").getValue(String.class);
                if (projectAdmin.equals(getUser())) {
                    arraySize++;
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

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Create new Project");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //setup components
    private void components() {
        appBar = findViewById(R.id.appBar);
        categorySpinner = findViewById(R.id.projectCategorySpinner);
        projectName = findViewById(R.id.projectNameET);
        projectCustomers = findViewById(R.id.projectCustomersET);
        projectDescription = findViewById(R.id.projectDescriptionET);
        projectOrganisation = findViewById(R.id.projectOrganisationET);
        addProject = findViewById(R.id.addProjectButton);
        addProject.setOnClickListener(this);
        layout = findViewById(R.id.relativeLay);
    }

    private boolean getAndValidate() {
        name = projectName.getEditText().getText().toString();
        description = projectDescription.getEditText().getText().toString();
        organisation = projectOrganisation.getEditText().getText().toString();
        customers = projectCustomers.getEditText().getText().toString();

        boolean validator = true;

        if (name.length() < 6) {
            projectName.setError("To short project name, Must be 6 characters");
            validator = false;
        }
        if (description.length() < 6) {
            projectDescription.setError("To short description");
            validator = false;
        }
        if (organisation.length() < 6) {
            projectOrganisation.setError("To short organisation name");
            validator = false;
        }
        if (customers.length() < 6) {
            projectCustomers.setError("To short customers list");
            validator = false;
        }

        return validator;
    }

    //create custom toast
    public void createToast(String text, int image) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        ImageView toastImage = layout.findViewById(R.id.toastImage);
        toastImage.setImageResource(image);

        TextView toastText = layout.findViewById(R.id.toastText);
        toastText.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Get iformation from firebase about categories
    private void setupSpinner() {
        mDatabase.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                categoryName = dataSnapshot.getValue(String.class);
                catergoryArrayList.add(categoryName);
                categoryAdapter = new ArrayAdapter<>(CreateProjectActivity.this, android.R.layout.simple_list_item_1, catergoryArrayList);
                categorySpinner.setAdapter(categoryAdapter);
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
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                spinnerActivePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //Add data to database
    private void addToDatabase() {
        if (!getAndValidate()) {
            return;
        } else {
            progressBar();
            String id = mDatabase.push().getKey();
            mDatabase.child("projects").child(id).setValue(new ProjectClass(name,
                    "Category" + spinnerActivePosition,
                    description, getUser(), organisation, customers));
            finish();
        }
    }

    //Progress bar funtion to show and hide progress bar
    public void progressBar() {
        progressBar = new ProgressBar(CreateProjectActivity.this, null, android.R.attr.progressBarStyleLargeInverse);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProjectButton:
                if (arraySize < 3) {
                    addToDatabase();
                } else {
                    createToast("You already have three active projects", R.drawable.ic_close);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
