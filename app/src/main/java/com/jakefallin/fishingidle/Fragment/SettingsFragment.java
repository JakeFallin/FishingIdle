package com.jakefallin.fishingidle.Fragment;

/**
 * Created by JakeFallin on 12/8/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakefallin.fishingidle.Lure;
import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.TinyDB;
import com.jakefallin.fishingidle.upgrades.Upgrade;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsFragment extends Fragment {

    @BindView(R.id.buttonReset)
    Button reset;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        reset.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        reset();
                    }

                });

        return view;


    }

    public void reset() {

        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Upgrade> rod = new ArrayList<>();
        ArrayList<Upgrade> boat = new ArrayList<>();
        ArrayList<Lure> lure = new ArrayList<>();

        rod.add(new Upgrade("Shaft", 10.0, Upgrade.Category.shaft, false, 0));
        rod.add(new Upgrade("Line", 10.0, Upgrade.Category.line, false, 0));
        rod.add(new Upgrade("Reel", 10.0, Upgrade.Category.reel, false, 0));

        boat.add(new Upgrade("Hull", 1000.0, Upgrade.Category.grip, false, 0));
        boat.add(new Upgrade("Storage", 1000.0, Upgrade.Category.grip, false, 0));
        boat.add(new Upgrade("Fuel", 1000.0, Upgrade.Category.grip, false, 0));

        lure.add(new Lure("Hook", 10.0, 1.0));
        lure.add(new Lure("Double Hook", 25.0, 1.0));
        lure.add(new Lure("Triple Hook", 100.0, 1.0));
        lure.add(new Lure("Shiny Hook", 250.0, 1.0));
        lure.add(new Lure("Worm", 1000.0, 1.0));
        lure.add(new Lure("Nightcrawler", 2500.0, 1.0));
        lure.add(new Lure("Lure", 10000.0, 1.0));

        double money = 0.00;
        int level = 0;
        tinyDB.putListObject("Rod", rod);
        tinyDB.putListObject("Boat", boat);
        tinyDB.putDouble("money", money);
        tinyDB.putInt("Level", level);

     }
}


