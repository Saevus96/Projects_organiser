package pl.kpchl.registrationproject.useractivities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.GroupClass;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar appBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;

    private Button addGroupButton;
    private TextInputLayout groupName;
    private TextInputLayout groupSpeciality;
    private String name;
    private String speciality;

    private ProgressBar progressBar;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        deleteInfoBar();
        components();
        setupActionBar();
        setupDatabase();
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Create new Group");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //setup components
    private void components() {
        appBar = findViewById(R.id.appBar);
        groupName = findViewById(R.id.groupNameET);
        groupSpeciality = findViewById(R.id.groupSpecialityET);
        layout = findViewById(R.id.relativeLay);
        addGroupButton = findViewById(R.id.addGroupButton);
        addGroupButton.setOnClickListener(this);
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    // validate edit text
    private boolean getAndValidate() {
        name = groupName.getEditText().getText().toString();
        speciality = groupSpeciality.getEditText().getText().toString();

        boolean validator = true;

        if (name.length() < 6) {
            groupName.setError("To short project name, Must be 6 characters");
            validator = false;
        }
        if (speciality.length() < 6) {
            groupSpeciality.setError("To short speciality description ");
            validator = false;
        }

        return validator;
    }

    //setup Progress Bar
    public void progressBar() {
        progressBar = new ProgressBar(CreateGroupActivity.this, null, android.R.attr.progressBarStyleLargeInverse);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
    }

    //Add data to database
    private void addToDatabase() {
        if (!getAndValidate()) {
            return;
        } else {
            progressBar();
            String id = mDatabase.push().getKey();
            //mDatabase.child("projects").child(id).setValue(new ProjectClass(name, "Category" + spinnerActivePosition, description, getUser(), organisation, customers));
            mDatabase.child("teams").child(id).setValue(new GroupClass(name, getUser(), speciality));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addGroupButton:
                addToDatabase();
                break;
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
    }*/

}
