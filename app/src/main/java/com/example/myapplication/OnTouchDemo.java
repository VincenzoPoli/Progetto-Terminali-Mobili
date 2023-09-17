package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class OnTouchDemo extends Activity implements View.OnTouchListener {

    private View viewMousePad;
    private TextView txtCoordinates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewMousePad = (View) findViewById(R.id.viewMousePad);
        txtCoordinates = (TextView) findViewById(R.id.txtCoordinates);

        viewMousePad.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        txtCoordinates.setText("OnTouchDemo -> X==" + String.valueOf(x) + " Y==" + String.valueOf(y));

        return false;
    }
}