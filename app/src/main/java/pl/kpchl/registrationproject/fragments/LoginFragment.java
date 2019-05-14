package pl.kpchl.registrationproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pl.kpchl.registrationproject.HomePageActivity;
import pl.kpchl.registrationproject.R;

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private TextView loginText;
    private ImageView loginImage;
    private TextView signInText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        compontents(view);
        animation();
        return view;
    }
    //Components animations
    private void animation() {
        Animation animBounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_anim);
        Animation animFromUp = AnimationUtils.loadAnimation(getContext(), R.anim.from_up);

        loginText.startAnimation(animFromUp);
        loginImage.startAnimation(animFromUp);
        signInText.startAnimation(animFromUp);
        signInText.startAnimation(animBounce);
    }
    // Function to find components in activity_main
    private void compontents(View v) {
        signInText = v.findViewById(R.id.showLogin);
        loginImage = v.findViewById(R.id.loginImage);
        loginText = v.findViewById(R.id.loginText);
        signInText.setOnClickListener(this);
    }

    //Method to find clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showLogin:
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.putExtra("startPage", "2");
                startActivity(intent);
                break;
        }
    }
}
