package pl.kpchl.registrationproject.useractivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.models.UserClass;

public class FillDetailsActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener, View.OnClickListener {
    private Button choosePhoto, addCV, save;
    private ImageView personImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri photoPath;
    private Uri filePath;
    private FirebaseAuth mAuth;
    private String currentUser;
    private Toolbar appBar;
    private ScrollView scrollView;
    private TextInputLayout firstNameET, lastNameET, ageET, educationET, experienceET, organisationET, interestsET, emailET, phoneET;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_FILE_REQUEST = 72;
    private DatabaseReference mDatabase;
    private String firstName, lastName, age, education, experience, organisation, interests, email, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details);
        deleteInfoBar();
        components();
        setupActionBar();
        setupDatabase();
        firebaseStorageSetup();
    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //validation user details
    private boolean validate() {
        firstName = firstNameET.getEditText().getText().toString();
        lastName = lastNameET.getEditText().getText().toString();
        age = ageET.getEditText().getText().toString();
        education = educationET.getEditText().getText().toString();
        experience = experienceET.getEditText().getText().toString();
        organisation = organisationET.getEditText().getText().toString();
        interests = interestsET.getEditText().getText().toString();
        phone = phoneET.getEditText().getText().toString();
        email = emailET.getEditText().getText().toString();

        boolean validator = true;
        if (firstName.isEmpty()) {
            firstNameET.setError("First name field can not be empty!");
            validator = false;
        }
        if (lastName.isEmpty()) {
            lastNameET.setError("Last name field can not be empty!");
            validator = false;
        }
        if (age.isEmpty()) {
            ageET.setError("Age field can not be empty!");
            validator = false;
        }
        if (education.isEmpty()) {
            educationET.setError("Education field can not be empty!");
            validator = false;
        }
        if (experience.isEmpty()) {
            experienceET.setError("Experience field can not be empty!");
            validator = false;
        }
        if (organisation.isEmpty()) {
            organisationET.setError("Organisation field can not be empty!");
            validator = false;
        }
        if (interests.isEmpty()) {
            interestsET.setError("Interests field can not be empty!");
            validator = false;
        }
        if (phone.isEmpty()) {
            phoneET.setError(("Phone field can not be empty!"));
            validator = false;
        }
        if (email.isEmpty()) {
            emailET.setError("Email field can not be empty!");
            validator = false;
        }
        if (filePath == null) {
            validator = false;
        }
        if (photoPath == null) {
            validator = false;
        }

        return validator;
    }

    //setup firebase database
    private void setupDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //setup components
    private void components() {
        appBar = findViewById(R.id.appBar);
        findViewById(R.id.scrollView).getViewTreeObserver().addOnScrollChangedListener(this);
        firstNameET = findViewById(R.id.firstName);
        lastNameET = findViewById(R.id.lastName);
        ageET = findViewById(R.id.age);
        educationET = findViewById(R.id.education);
        experienceET = findViewById(R.id.experience);
        organisationET = findViewById(R.id.organisation);
        interestsET = findViewById(R.id.interests);
        emailET = findViewById(R.id.email);
        phoneET = findViewById(R.id.phone);
        choosePhoto = findViewById(R.id.addPhoto);
        addCV = findViewById(R.id.addCv);
        save = findViewById(R.id.saveDetails);
        choosePhoto.setOnClickListener(this);
        addCV.setOnClickListener(this);
        save.setOnClickListener(this);
        personImage = findViewById(R.id.personImage);
    }


    //setup action bar
    private void setupActionBar() {
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Fill your details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        .load(storageReference.child("user_images").child(getUser()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(personImage);
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

    //get logged user
    private String getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        return currentUser;
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

    //setup firebase storage
    private void firebaseStorageSetup() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    //scroll status for action bar
    @Override
    public void onScrollChanged() {
        float mFloat = findViewById(R.id.scrollView).getScrollY();
        if (mFloat >= 55.0 && getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        } else if (mFloat == 0 && !getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPhoto:
                chooseImage();
                break;
            case R.id.addCv:
                chooseFile();
                break;
            case R.id.saveDetails:
                sendDetailsToDatabase();
                break;
        }
    }

    private void sendDetailsToDatabase() {
        if (validate() && filePath != null && photoPath != null) {
            mDatabase.child("users").child(getUser()).setValue(new UserClass(firstName, lastName, age, education, experience, organisation, interests, email, phone));
            finish();
        } else {
            createToast("Fill all details", R.drawable.ic_alert);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
