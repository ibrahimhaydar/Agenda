package com.mobiweb.ibrahim.agenda.Custom;

import android.content.Context;
import android.util.AttributeSet;

import com.mobiweb.ibrahim.agenda.utils.AppHelper;


public class CustomTextViewBold extends android.support.v7.widget.AppCompatTextView {
    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode())
            setTypeface(AppHelper.getTypeFaceBold(getContext()));
    }
}
