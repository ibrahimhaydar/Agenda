package com.mobiweb.ibrahim.agenda.Custom;

import android.content.Context;
import android.util.AttributeSet;

import com.mobiweb.ibrahim.agenda.utils.AppHelper;

/**
 * Created by Ali Hariri on 5/22/2017.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode())
            setTypeface(AppHelper.getTypeFace(getContext()));
    }
}
