package com.seekting.study.view;

import com.seekting.study.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ProgressDialog extends Dialog {

  

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }
    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog);
    }
}
