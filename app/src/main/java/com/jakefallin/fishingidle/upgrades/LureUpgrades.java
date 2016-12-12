package com.jakefallin.fishingidle.upgrades;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakefallin.fishingidle.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/9/2016.
 */


public class LureUpgrades extends ListFragment {
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
    static LureUpgrades newInstance(int num) {
        LureUpgrades f = new LureUpgrades();

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


    public void populate()
    {

        preferences = getActivity().getSharedPreferences("money", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        Gson gson = new Gson();

        boolean hasRun = preferences.getBoolean("firstTime", false);


        if(!hasRun) {
            //reel
            reel.add(new Upgrade("Crank", 10.0, Upgrade.Category.reel, false, 0));
            reel.add(new Upgrade("Pulley", 25.0, Upgrade.Category.reel, false, 0));
            //line
            line.add(new Upgrade("Floss", 10.0, Upgrade.Category.line, false, 0));
            line.add(new Upgrade("String", 25.0, Upgrade.Category.line, false, 0));
            //shaft
            shaft.add(new Upgrade("Stick", 10.0, Upgrade.Category.shaft, false, 0));
            shaft.add(new Upgrade("Basic Rod", 25.0, Upgrade.Category.shaft, false, 0));

            upgrades.add(reel);
            upgrades.add(line);
            upgrades.add(shaft);

            String json = gson.toJson(reel);
            preferencesEditor.putString("Rod", json);
            preferencesEditor.commit();
        }
        else {

            String json = preferences.getString("Rod", "");
            Type type = new TypeToken<ArrayList<Upgrade>>(){}.getType();
            reel = gson.fromJson(json, type);
            upgrades.add(reel);

        }


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


    public class UpgradesAdapter extends ArrayAdapter<Upgrade> {

        SharedPreferences preferences;
        SharedPreferences.Editor preferencesEditor;

        public UpgradesAdapter(Context context, ArrayList<Upgrade> users) {
            super(context, 0, users);
            preferences = context.getSharedPreferences("money", Context.MODE_PRIVATE);
            preferencesEditor = preferences.edit();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final ViewHolder viewHolder;
            Upgrade user = getItem(position);
//        FragmentTransaction transaction = convertView.getFragmentManager().beginTransaction();


            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.upgrade_item, parent, false);
                viewHolder.upgradeName = (TextView) convertView.findViewById(R.id.tvUpgradeName);
                viewHolder.upgradeCost = (TextView) convertView.findViewById(R.id.tvUpgradeCost);
                viewHolder.upgradeButton = (Button) convertView.findViewById(R.id.buttonUpgrade);
                viewHolder.up = user;

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.upgradeButton.setTag(position);
            viewHolder.upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    double money = getDouble(preferences, "money", 1.0);
                    money = Math.round(money * 100.0) / 100.0;
                    money -= viewHolder.up.getCost();
                    putDouble(preferencesEditor, "money", money);
                    preferencesEditor.commit();
                    tvMoney.setText("$" + money);

                }
            });
            notifyDataSetChanged();
            getContext();
            Fragment f = new LureUpgrades();
            viewHolder.upgradeCost.setText("$" + user.getCost());
            viewHolder.upgradeName.setText(user.getName());

            // Lookup view for data population

            // Populate the data into the template view using the data object

            // Return the completed view to render on screen
            return convertView;
        }

        private class ViewHolder {
            TextView upgradeName;
            TextView upgradeCost;
            Button upgradeButton;
            Upgrade up;
        }


        SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
            return edit.putLong(key, Double.doubleToRawLongBits(value));
        }

        double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
            return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
        }
    }
}