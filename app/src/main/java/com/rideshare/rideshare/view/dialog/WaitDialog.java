package com.rideshare.rideshare.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rideshare.rideshare.R;

public class WaitDialog extends Dialog {

    private String waitMsg;

    public WaitDialog(Context context, String waitMsg) {
        super(context);
        this.waitMsg = waitMsg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.fragment_wait_dialog);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void show(){
        super.show();
        TextView waitMsgView = (TextView) this.findViewById(R.id.wait_msg);
        waitMsgView.setText(waitMsg);
    }

    @Override
    public void dismiss(){
        super.dismiss();
    }
}
