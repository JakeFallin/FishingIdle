package com.jakefallin.fishingidle.upgrades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.adapters.UpgradesAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/9/2016.
 */


public class RodUpgrades extends ListFragment {
    int mNum;

    ArrayList<Upgrade> shaft;
    ArrayList<Upgrade> reel;
    ArrayList<Upgrade> line;
    ArrayList<ArrayList<Upgrade>> upgrades;
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    @BindView(R.id.tvRodMoney) TextView tvMoney;
    double money;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static RodUpgrades newInstance(int num) {
        RodUpgrades f = new RodUpgrades();

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }


    @Override
    public void onResume() {
        super.onResume();

        getData();

    }


    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upgrade_item_fragment, container, false);
        ButterKnife.bind(this, v);

        upgrades = new ArrayList<>();
        reel = new ArrayList<>();
        shaft = new ArrayList<>();
        line = new ArrayList<>();
        populate();
        getData();


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UpgradesAdapter upgradesAdapter = new UpgradesAdapter(getActivity(), reel);
        setListAdapter(upgradesAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }


    public void populate() {

        //reel
        reel.add(new Upgrade("Crank", 10.0, Upgrade.Category.reel, false));
        reel.add(new Upgrade("Pulley", 25.0, Upgrade.Category.reel, false));
        //line
        line.add(new Upgrade("Floss", 10.0, Upgrade.Category.line, false));
        line.add(new Upgrade("String", 25.0, Upgrade.Category.line, false));
        //shaft
        shaft.add(new Upgrade("Stick", 10.0, Upgrade.Category.shaft, false));
        shaft.add(new Upgrade("Basic Rod", 25.0, Upgrade.Category.shaft, false));

        upgrades.add(reel);
        upgrades.add(line);
        upgrades.add(shaft);


    }

    public void getData() {

        preferences = this.getActivity().getSharedPreferences("money", Context.MODE_PRIVATE);
        money = getDouble(preferences, "money", 1.0);
        tvMoney.setText("$" + money);

    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

}