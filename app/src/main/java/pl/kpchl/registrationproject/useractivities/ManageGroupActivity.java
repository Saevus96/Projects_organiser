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
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.kpchl.registrationproject.MainMenuActivity;
import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.adapters.GroupListAdapter;
import pl.kpchl.registrationproject.adapters.SwipeToDeleteCallback;
import pl.kpchl.registrationproject.models.GroupClass;

public class ManageGroupActivity extends AppCompatActivity {
    private Toolbar appBar;
    private RecyclerView recyclerView;
    private ArrayList<GroupClass> groupArray;
    private GroupListAdapter groupListAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private ArrayList<String> groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
        deleteInfoBar();
        compontents();
        setupActionBar();
        setupDatabase();
        readDataFromDatabase();
        swipeDelete();
    }

    //delete information bar
    private void deleteInfoBar(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }
    private void swipeDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                groupListAdapter.removeItem(position);
                groupListAdapter.notifyDataSetChanged();
                mDatabase.child("teams").child(groupId.get(position)).removeValue();
                groupId.remove(position);
                if(groupId.size()==0){
                    startActivity(new Intent(ManageGroupActivity.this, MainMenuActivity.class));
                    finish();
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    //read data about group from firebase
    private void readDataFromDatabase() {
        groupArray = new ArrayList<>();
        mDatabase.child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String groupAdmin = dataSnapshot.child("groupAdmin").getValue(String.class);
                if (groupAdmin.equals(getUser())) {
                    groupArray.add(dataSnapshot.getValue(GroupClass.class));
                    //Collections.shuffle(projectArray);
                    setupRecyclerView();
                    groupId.add(dataSnapshot.getKey());
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

    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Your groups");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //setup components
    private void compontents() {
        recyclerView = findViewById(R.id.recyclerViewGroups);
        appBar = findViewById(R.id.appBar);
        groupId = new ArrayList<>();
    }

    //setup Firebase Database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    //Configuration recycler view
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupListAdapter = new GroupListAdapter(this, groupArray);
        recyclerView.setAdapter(groupListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            startActivity(new Intent(this, MainMenuActivity.class));
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainMenuActivity.class));
        this.finish();
    }

}
