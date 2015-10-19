package com.hgsoft.mylibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity 基础类型
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract String getTag(int resId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
