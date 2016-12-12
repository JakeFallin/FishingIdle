package com.jakefallin.fishingidle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakefallin.fishingidle.upgrades.Upgrade;
import com.jakefallin.fishingidle.upgrades.UpgradesFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    double money = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fragment f = new FishingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, f).commit();

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        setupDrawerContent(navigationView);

        dataSetup();


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.fishingDrawer:
                fragmentClass = FishingFragment.class;
                break;
            case R.id.upgradesDrawer:
                fragmentClass = UpgradesFragment.class;
                break;
            case R.id.locationsDrawer:
                fragmentClass = LocationsFragment.class;
                break;
            case R.id.settingDrawer:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = FishingFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }


    public void dataSetup() {

        SharedPreferences preferences = this.getSharedPreferences("fishing", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        Gson gson = new Gson();
        boolean hasRun = preferences.getBoolean("firstTime", false);


        if (!hasRun) {

            double money = 0.00;
            putDouble(preferencesEditor, "money", money);

            ArrayList<Upgrade> rodUpgrades = new ArrayList<>();

            //reel
            rodUpgrades.add(new Upgrade("Reel", 10.0, Upgrade.Category.reel, false, 0));
            rodUpgrades.add(new Upgrade("Line", 10.0, Upgrade.Category.reel, false, 0));
            rodUpgrades.add(new Upgrade("Shaft", 10.0, Upgrade.Category.line, false, 0));

            ArrayList<Integer> rodUpgradeLevels = new ArrayList<>();

            rodUpgradeLevels.add(0);
            rodUpgradeLevels.add(0);
            rodUpgradeLevels.add(0);

            String json = gson.toJson(rodUpgrades);
            preferencesEditor.putString("Rod", json);
            json = gson.toJson(rodUpgradeLevels);
            preferencesEditor.putString("UpgradeLevels", json);
            preferencesEditor.apply();
        }
    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

}