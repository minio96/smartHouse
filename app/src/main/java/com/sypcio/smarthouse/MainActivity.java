package com.sypcio.smarthouse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sypcio.smarthouse.Fragments.AccountFragment;
import com.sypcio.smarthouse.Fragments.GridFragment;
import com.sypcio.smarthouse.Fragments.DeviceFragment;
import com.sypcio.smarthouse.Fragments.MonitorFragment;

public class MainActivity extends AppCompatActivity  implements  OnBackPressed {

    private final static String MAP_FRAGMENT = "MAP";
    private final static String CONTROL_FRAGMENT = "CONTROL";
    private final static String ALARM_FRAGMENT = "ALARM";
    private final static String ACCOUNT_FRAGMENT = "ACCOUNT";


    private BottomNavigationViewEx bottomNavigationViewEx ;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_monitor:
                    for (int backStack = getSupportFragmentManager().getBackStackEntryCount(); backStack > 0; backStack--) {
                        getSupportFragmentManager().popBackStack();
                    }
                    return true;

                case R.id.navigation_control:
                    switchToFragment(new DeviceFragment(), CONTROL_FRAGMENT);

                    return true;

                case R.id.navigation_alarm:
                    switchToFragment(new GridFragment(), ALARM_FRAGMENT);
                    return true;

                case R.id.navigation_account:
                    switchToFragment(new AccountFragment(), ACCOUNT_FRAGMENT);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationViewEx = this.findViewById(R.id.bnve);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, MonitorFragment.newInstance(), MAP_FRAGMENT)
                    .commit();
        }
        bottomNavigationViewEx.enableAnimation(false)
                .setTextVisibility(false)
                .setIconsMarginTop(10)
                .enableShiftingMode(false)
                .setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void switchToFragment(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("DEVICE");
        int backStack;
        if(fragment != null && fragment.isVisible()){
            super.onBackPressed();
        } else if(getSupportFragmentManager().getBackStackEntryCount()>=1) {
            for (backStack = getSupportFragmentManager().getBackStackEntryCount(); backStack > 0; backStack--) {
                getSupportFragmentManager().popBackStack();
            }
            bottomNavigationViewEx.setCurrentItem(0);
        }
        else{
            super.onBackPressed();
        }
    }
}
