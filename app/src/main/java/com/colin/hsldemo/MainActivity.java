package com.colin.hsldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements HSLView.ProgressMappingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HSLView hslView = findViewById(R.id.test_hsl);
        hslView.setColorData(HSLUtil.result);
        hslView.setMappingListener(this);
    }

    @Override
    public void onHSLHueProgressChanged(String name, int progress, boolean fromUser) {

    }

    @Override
    public void onHSLSatProgressChanged(String name, int progress, boolean fromUser) {

    }

    @Override
    public void onHSLLigProgressChanged(String name, int progress, boolean fromUser) {

    }

    @Override
    public int onHSLMapping(int seekBarProgress) {
        return seekBarProgress - 100;
    }

    @Override
    public int onHSLMappingRevert(int hslProgress) {
        return hslProgress + 100;
    }
}