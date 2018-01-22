package com.wanbox.bxweb.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView textView = new TextView(this);
        textView.setText("Hello World！");
        textView.setTextColor(Color.RED);
        setContentView(textView);
    }

}
