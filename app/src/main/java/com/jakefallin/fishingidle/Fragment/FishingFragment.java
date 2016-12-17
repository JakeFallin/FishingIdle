package com.jakefallin.fishingidle.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakefallin.fishingidle.Fish;
import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.TinyDB;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JakeFallin on 12/8/2016.
 */

public class FishingFragment extends Fragment {

    @BindView(R.id.button)
    Button btnStartProgress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    double money;
    BigDecimal cash;
    TinyDB tinyDB;
    ArrayList<Fish> fishing;


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
        fishing = new ArrayList<>();

        return view;

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        progressBar.setProgress(0);
        addListenerOnButton();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fishing.size() > 0) {

                    openFishingResults();


                }
            }
        });
    }

    public void addListenerOnButton() {

        btnStartProgress.setOnClickListener(
                new View.OnClickListener()  {

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
                                            btnStartProgress.setEnabled(false);
                                            btnStartProgress.setClickable(false);

                                            if (progressBarStatus == 1000) {
                                                fish();
                                                progressBarStatus = 1001;
                                            }
                                            if (progressBarStatus == 1001) {
                                                textView.setText("Click to view Results");
                                                progressBar.setProgress(0);
                                                btnStartProgress.setEnabled(true);
                                                btnStartProgress.setClickable(true);
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

        double d = 0.0;
        fishing = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            Fish f = new Fish(tinyDB);
            fishing.add(f);
            d += f.getValue();
        }

        money += d;
        money = Math.round(money * 100.0) / 100.0;

        tvMoney.setText("$" + money);

        tinyDB = new TinyDB(getContext());
        tinyDB.putDouble("money", money);


    }

    public void openFishingResults() {

        DialogAdapter adapter = new DialogAdapter(getContext(), fishing);

        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setAdapter(adapter)
                .setContentHolder(new ListHolder())
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .setPadding(10, 10, 10, 10)
                .setContentHeight(1200)  // or any custom width ie: 300

                .setHeader(R.layout.fish_item_header)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();


    }

    public class DialogAdapter extends ArrayAdapter<Fish> {

        public DialogAdapter(Context context, ArrayList<Fish> fish) {
            super(context, 0, fish);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            Fish fish = getItem(position);
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fish_item, parent, false);
                viewHolder.species = (TextView) convertView.findViewById(R.id.dialogSpecies);
                viewHolder.size = (TextView) convertView.findViewById(R.id.dialogSize);
                viewHolder.rarity = (TextView) convertView.findViewById(R.id.dialogRarity);
                viewHolder.value = (TextView) convertView.findViewById(R.id.dialogValue);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.species.setText(fish.getSpecies());
            viewHolder.size.setText(fish.getSize());
            viewHolder.rarity.setText(fish.getRarity());
            viewHolder.value.setText("$" + fish.getValue());
            return convertView;

        }

        private class ViewHolder {
            TextView species;
            TextView size;
            TextView rarity;
            TextView value;
        }

    }

}
