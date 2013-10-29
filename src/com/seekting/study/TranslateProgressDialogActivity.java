
package com.seekting.study;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.seekting.study.view.ProgressDialog;

/**
 * 透明dialog进度条
 * 
 * @author seekting
 */
public class TranslateProgressDialogActivity extends BaseActivity {

    public TranslateProgressDialogActivity() {
        name = "透明dialog进度条";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        // setContentView(button);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.progress_dialog);
        findViewById(R.id.loadingbar).setLayerType(View.LAYER_TYPE_HARDWARE, null);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(
                        TranslateProgressDialogActivity.this, R.style.transparentDialog);
                progressDialog.show();
            }
        });

    }
}
