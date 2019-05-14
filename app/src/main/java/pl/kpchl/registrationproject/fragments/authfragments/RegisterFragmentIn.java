package pl.kpchl.registrationproject.fragments.authfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pl.kpchl.registrationproject.MainMenuActivity;
import pl.kpchl.registrationproject.R;
import pl.kpchl.registrationproject.fragments.BaseFragment;
import pl.kpchl.registrationproject.fragments.LoginFragment;

public class RegisterFragmentIn extends BaseFragment implements View.OnClickListener {
    private TextInputLayout registerEmail;
    private TextInputLayout registerPassword;
    private TextInputLayout registerPasswordRepeat;
    private Button registerButton;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private RelativeLayout layout;

    // Method to find clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                register();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment_in, container, false);

        compontents(view);
        return view;
    }

    // Function to find components in activity_main
    private void compontents(View v) {
        registerEmail = v.findViewById(R.id.registerEmail);
        registerPassword = v.findViewById(R.id.registerPassword);
        registerPasswordRepeat = v.findViewById(R.id.repeatPassword);
        registerButton = v.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        checkBox = v.findViewById(R.id.termsCheckBox);
        layout = v.findViewById(R.id.relativeLay);
    }

    //Check validation
    private boolean validation() {
        boolean validator = true;

        if (!registerEmail.getEditText().getText().toString().contains("@")) {
            registerEmail.setError("Incorrect E-mail");
            validator = false;
        }
        if (registerEmail.getEditText().getText().toString().isEmpty()) {
            registerEmail.setError("E-mail field is empty");
            validator = false;
        }
        if (registerPassword.getEditText().getText().toString().length() <= 6) {
            registerPassword.setError("Password to short");
            validator = false;
        }
        if (registerPassword.getEditText().getText().toString().isEmpty()) {
            registerPassword.setError("Password cannot be empty");
            validator = false;
        }
        if (!registerPassword.getEditText().getText().toString()
                .equals(registerPasswordRepeat.getEditText().getText().toString())) {
            registerPassword.setError("Passwords are different");
            validator = false;
        }
        if (!registerPasswordRepeat.getEditText().getText().toString()
                .equals(registerPassword.getEditText().getText().toString())) {
            registerPasswordRepeat.setError("Passwords are different");
            validator = false;
        }
        if (!checkBox.isChecked()) {
            Toast.makeText(getActivity(), "CHECK BOX NOT CHECKED", Toast.LENGTH_SHORT).show();
            validator = false;
        }
        return validator;
    }

    //Firebase Register Method
    private void register() {
        if (!validation()) {
            return;
        } else {
            progressBar();
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(registerEmail.getEditText().getText().toString(),
                    registerPassword.getEditText().getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateUI();
                            } else {
                                Toast.makeText(getActivity(), "SIGN UP FAILED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
    //after sign up push user to user panel activity
    private void updateUI() {
        Toast.makeText(getActivity(), "HELLO", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
        getActivity().finish();
    }

    //Progress bar funtion to show and hide progress bar
    public void progressBar() {
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLargeInverse);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);

    }
}
