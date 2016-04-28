package com.brkc.traffic.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brkc.traffic.R;
import com.brkc.traffic.dialog.CustomChoicePickerDialog;
import com.brkc.traffic.dialog.MultiChoicePickerDialog;
import com.brkc.traffic.dialog.ProvCityDialogFragment;
import com.brkc.traffic.dialog.SingleChoicePickerDialog;
import com.brkc.common.util.DateUtil;
import com.brkc.common.net.HttpUtil;
import com.brkc.common.util.StringUtil;
import com.brkc.traffic.ui.image.ImageListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class VehicleQueryActivity extends AppCompatActivity
        implements View.OnClickListener,View.OnFocusChangeListener,
            ProvCityDialogFragment.ProvCityOnChangeListener,
        CustomChoicePickerDialog{

    private static final String TAG = "BRKC_VehicleQueryActivi";

    private static final int CODE_SHOW_RESULT = 1;
    public static final String EXTRA_RESULT_NAME = "result";
    /***************************************************
     *               开始和结束时间
     ***************************************************/
    DatePickerDialog datePicker = null;
    TimePickerDialog timePicker = null;
    EditText dateTimePickerFrom = null;//可能值是下面四个日期和时间输入框
    EditText textStartDate = null;
    EditText textStartTime = null;
    EditText textEndDate = null;
    EditText textEndTime = null;

    Calendar today = Calendar.getInstance();

    /***************************************************
     *               车牌号
     ***************************************************/
    Button buttonSelectProvCity = null;
    TextView textProv;
    TextView textCity;
    EditText textPlateNO;

    /***************************************************
     *               车牌品牌和车身颜色
     ***************************************************/

    EditText textBodyColor;
    EditText textVehicleBrand;
    EditText textPolice;

    Button buttonVehicleQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_query);

        initUI();
    }
    /**
     * 包括：
     * 1，时间和日期的选择
     * @param v
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.vehicle_query_start_date:
                openDatePickerDialog(textStartDate);
                break;
            case R.id.vehicle_query_end_date:
                openDatePickerDialog(textEndDate);
                break;
            case R.id.vehicle_query_start_time:
                openTimePickerDialog(textStartTime);
                break;
            case R.id.vehicle_query_end_time:
                openTimePickerDialog(textEndTime);
                break;
            case R.id.button_select_prov_city:
                openProvCityPickerDialog();
                break;
            case R.id.vehicle_body_color:
                openVehicleBodyColorPickerDialog(v);
                break;
            case R.id.vehicle_brand:
                openVehicleBrandPickerDialog(v);
                break;
            case R.id.select_police:
                openPoliceMultiPickerDialog(v);
                break;
            case R.id.button_vehicle_query:
                doVehicleQuery();
                break;
            default:
        }
    }

    /**
     * 时间和日期输入框获得焦点的时候，并不触发click事件，
     * 这时候调用onclick里面，打开选择框
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            int id = v.getId();
            switch (id){
                case R.id.vehicle_query_start_date:
                case R.id.vehicle_query_end_date:
                case R.id.vehicle_query_start_time:
                case R.id.vehicle_query_end_time:

                case R.id.vehicle_brand:
                case R.id.vehicle_body_color:

                case R.id.select_police:
                    onClick(v);
                    break;
                default:
            }
        }
    }


    private void initUI(){
        /***************************************************
         *               开始和结束时间
         ***************************************************/
        textStartDate = initEditWithOnClickOnFocus(R.id.vehicle_query_start_date);
        textStartTime = initEditWithOnClickOnFocus(R.id.vehicle_query_start_time);
        textEndDate = initEditWithOnClickOnFocus(R.id.vehicle_query_end_date);
        textEndTime = initEditWithOnClickOnFocus(R.id.vehicle_query_end_time);

        today.add(Calendar.DAY_OF_MONTH, -1);
        initDateTime(today);

        /***************************************************
         *               车牌号
         ***************************************************/
        buttonSelectProvCity = initButtonWithOnClick(R.id.button_select_prov_city);
        textProv = initTextWithDefaultValue(R.id.text_prov,R.string.plate_no_prov_default);
        textCity = initTextWithDefaultValue(R.id.text_city,R.string.plate_no_city_default);
        textPlateNO = (EditText)findViewById(R.id.plate_no);


        /***************************************************
         *               车辆品牌和车身颜色
         ***************************************************/

        textBodyColor = initEditWithOnClickOnFocus(R.id.vehicle_body_color);
        textVehicleBrand = initEditWithOnClickOnFocus(R.id.vehicle_brand);
        textPolice = initEditWithOnClickOnFocus(R.id.select_police);
        buttonVehicleQuery = initButtonWithOnClick(R.id.button_vehicle_query);

        if(!HttpUtil.isNetworkAvailable()){
            buttonVehicleQuery.setEnabled(false);
            HttpUtil.toastNetworkInfo(getActivity());
        }
        else {
            //服务器时间
            String address = getResources().getString(R.string.now_url);
            HttpUtil.get(address, new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.d(TAG, "从服务器返回时间：" + response);
                    today = Calendar.getInstance();
                    today.add(Calendar.DATE, -1);

                    initDateTime(today);
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "时间返回发生错误：" + e.getMessage());
                }
            });
        }

    }

    private EditText initEditWithOnClickOnFocus(int res_id){
        EditText editText = (EditText)findViewById(res_id);
        editText.setOnClickListener(this);
        editText.setOnFocusChangeListener(this);
        return editText;
    }

    private Button initButtonWithOnClick(int res_id){
        Button button = (Button)findViewById(res_id);
        button.setOnClickListener(this);
        return button;
    }
    private TextView initTextWithDefaultValue(int viewId,int res_default_value){
        TextView textView = (TextView)findViewById(viewId);
        String defaultStr = getResources().getString(res_default_value);
        textView.setText(defaultStr);
        return textView;
    }

    /***************************************************
     *               开始和结束时间
     ***************************************************/
    private void initDateTime(Calendar cal){
        String startDate = DateUtil.getDate(cal);

        textStartDate.setText(startDate);
        textEndDate.setText(startDate);
        textStartTime.setText("00:00");
        textEndTime.setText("23:59");
    }


    private void openDatePickerDialog(EditText text){

        if(datePicker == null){
            datePicker = new DatePickerDialog(VehicleQueryActivity.this,
                    new OnMyDateSetListener(),
                    DateUtil.getYear(today),
                    DateUtil.getMonthOfYear(today),
                    DateUtil.getDayOfMonth(today));

            // 避免进入界面就弹出日期选择,第一次只初始化
            return;
        }

        dateTimePickerFrom = text;
        String date = text.getText().toString();
        Calendar cal = DateUtil.parseDate(date);
        datePicker.updateDate(DateUtil.getYear(cal), (DateUtil.getMonthOfYear(cal) - 1),
                DateUtil.getDayOfMonth(cal));

        datePicker.show();
    }

    private void openTimePickerDialog(EditText text){
        if(timePicker == null){
            timePicker = new TimePickerDialog(VehicleQueryActivity.this,
                    new OnMyTimeSetListener(),0,0,true);
        }
        dateTimePickerFrom = text;

        String[] times = text.getText().toString().split(":");
        int hour = StringUtil.parseInteger(times[0]);
        int minutes = StringUtil.parseInteger(times[1]);
        timePicker.updateTime(hour, minutes);
        timePicker.show();
    }

    @Override
    public void onChange(ProvCityDialogFragment dialog,int type, String text) {
        if (type == ProvCityDialogFragment.TYPE_PROV){
            textProv.setText(text);
        }
        else if(type == ProvCityDialogFragment.TYPE_CITY){
            textCity.setText(text);
        }
    }


    @Override
    public void onChange(CustomChoicePickerDialog dialog, int res_code_array, Object names, Object codes) {
        switch (res_code_array){
            case R.array.vehicle_brand:
                textVehicleBrand.setText(names.toString());
                break;
            case R.array.body_color_code:
                String colorName = names.toString();
                List colorList = (List)codes;
                String color = StringUtil.join(colorList, "");
                textBodyColor.setText(colorName.substring(1, colorName.length() - 1));
                textBodyColor.setTag(color);
                break;

            case R.array.police_code:
                String policeName = names.toString();
                List policeList = (List)codes;
                String polices = StringUtil.join(policeList, ",");
                textPolice.setText(policeName.substring(1, policeName.length()-1));
                textPolice.setTag(polices);
                break;
            default:
        }
    }


    private class OnMyDateSetListener implements OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String text  = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            dateTimePickerFrom.setText(text);
        }
    }

    private class OnMyTimeSetListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String text = hourOfDay + ":" + minute;
            dateTimePickerFrom.setText(text);
        }
    }

    /***************************************************
     *               车牌号
     ***************************************************/

    ProvCityDialogFragment provCityDialogFragment;
    private void openProvCityPickerDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if(provCityDialogFragment == null) {
            provCityDialogFragment = new ProvCityDialogFragment();
            provCityDialogFragment.textCity = textCity;
            provCityDialogFragment.textProv = textProv;
        }
        provCityDialogFragment.show(fragmentManager, "dialog");
    }


    MultiChoicePickerDialog bodyColorDialog;
    private void openVehicleBodyColorPickerDialog(View view){
        FragmentManager fragmentManager = getFragmentManager();
        if(bodyColorDialog == null) {
            bodyColorDialog = new MultiChoicePickerDialog();
            bodyColorDialog.code_string_array = R.array.body_color_code;
            bodyColorDialog.name_string_array = R.array.body_color_name;
            bodyColorDialog.title_string = R.string.pick_body_color;
        }
        bodyColorDialog.show(fragmentManager, "dialog");
    }

    SingleChoicePickerDialog brandPicker;
    private void openVehicleBrandPickerDialog(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        if(brandPicker == null) {
            brandPicker = new SingleChoicePickerDialog();
            brandPicker.res_name_array = R.array.vehicle_brand;
            brandPicker.res_title = R.string.pick_vehicle_brand;
        }
        brandPicker.show(fragmentManager, "dialog");
    }

    MultiChoicePickerDialog policeDialog;
    private void openPoliceMultiPickerDialog(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        if(policeDialog == null) {
            policeDialog = new MultiChoicePickerDialog();
            policeDialog.code_string_array = R.array.police_code;
            policeDialog.name_string_array = R.array.police_name;
            policeDialog.title_string = R.string.pick_police;
        }
        policeDialog.show(fragmentManager, "dialog");
    }

    /***************************************************
     *               查询操作
     ***************************************************/
    private void doVehicleQuery(){
        String plateNoChars = textPlateNO.getText().toString();
        if(StringUtil.isEmpty(plateNoChars) || plateNoChars.length()<5){
            Toast.makeText(getActivity(),R.string.prompt_require_plate_no,Toast.LENGTH_SHORT).show();
            return;
        }
        //时间字符串
        String startTime = textStartDate.getText().toString() + " " +
                textStartTime.getText().toString() + ":00";
        String endTime = textEndDate.getText().toString() + " " +
                textEndTime.getText().toString() + ":59";

        //时间字符串转为时间变量
        Calendar startDate = DateUtil.parseDateTime(startTime);
        Calendar endDate = DateUtil.parseDateTime(endTime);

        //根据开始时间，知道最大的截止时间
        Calendar maxEndDate = (Calendar)startDate.clone();
        int month = StringUtil.parseInteger(getResources().getString(R.string.max_date_span));
        maxEndDate.add(Calendar.MONTH, month);
        //判断输入的截止时间是否晚于最大的直接时间
        if(endDate.after(maxEndDate)){
            Toast.makeText(getActivity(), R.string.prompt_max_date_span,Toast.LENGTH_SHORT).show();
            return;
        }

        String reg = "[\\- \\:]";
        startTime = startTime.replaceAll(reg,"");
        endTime = endTime.replaceAll(reg,"");
        String paraTime = startTime + "-" + endTime;
        String paraPlateNo = textProv.getText().toString() + textCity.getText().toString()+
                textPlateNO.getText().toString();
        String paraVehBrand = textVehicleBrand.getText().toString();
        String paraVehBodyColor = StringUtil.parseString(textBodyColor.getTag());
        String paraPolice = StringUtil.parseString(textPolice.getTag());

        buttonVehicleQuery.setText(R.string.query_waiting);
        buttonVehicleQuery.setEnabled(false);

        final JSONObject json = new JSONObject();
        try {
            json.put("PARA_TIME",paraTime);
            json.put("PARA_PLATENO",paraPlateNo);
            json.put("PARA_PLATECOLOR",6);
            json.put("PARA_NOPLATE",0);
            json.put("PARA_BODY_COLOR",paraVehBodyColor);
            json.put("PARA_BRAND",paraVehBrand);
            json.put("polices",paraPolice);
            json.put("separator",",");
            json.put("user","OO");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread() { // 网络相关处理不能在主线程上进行
            @Override
            public void run() {

                createRequest( json);

            }
        }.start();

    }

    private final void createRequest(JSONObject params){
        String url = getResources().getString(R.string.vehicle_query_url);
        Log.d(TAG,"query url = " + url);

        String totalKey = getResources().getString(R.string.vehicle_query_result_total);
        int total = 0;
        try {
            String result = HttpUtil.post(url, params);
            JSONObject json = new JSONObject(result);
            total = json.getInt(totalKey);

            if(total==0){
                Toast.makeText(getActivity(),R.string.no_result,Toast.LENGTH_SHORT).show();
                return;
            }


            Intent intent = new Intent(getActivity(),ImageListActivity.class);
            intent.putExtra(EXTRA_RESULT_NAME,result);
            startActivityForResult(intent, CODE_SHOW_RESULT);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CODE_SHOW_RESULT) {
            buttonVehicleQuery.setEnabled(true);
            buttonVehicleQuery.setText(R.string.query);
        }

    }
    private Activity getActivity(){
        return VehicleQueryActivity.this;
    }
}
