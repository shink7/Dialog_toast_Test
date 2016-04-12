package com.example.ms.dialog_toast_test;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Sub subclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Activity activity = MainActivity.this;

        subclass = new Sub(this, getApplicationContext(), activity);


        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);

        if (button1 != null) {
            button1.setOnClickListener(button1ClickListener);
        } else { Log.e("ButtonOnClick","NotFound button1"); }
        if (button2 != null) {
            button2.setOnClickListener(button2ClickListener);
        } else { Log.e("ButtonOnClick","NotFound button2"); }
        if (button3 != null) {
            button3.setOnClickListener(button3ClickListener);
        } else { Log.e("ButtonOnClick","NotFound button3"); }

    }


    // AlertDialog呼び出し
    private View.OnClickListener button1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            subclass.createDialog(v.getContext());
        }
    };


    // Toast呼び出し
    private View.OnClickListener button2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            subclass.createToast(v.getContext());
        }
    };


    // PermissionDialog呼び出し
    private View.OnClickListener button3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            subclass.checkPermission(v.getContext());
        }
    };


    @Override
    public void onRequestPermissionsResult(
                       int requestCode
            , @NonNull String[] permissions
            , @NonNull int[] grantResults   ) {

        Log.d("MainActivity", "onRequestStart");
        subclass.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
