package com.mobiweb.ibrahim.agenda.Custom;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ibrahim on 4/22/2018.
 */

public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    boolean userSelect = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            // Your selection handling code here
            userSelect = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}