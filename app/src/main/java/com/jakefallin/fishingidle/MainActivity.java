package com.jakefallin.fishingidle;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jakefallin.fishingidle.Fragment.FishingFragment;
import com.jakefallin.fishingidle.Fragment.LocationsFragment;
import com.jakefallin.fishingidle.Fragment.SettingsFragment;
import com.jakefallin.fishingidle.Fragment.UpgradesFragment;
import com.jakefallin.fishingidle.Fragment.WorkersFragment;
import com.jakefallin.fishingidle.upgrades.Upgrade;

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
            case R.id.workersDrawer:
                fragmentClass = WorkersFragment.class;
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }


    public void dataSetup() {


        TinyDB tinyDB = new TinyDB(this);
        boolean hasRun = tinyDB.getBoolean("firstTime");
        ArrayList<Upgrade> rod = new ArrayList<>();
        ArrayList<Upgrade> boat = new ArrayList<>();
        ArrayList<Lure> lure = new ArrayList<>();
        ArrayList<double[]> cost = new ArrayList<>();

        if (!hasRun) {

            rod.add(new Upgrade("Shaft", 10.00, Upgrade.Category.shaft, false, 0));
            rod.add(new Upgrade("Line", 10.00, Upgrade.Category.line, false, 0));
            rod.add(new Upgrade("Reel", 10.00, Upgrade.Category.reel, false, 0));

            boat.add(new Upgrade("Hull", 100.00, Upgrade.Category.grip, false, 0));
            boat.add(new Upgrade("Storage", 1000.00, Upgrade.Category.grip, false, 0));
            boat.add(new Upgrade("Fuel", 100.00, Upgrade.Category.grip, false, 0));

            lure.add(new Lure("Hook", 10.0, 1.0));
            lure.add(new Lure("Double Hook", 25.0, 1.0));
            lure.add(new Lure("Triple Hook", 100.0, 1.0));
            lure.add(new Lure("Shiny Hook", 250.0, 1.0));
            lure.add(new Lure("Worm", 1000.0, 1.0));
            lure.add(new Lure("Nightcrawler", 2500.0, 1.0));
            lure.add(new Lure("Lure", 10000.0, 1.0));

            double[] c0 = {10.0, 10.0, 10.0};
            cost.add(c0);
            cost.add(c0);
            cost.add(c0);

            int level = 0;
            int upgradeFactor = 1;
            tinyDB.putListObject("Rod", rod);
            tinyDB.putListObject("Boat", boat);
            tinyDB.putListObjectLure("Lure", lure);
            tinyDB.putInt("Factor", upgradeFactor);
            tinyDB.putListObjectDouble("Cost", cost);
            tinyDB.putInt("Level", level);
            tinyDB.putBoolean("firstTime", true);

        }
    }
}