package com.sketch.securityowner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sketch.securityowner.R;


public class LoaderDialog extends Dialog {

    private Context context;
    private TextView tv_msg;
    private boolean showMsg;
    private String msgText;


    // (this, android.R.style.Theme_Translucent, show message, message text)
    public LoaderDialog(@NonNull Context context, int themeResId,
                        boolean showMsg, String msgText) {
        super(context, themeResId);
        this.context = context;
        this.showMsg = showMsg;
        this.msgText = msgText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.loader_dialog);
        setCancelable(false);

        tv_msg = findViewById(R.id.tv_msg);

        if (showMsg){
            tv_msg.setVisibility(View.VISIBLE);
        }else {
            tv_msg.setVisibility(View.GONE);
        }

        tv_msg.setText(msgText);


    }


    @Override
    public void show() {
        threadStart();
        super.show();
    }

    @Override
    public void dismiss() {

        try {
            super.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void threadStart(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isShowing()){

                    dismiss();
                }

            }
        }, 30*1000);

    }


}
