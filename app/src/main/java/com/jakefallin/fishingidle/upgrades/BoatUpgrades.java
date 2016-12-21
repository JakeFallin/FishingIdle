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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.TinyDB;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/9/2016.
 */


public class BoatUpgrades extends ListFragment {
    int mNum;

    ArrayList<Upgrade> boat;
    @BindView(R.id.tvRodMoney)
    TextView tvMoney;
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
        TinyDB tinyDB = new TinyDB(getContext());

        getData();
        boat = new ArrayList<>();
        boat = tinyDB.getListObject("Boat", Upgrade.class);
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
        TinyDB tinyDB = new TinyDB(getContext());

        boat = new ArrayList<>();
        boat = tinyDB.getListObject("Boat", Upgrade.class);
        getData();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UpgradesAdapter upgradesAdapter = new UpgradesAdapter(getActivity(), boat);
        setListAdapter(upgradesAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }

    public void getData() {

        TinyDB tinyDB = new TinyDB(getContext());
        money = tinyDB.getDouble("money", 0.0);
        tvMoney.setText("$" + money);

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
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<Upgrade> upgrades = tinyDB.getListObject("Boat", Upgrade.class);

            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.upgrade_item, parent, false);
                viewHolder.upgradeName = (TextView) convertView.findViewById(R.id.tvUpgradeName);
                viewHolder.upgradeCost = (TextView) convertView.findViewById(R.id.tvUpgradeCost);
                viewHolder.upgradeButton = (Button) convertView.findViewById(R.id.buttonUpgrade);
                viewHolder.up = user;
                viewHolder.upgradeButton.setText("Level " + upgrades.get(position).getLevel());
                viewHolder.upgradeCost.setText("$" + upgrades.get(position).getCost());
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.upgradeButton.setTag(position);
            viewHolder.upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = (Integer) view.getTag();
                    SharedPreferences preferences = getContext().getSharedPreferences("fishing", Context.MODE_PRIVATE);
                    Gson gson = new Gson();

                    TinyDB tinyDB = new TinyDB(getContext());
                    ArrayList<Upgrade> upgrades = tinyDB.getListObject("Boat", Upgrade.class);
                    int level = tinyDB.getInt("Level");
                    double money = tinyDB.getDouble("money", 1.0);

                    if(money >= upgrades.get(pos).getCost()) {
                        money -= upgrades.get(pos).getCost();
                        money = Math.round(money * 100.0) / 100.0;
                        tvMoney.setText("$ " + money);

                        upgrades.get(pos).getCost();
                        upgrades.get(pos).incrementLevel();
                        viewHolder.upgradeButton.setText("Level " + upgrades.get(pos).getLevel());
                        viewHolder.upgradeCost.setText("$" + upgrades.get(pos).getCost());
                        tinyDB.putInt("Level", level);
                        tinyDB.putListObject("Boat", upgrades);
                        tinyDB.putDouble("money", money);

                    }
                }
            });

            notifyDataSetChanged();
            getContext();
            viewHolder.upgradeCost.setText("$" + user.getCost());
            viewHolder.upgradeName.setText(user.getName());

            return convertView;
        }


        private class ViewHolder {
            TextView upgradeName;
            TextView upgradeCost;
            Button upgradeButton;
            Upgrade up;
        }
    }
}