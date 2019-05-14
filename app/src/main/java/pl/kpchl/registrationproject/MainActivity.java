package pl.kpchl.registrationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.kpchl.registrationproject.adapters.ViewPagerAdapter;
import pl.kpchl.registrationproject.fragments.DescriptionFragment;
import pl.kpchl.registrationproject.fragments.LoginFragment;
import pl.kpchl.registrationproject.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager slidingViewPager;
    private LinearLayout linearLayout;
    private TextView[] dots;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteInfoBar();
        compontents();
        addDotsIndicator(0);
        setupViewPager(slidingViewPager);

    }
    //delete information bar
    private void deleteInfoBar(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }
    // Function to find components in activity_main
    private void compontents() {
        slidingViewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.dots);
        slidingViewPager.addOnPageChangeListener(viewListener);
    }


    // Function to create viewPager
    private void setupViewPager(ViewPager slidingViewPager) {
        slidingViewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new DescriptionFragment());
        viewPagerAdapter.addFragment(new RegisterFragment());
        viewPagerAdapter.addFragment(new LoginFragment());

        slidingViewPager.setAdapter(viewPagerAdapter);
    }

    // Function to create dots navigation
    public void addDotsIndicator(int position) {
        dots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            linearLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    //ViewPagerListener
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    //Method for logged user
    private void updateUI() {
        startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
        finish();
    }
}
