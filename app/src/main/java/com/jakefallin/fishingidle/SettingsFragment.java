package com.jakefallin.fishingidle;

/**
 * Created by JakeFallin on 12/8/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        ArrayList<Upgrade> reel = new ArrayList<>();

        reel.add(new Upgrade("Crank", 10.0, Upgrade.Category.reel, false, 0));
        reel.add(new Upgrade("Pulley", 25.0, Upgrade.Category.reel, false, 0));
        double money = 0.00;
        tinyDB.putDouble("money", money);
        tinyDB.putListObject("Rod", reel);

     }
}

