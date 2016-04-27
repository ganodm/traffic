package com.brkc.traffic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.brkc.traffic.ui.LoginActivity;
import com.brkc.traffic.ui.MainActivity;
import com.brkc.traffic.ui.VehicleQueryActivity;
import com.brkc.traffic.ui.VehicleQueryResultActivity;
import com.brkc.common.util.AndroidUtil;
import com.brkc.traffic.ui.image.ImageGridActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void showLoginActivity(View view){
        startActivity(AndroidUtil.createIntent(this, LoginActivity.class));
    }

    public void showMainActivity(View view){
        startActivity(AndroidUtil.createIntent(this, MainActivity.class));
    }

    public void showVehicleQueryActivity(View view){
        startActivity(AndroidUtil.createIntent(this, VehicleQueryActivity.class));
    }

    public void showVehicleQueryResultActivity(View view){
        Intent intent = AndroidUtil.createIntent(this, VehicleQueryResultActivity.class);
        intent.putExtra(VehicleQueryActivity.EXTRA_RESULT_NAME,VehicleQueryResult.result4Extra);
        startActivity(intent);
    }

    public void showGridViewActivity(View view){
        Intent intent = AndroidUtil.createIntent(this, ImageGridActivity.class);
        intent.putExtra(VehicleQueryActivity.EXTRA_RESULT_NAME,VehicleQueryResult.result4Extra);
        startActivity(intent);
    }
}
