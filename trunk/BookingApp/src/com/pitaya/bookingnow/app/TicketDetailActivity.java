package com.pitaya.bookingnow.app;

import com.pitaya.bookingnow.app.views.TicketDetailFragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class TicketDetailActivity extends FragmentActivity {
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
        	Fragment details = new TicketDetailFragment();
            details.setArguments(getIntent().getExtras());
            this.getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
