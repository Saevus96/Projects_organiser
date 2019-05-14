package pl.kpchl.registrationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import pl.kpchl.registrationproject.adapters.ViewPagerAdapter;
import pl.kpchl.registrationproject.fragments.DescriptionFragment;
import pl.kpchl.registrationproject.fragments.LoginFragment;
import pl.kpchl.registrationproject.fragments.MainMenu1Fragment;
import pl.kpchl.registrationproject.fragments.MainMenu2Fragment;
import pl.kpchl.registrationproject.fragments.RegisterFragment;
import pl.kpchl.registrationproject.useractivities.CreateProjectActivity;
import pl.kpchl.registrationproject.useractivities.ManageProjectsActivity;

public class MainMenuActivity extends AppCompatActivity { //implements View.OnClickListener {

    private CardView createNewProject;
    private CardView manageYourProjects;
    private CardView searchProjects;
    private CardView checkYourGroups;
    private CardView checkYourApplications;
    private CardView fillYourDetails;
    private CardView signOutButton;

    private Toolbar toolbar;
    private ViewPager menuViewPager;
    private LinearLayout linearLayout;
    private TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        deleteInfoBar();
        components();
        setupActionBar();
        addDotsIndicator(0);
        setupViewPager(menuViewPager);
    }
    //delete information bar
    private void deleteInfoBar(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }
    //setup ActionBar
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User centrum");
    }

    // Function to create viewPager
    private void setupViewPager(ViewPager slidingViewPager) {
        slidingViewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MainMenu1Fragment());
        viewPagerAdapter.addFragment(new MainMenu2Fragment());
        slidingViewPager.setAdapter(viewPagerAdapter);
    }

    // Function to create dots navigation
    public void addDotsIndicator(int position) {
        dots = new TextView[2];
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

    //setup components
    private void components() {
        toolbar = findViewById(R.id.appBar);
        menuViewPager = findViewById(R.id.menuViewPager);
        linearLayout = findViewById(R.id.dots);
        menuViewPager.addOnPageChangeListener(viewListener);

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

}
