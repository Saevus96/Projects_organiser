package pl.kpchl.registrationproject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.kpchl.registrationproject.adapters.ViewPagerAdapter;
import pl.kpchl.registrationproject.fragments.authfragments.LoginFragmentIn;
import pl.kpchl.registrationproject.fragments.authfragments.RegisterFragmentIn;

public class HomePageActivity extends AppCompatActivity {

    private ViewPager viewPagerRegLog;
    private LinearLayout linearLayout;
    private TextView[] dots;
    private String data;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getStringExtra("startPage");
        setContentView(R.layout.activity_home_page);
        deleteInfoBar();
        components();

        addDotsIndicator(0);
        setupViewPager(viewPagerRegLog);
        viewPagerRegLog.addOnPageChangeListener(viewListener);
        setupActionBar();


    }

    //delete information bar
    private void deleteInfoBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //Create Custom ActionBar
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Method for back to MainActivity with the same view pager fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Funtction to find components
    private void components() {
        viewPagerRegLog = findViewById(R.id.viewPagerRegLog);
        linearLayout = findViewById(R.id.dotsRegLog);
        toolbar = findViewById(R.id.customActionBar);
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

    // Function to create viewPager
    private void setupViewPager(ViewPager slidingViewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (data.equals("1")) {
            viewPagerAdapter.addFragment(new RegisterFragmentIn());
            viewPagerAdapter.addFragment(new LoginFragmentIn());
        } else if (data.equals("2")) {
            viewPagerAdapter.addFragment(new LoginFragmentIn());
            viewPagerAdapter.addFragment(new RegisterFragmentIn());
        }

        slidingViewPager.setAdapter(viewPagerAdapter);
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
