package pl.kpchl.registrationproject.interfaces;

import android.support.v4.app.Fragment;

//interface to send data between fragments
public interface NavigationListener {
    void changeFragment(Fragment fragment, Boolean addToBackStack);
}