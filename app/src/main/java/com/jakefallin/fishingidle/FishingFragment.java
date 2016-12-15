package com.jakefallin.fishingidle;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakefallin.fishingidle.upgrades.Upgrade;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/8/2016.
 */

public class FishingFragment extends Fragment {

    @BindView(R.id.button) Button btnStartProgress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tvMoney) TextView tvMoney;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    double money;
    BigDecimal cash;
    TinyDB tinyDB;


    public static FishingFragment newInstance() {
        FishingFragment fishingFragment = new FishingFragment();
        return fishingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fishing_fragment, parent, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getContext());
        money = tinyDB.getDouble("money", 0.0);
        tvMoney.setText("$" + money);

        return view;

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        progressBar.setProgress(0);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        btnStartProgress.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        progressBarStatus = 0;

                        new Thread(new Runnable() {
                            public void run() {
                                while (progressBarStatus < 1000) {

                                    // process some tasks
                                    progressBarStatus += 100;

                                    // your computer is too fast, sleep 1 second
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // Update the progress bar
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressBarStatus);
                                            textView.setText("Fishing...");
                                            if (progressBarStatus == 1000) {
                                                fish();
                                                progressBarStatus = 1001;
                                            }
                                            if(progressBarStatus == 1001) {
                                                textView.setText("Done Fishing");
                                                progressBar.setProgress(0);
                                            }
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                });

    }

    public void fish() {

        double val = 0;
        double highest = 0;

        for(int i = 0; i < 100; i++)
        {
            double d = new Fish(tinyDB).getValue();
            val += d;

            if(d > highest)
            {
                highest = d;
            }

        }

        System.out.println("Highest" + highest);
        System.out.println("Average" + val / 100.0);

        money += val;
        money = Math.round(money * 100.0) / 100.0;

        tvMoney.setText("$" + money);

        tinyDB = new TinyDB(getContext());
        tinyDB.putDouble("money", money);

    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }



}
