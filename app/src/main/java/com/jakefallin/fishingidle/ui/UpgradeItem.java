package com.jakefallin.fishingidle.ui;

import android.widget.Button;
import android.widget.TextView;

import com.jakefallin.fishingidle.R;
import com.jakefallin.fishingidle.upgrades.Upgrade;

import butterknife.BindView;

/**
 * Created by jakefallin on 12/9/16.
 */

public class UpgradeItem {

    Upgrade upgrade;
    double cost;

    public UpgradeItem(Upgrade upgrade) {

        this.upgrade = upgrade;

    }
}
