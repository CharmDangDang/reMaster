package lab.bandm.puzzletalk.ui;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import lab.bandm.puzzletalk.R;

public class CustomActionBar  {
    private Activity activity;
    private ActionBar actionBar;

    public CustomActionBar(Activity activity, ActionBar actionBar) {

        this.activity = activity;
        this.actionBar = actionBar;
    }

    public void setActionBar() {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        View mCustomView = LayoutInflater.from(activity).inflate(R.layout.appbar,null);
        actionBar.setCustomView(mCustomView);

    }
}
