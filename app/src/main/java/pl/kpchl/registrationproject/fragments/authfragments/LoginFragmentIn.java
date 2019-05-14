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

public class LoginFragmentIn extends BaseFragment implements View.OnClickListener {
    private TextInputLayout email;
    private TextInputLayout password;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private RelativeLayout layout;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_in, container, false);
        components(view);

        return view;
    }

    //Function to find components
    private void components(View v) {
        email = v.findViewById(R.id.loginEmail);
        password = v.findViewById(R.id.loginPassword);
        loginButton = v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        layout = v.findViewById(R.id.relativeLay);
    }

    // Method to find clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                signIn();
                break;
        }
    }

    //sign in function (FIREBASE)
    private void signIn() {
        progressBar();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString(),
                password.getEditText().getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI();
                        } else {
                            Toast.makeText(getActivity(), "BAD LOGIN OR PASSWORD"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //after success sign in push user to user panel activity
    private void updateUI() {
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
        getActivity().finish();
        progressBar.setVisibility(View.GONE);
    }

    //prograss bar function to setup progressbar when user sign in
    public void progressBar() {
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLargeInverse);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
    }
}
