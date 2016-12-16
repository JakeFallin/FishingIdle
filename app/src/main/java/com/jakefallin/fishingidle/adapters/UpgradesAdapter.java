package com.jakefallin.fishingidle.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.upgrades.RodUpgrades;
import com.jakefallin.fishingidle.upgrades.Upgrade;

import java.util.ArrayList;

/**
 * Created by jakefallin on 12/9/16.
 */

public class UpgradesAdapter extends ArrayAdapter<Upgrade> {

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;

    public interface OnUpdateListener {
        void onUpdate(String text);
    }



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
                money -= viewHolder.up.getCost();
                viewHolder.upgradeName.setText("Dick");

                putDouble(preferencesEditor, "money", money);
                preferencesEditor.commit();

            }
        });
        notifyDataSetChanged();
        getContext();
        Fragment f = new RodUpgrades();
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