package com.pitaya.bookingnow.app;

import com.pitaya.bookingnow.app.views.OrderDetailFragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class OrderDetailActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
        	Fragment details = new OrderDetailFragment();
            details.setArguments(getIntent().getExtras());
            this.getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
