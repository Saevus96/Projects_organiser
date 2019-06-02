package pl.kpchl.registrationproject.underactivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.ViewPagerAdapter;
import pl.kpchl.registrationproject.fragments.MainMenu1Fragment;
import pl.kpchl.registrationproject.fragments.MainMenu2Fragment;
import pl.kpchl.registrationproject.fragments.groupfragments.GroupRequestsFragment;
import pl.kpchl.registrationproject.fragments.groupfragments.GroupUsersFragment;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectGroupsFragment;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectInfoFragment;
import pl.kpchl.registrationproject.fragments.projectfragments.ProjectTasksFragment;

public class GroupManagmentActivity extends AppCompatActivity {
    public static String groupId;
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static DatabaseReference mDatabase;
    private String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_managment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString("groupId");
        }
        components();
        deleteInfoBar();
        setupDatabase();
        setupActionBar();
        setupViewPager();
        tabOrientation();
    }

    //Tab items text
    private void tabOrientation() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("GROUP USERS");
        tabLayout.getTabAt(1).setText("GROUP REQUEST");
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // Function to create viewPager
    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new GroupUsersFragment());
        viewPagerAdapter.addFragment(new GroupRequestsFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }
    private void components() {
        toolbar = findViewById(R.id.appBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        mDatabase.child("teams").child(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupName = dataSnapshot.child("groupName").getValue(String.class);
                getSupportActionBar().setTitle(groupName);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
