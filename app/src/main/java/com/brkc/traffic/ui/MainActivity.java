package com.brkc.traffic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.brkc.traffic.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonVehicleQuery = null;
    private Button buttonVehicleCtrl = null;
    private Button buttonVehicleAlarm = null;
    private Button buttonDeviceUpdateXY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls(){
        buttonVehicleQuery = (Button)findViewById(R.id.button_main_vehicle_query);
        buttonVehicleCtrl = (Button)findViewById(R.id.button_main_ctrl_vehicle);
        buttonVehicleAlarm = (Button)findViewById(R.id.button_main_alarm_realtime);
        buttonDeviceUpdateXY = (Button)findViewById(R.id.button_main_device_update_position);
        /*
        ButtonUtil.setButtonStateChangeListener(buttonVehicleQuery);
        ButtonUtil.setButtonStateChangeListener(buttonVehicleCtrl);
        ButtonUtil.setButtonStateChangeListener(buttonVehicleAlarm);
        ButtonUtil.setButtonStateChangeListener(buttonDeviceUpdateXY);
*/
        buttonVehicleQuery.setOnClickListener(this);
        buttonVehicleCtrl.setOnClickListener(this);
        buttonVehicleAlarm.setOnClickListener(this);
        buttonDeviceUpdateXY.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.button_main_vehicle_query:
                openVehicleQuery();
                break;
            case R.id.button_main_ctrl_vehicle:
                openControlVehicle();
                break;
            case R.id.button_main_alarm_realtime:
                openVehicleAlarm();
                break;
            case R.id.button_main_device_update_position:
                openDeviceUpdate();
                break;
            default:
        }
    }

    private void openVehicleQuery() {
        Intent intent = new Intent(MainActivity.this,VehicleQueryActivity.class);
        startActivity(intent);

    }

    private void openControlVehicle() {

    }

    private void openVehicleAlarm() {

    }

    private void openDeviceUpdate() {

    }
}
