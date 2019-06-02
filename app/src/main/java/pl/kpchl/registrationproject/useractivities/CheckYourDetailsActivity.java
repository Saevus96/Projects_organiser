package pl.kpchl.registrationproject.useractivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.dialogfragments.EditDetailsDialog;
import pl.kpchl.registrationproject.models.UserClass;

public class CheckYourDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUser;
    private TextView firstName;
    private TextView lastName;
    private TextView age;
    private TextView education;
    private TextView experience;
    private TextView organisation;
    private TextView interests;
    private TextView email;
    private TextView phone;
    private ImageView userImage;
    private Button addImageButton;
    private Button addCvButton;
    private android.support.v7.widget.Toolbar toolbar;
    private ArrayList<String> sendToDialog = new ArrayList<>();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri photoPath;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_FILE_REQUEST = 72;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_your_details);
        deleteInfoBar();
        components();
        setupActionBar();
        setupDatabase();
        firebaseStorageSetup();
        fillDetails();
        getImage();

    }

    private void fillDetails() {
        mDatabase.child("users").child(getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);

                firstName.setText(userClass.getFirstName());
                lastName.setText(userClass.getLastName());
                age.setText(userClass.getAge());
                education.setText(userClass.getEducation());
                experience.setText(userClass.getExperience());
                organisation.setText(userClass.getOrganisation());
                interests.setText(userClass.getInterests());
                email.setText(userClass.getEmail());
                phone.setText(userClass.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //setup components
    private void components() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        education = findViewById(R.id.education);
        experience = findViewById(R.id.experience);
        organisation = findViewById(R.id.organisation);
        interests = findViewById(R.id.interests);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        userImage = findViewById(R.id.loadedImage);
        addImageButton = findViewById(R.id.addPhoto);
        addCvButton = findViewById(R.id.addCv);
        toolbar = findViewById(R.id.appBar);
        firstName.setOnClickListener(this);
        lastName.setOnClickListener(this);
        age.setOnClickListener(this);
        education.setOnClickListener(this);
        experience.setOnClickListener(this);
        organisation.setOnClickListener(this);
        interests.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);
        addImageButton.setOnClickListener(this);
        addCvButton.setOnClickListener(this);
    }
    //delete information bar
    private void deleteInfoBar(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }


    //setup firebase storage
    private void firebaseStorageSetup() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    //setup Firebase Database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void getImage() {
        Glide.with(this)
                .load(storageReference.child("user_images").child(getUser()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userImage);
    }

    //Create Custom ActionBar
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Check your details");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstName:
                sendToDialog.add(0, "firstName");
                sendToDialog.add(1, firstName.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.lastName:
                sendToDialog.add(0, "lastName");
                sendToDialog.add(1, lastName.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.age:
                sendToDialog.add(0, "age");
                sendToDialog.add(1, age.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.education:
                sendToDialog.add(0, "education");
                sendToDialog.add(1, education.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.experience:
                sendToDialog.add(0, "experience");
                sendToDialog.add(1, experience.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.organisation:
                sendToDialog.add(0, "organisation");
                sendToDialog.add(1, organisation.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.interests:
                sendToDialog.add(0, "interests");
                sendToDialog.add(1, interests.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.email:
                sendToDialog.add(0, "email");
                sendToDialog.add(1, email.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.phone:
                sendToDialog.add(0, "phone");
                sendToDialog.add(1, phone.getText().toString());
                showDialog(sendToDialog);
                break;
            case R.id.addPhoto:
                chooseImage();
                break;
            case R.id.addCv:
                chooseFile();
                break;
        }
    }

    public void showDialog(ArrayList<String> detailType) {

        EditDetailsDialog editDetailsDialog = new EditDetailsDialog();
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        Bundle args = new Bundle();

        args.putStringArrayList("detailType", detailType);
        args.putString("dataType","userInfo");
        editDetailsDialog.setArguments(args);
        editDetailsDialog.show(fragmentManager, "Dialog");

    }

    //set activity type for files
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select FILE"), PICK_FILE_REQUEST);
    }

    //set activity type for image
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Activity to get data from device storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            photoPath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoPath);
                Glide.with(this)
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(userImage);
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
        }
    }

    //upload file to firebase storage
    private void uploadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading CV file...");
        progressDialog.show();
        if (filePath != null) {

            StorageReference ref = storageReference.child("user_cv/" + getUser());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            createToast("CV file successfully uploaded", R.drawable.ic_alert);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            createToast(e.getMessage(), R.drawable.ic_alert);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

    }

    //upload image to firebase storage
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image file...");
        progressDialog.show();
        if (photoPath != null) {
            progressDialog.show();
            StorageReference ref = storageReference.child("user_images/" + getUser());
            ref.putFile(photoPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            createToast("Image successfully uploaded", R.drawable.ic_alert);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            createToast(e.getMessage(), R.drawable.ic_alert);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
