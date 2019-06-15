package com.mobiweb.ibrahim.agenda.Custom;

import android.content.Context;
import android.util.AttributeSet;

import com.mobiweb.ibrahim.agenda.utils.AppHelper;


public class CustomTextViewBoldAr extends android.support.v7.widget.AppCompatTextView {
    public CustomTextViewBoldAr(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewBoldAr(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBoldAr(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode())
            setTypeface(AppHelper.getTypeFaceBoldAr(getContext()));
    }
}
