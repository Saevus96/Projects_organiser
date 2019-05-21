package pl.kpchl.registrationproject.underactivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.ViewPagerAdapter;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectGroupsFragment;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectInfoFragment;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectTasksFragment;

public class ProjectManagmentActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static String projectId;
    private DatabaseReference mDatabase;
    public String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_managment);
        deleteInfoBar();
        components();
        setProjectId();
        setupDatabase();
        setupActionBar();
        setupViewPager();
        tabOrientation();
    }

    //get project id
    private void setProjectId() {
        projectId = getIntent().getStringExtra("projectId");
    }

    //Tab items text
    private void tabOrientation() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("PROJECT TASKS");
        tabLayout.getTabAt(1).setText("PROJECT GROUPS");
        tabLayout.getTabAt(2).setText("PROJECT INFORMATIONS");
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // Function to create viewPager
    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ProjectTasksFragment());
        viewPagerAdapter.addFragment(new ProjectGroupsFragment());
        viewPagerAdapter.addFragment(new ProjectInfoFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        mDatabase.child("projects").child(projectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectName = dataSnapshot.child("projectName").getValue(String.class);
                // Toast.makeText(ProjectManagmentActivity.this, projectName, Toast.LENGTH_SHORT).show();
                getSupportActionBar().setTitle(projectName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Database reference setup
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void components() {
        toolbar = findViewById(R.id.appBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

}
