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

import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.TinyDB;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/9/2016.
 */

public class RodUpgrades extends ListFragment {
    int mNum;

    ArrayList<Upgrade> reel;
    ArrayList<Upgrade> line;
    ArrayList<ArrayList<Upgrade>> upgrades;
    @BindView(R.id.tvRodMoney)
    TextView tvMoney;
    @BindView(R.id.upgradeX1)
    Button x1;
    @BindView(R.id.upgradeX10)
    Button x10;
    @BindView(R.id.upgradeX100)
    Button x100;
    double money;
    UpgradesAdapter upgradesAdapter;

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
        line = new ArrayList<>();
        reel = tinyDB.getListObject("Rod", Upgrade.class);
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

        upgrades = new ArrayList<>();
        reel = new ArrayList<>();

        reel = tinyDB.getListObject("Rod", Upgrade.class);
        getData();

        x1.setText("Current");
        x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(getContext());
                ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
                ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
                x1.setText("Current");
                x10.setText("X10");
                x100.setText("X100");
                tinyDB.putInt("Factor", 0);
                double[] c = cost.get(0);

                for(int i = 0; i < 1; i++) {
                    c[0] = rod.get(0).getCost();
                    rod.get(0).incrementLevel();
                    c[1] = rod.get(1).getCost();
                    rod. get(1).incrementLevel();
                    c[2] = rod.get(2).getCost();
                    rod.get(2).incrementLevel();
                }

                cost.add(0, c);
                tinyDB.putListObjectDouble("Cost", cost);
                Log.e("A", "" + c[0]);
                Log.e("B", "" + c[1]);
                Log.e("C", "" + c[2]);
                upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
                setListAdapter(upgradesAdapter);
            }
        });
        x10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(getContext());
                ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
                ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
                double[] c = cost.get(1);
                c[0] = rod.get(0).getCost();
                c[1] = rod.get(1).getCost();
                c[2] = rod.get(2).getCost();

                x1.setText("X1");
                x10.setText("Current");
                x100.setText("X100");
                tinyDB.putInt("Factor", 10);
                for(int i = 0; i < 10; i++) {
                    c[0] += rod.get(0).getCost();
                    rod.get(0).incrementLevel();
                    c[1] += rod.get(1).getCost();
                    rod.get(1).incrementLevel();
                    c[2] += rod.get(2).getCost();
                    rod.get(2).incrementLevel();
                }
                cost.add(1, c);
                tinyDB.putListObjectDouble("Cost", cost);
                Log.e("A", "" + c[0]);
                Log.e("B", "" + c[1]);
                Log.e("C", "" + c[2]);
                upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
                setListAdapter(upgradesAdapter);
            }
        });
        x100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(getContext());

                ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
                ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
                double[] c = cost.get(2);

                x1.setText("X1");
                x10.setText("X10");
                x100.setText("Current");
                tinyDB.putInt("Factor", 100);
                c[0] = rod.get(0).getCost();
                c[1] = rod.get(1).getCost();
                c[2] = rod.get(2).getCost();

                for(int i = 0; i < 100; i++) {
                    c[0] += rod.get(0).getCost();
                    rod.get(0).incrementLevel();
                    c[1] += rod.get(1).getCost();
                    rod.get(1).incrementLevel();
                    c[2] += rod.get(2).getCost();
                    rod.get(2).incrementLevel();
                }
                cost.add(2, c);
                tinyDB.putListObjectDouble("Cost", cost);
                Log.e("A", "" + c[0]);
                Log.e("B", "" + c[1]);
                Log.e("C", "" + c[2]);
                upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
                setListAdapter(upgradesAdapter);

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        upgradesAdapter = new UpgradesAdapter(getActivity(), reel);
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
            ArrayList<Upgrade> upgrades = tinyDB.getListObject("Rod", Upgrade.class);
            ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
            int factor = tinyDB.getInt("Factor");
            double[] c = cost.get(0);
            if(factor == 1)
                c = cost.get(0);
            else if(factor == 10)
                c = cost.get(1);
            else if(factor == 100)
                c = cost.get(2);
            
            
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.upgrade_item, parent, false);
                viewHolder.upgradeName = (TextView) convertView.findViewById(R.id.tvUpgradeName);
                viewHolder.upgradeCost = (TextView) convertView.findViewById(R.id.tvUpgradeCost);
                viewHolder.upgradeButton = (Button) convertView.findViewById(R.id.buttonUpgrade);
                viewHolder.up = user;
                viewHolder.upgradeButton.setText("Level " + upgrades.get(position).getLevel());
//                viewHolder.upgradeCost.setText("$" + upgrades.get(position).getCost());
//                viewHolder.upgradeCost.setText("$" + c[position]);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }



            double temp = c[position];
            temp = Math.round(temp * 100.0) / 100.0;
            viewHolder.upgradeCost.setText("$" + temp);
            viewHolder.upgradeButton.setText("Level " + upgrades.get(position).getLevel());


            viewHolder.upgradeButton.setTag(position);
            viewHolder.upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = (Integer) view.getTag();
                    TinyDB tinyDB = new TinyDB(getContext());
                    ArrayList<Upgrade> upgrades = tinyDB.getListObject("Rod", Upgrade.class);
                    ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
                    int level = tinyDB.getInt("Level");
                    double money = tinyDB.getDouble("money", 1.0);
                    int factor = tinyDB.getInt("Factor");
                    double[] c = cost.get(0);
                    if(factor == 1)
                        c = cost.get(0);
                    else if(factor == 10)
                        c = cost.get(1);
                    else if(factor == 100)
                        c = cost.get(2);


                    if (money >= c[pos]) {

                        money -= c[pos];
                        money = Math.round(money * 100.0) / 100.0;
                        tvMoney.setText("$ " + money);
                        upgrades.get(pos).setLevel(upgrades.get(pos).getLevel() + factor);

                        viewHolder.upgradeButton.setText("Level " + upgrades.get(pos).getLevel());

                        upgrades.get(pos).incrementLevel();

                        double temp = c[pos];
                        temp = Math.round(temp * 100.0) / 100.0;
                        Log.e("TTT", "" + temp);

                        viewHolder.upgradeCost.setText("$" + x10(pos));

//                        if(factor == 1)
//                            viewHolder.upgradeCost.setText("$" + x1(pos));
//                        else if(factor == 10)
//                            viewHolder.upgradeCost.setText("$" + x10(pos));
//                        else if(factor == 100)
//                            viewHolder.upgradeCost.setText("$" + x100(pos));

                        tinyDB.putInt("Level", level);
                        tinyDB.putListObject("Rod", upgrades);
                        tinyDB.putDouble("money", money);

                        upgradesAdapter = new UpgradesAdapter(getActivity(), upgrades);
                        setListAdapter(upgradesAdapter);

                    }
                }
            });

            notifyDataSetChanged();
            getContext();
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

    public double x1(int pos) {

        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
        ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
        x1.setText("Current");
        x10.setText("X10");
        x100.setText("X100");
        tinyDB.putInt("Factor", 0);
        double[] c = cost.get(0);

        for(int i = 0; i < 1; i++) {
            c[0] = rod.get(0).getCost();
            rod.get(0).incrementLevel();
            c[1] = rod.get(1).getCost();
            rod. get(1).incrementLevel();
            c[2] = rod.get(2).getCost();
            rod.get(2).incrementLevel();
        }
        upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
        setListAdapter(upgradesAdapter);

        cost.add(0, c);
        tinyDB.putListObjectDouble("Cost", cost);
        if(pos == 1)
            return c[0];
        if(pos == 2)
            return c[1];
        if(pos == 3)
            return c[2];

        return 0;
    }

    public double x10(int pos) {

        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
        ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
        double[] c = cost.get(1);
        c[0] = rod.get(0).getCost();
        c[1] = rod.get(1).getCost();
        c[2] = rod.get(2).getCost();

        x1.setText("X1");
        x10.setText("Current");
        x100.setText("X100");
        tinyDB.putInt("Factor", 10);
        for(int i = 0; i < 10; i++) {
            c[0] += rod.get(0).getCost();
            rod.get(0).incrementLevel();
            c[1] += rod.get(1).getCost();
            rod.get(1).incrementLevel();
            c[2] += rod.get(2).getCost();
            rod.get(2).incrementLevel();
        }
        cost.add(1, c);
        tinyDB.putListObjectDouble("Cost", cost);
        Log.e("A", "" + c[0]);
        Log.e("B", "" + c[1]);
        Log.e("C", "" + c[2]);
        upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
        setListAdapter(upgradesAdapter);

        if(pos == 1)
            return c[0];
        if(pos == 2)
            return c[1];
        if(pos == 3)
            return c[2];

        return 0;

    }
    public double x100(int pos) {
        TinyDB tinyDB = new TinyDB(getContext());

        ArrayList<Upgrade> rod = tinyDB.getListObject("Rod", Upgrade.class);
        ArrayList<double[]> cost = tinyDB.getListObjectDouble("Cost", double[].class);
        double[] c = cost.get(2);

        x1.setText("X1");
        x10.setText("X10");
        x100.setText("Current");
        tinyDB.putInt("Factor", 100);
        c[0] = rod.get(0).getCost();
        c[1] = rod.get(1).getCost();
        c[2] = rod.get(2).getCost();

        for(int i = 0; i < 100; i++) {
            c[0] += rod.get(0).getCost();
            rod.get(0).incrementLevel();
            c[1] += rod.get(1).getCost();
            rod.get(1).incrementLevel();
            c[2] += rod.get(2).getCost();
            rod.get(2).incrementLevel();
        }
        cost.add(2, c);
        tinyDB.putListObjectDouble("Cost", cost);
        Log.e("A", "" + c[0]);
        Log.e("B", "" + c[1]);
        Log.e("C", "" + c[2]);
        upgradesAdapter = new UpgradesAdapter(getActivity(), rod);
        setListAdapter(upgradesAdapter);

        if(pos == 1)
            return c[0];
        if(pos == 2)
            return c[1];
        if(pos == 3)
            return c[2];

        return 0;

    }

}