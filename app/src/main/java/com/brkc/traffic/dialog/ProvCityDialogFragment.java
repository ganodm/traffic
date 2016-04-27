package com.brkc.traffic.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.brkc.traffic.R;

/**
 * Created by Administrator on 16-4-20.
 */
public class ProvCityDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener,View.OnClickListener{

    private static final int TAG_TYPE = 1;//类型：省份还是城市
    private static final int TAG_INDEX = 2;//索引，省份索引
    public static final int TYPE_PROV = 1;//表示省份
    public static final int TYPE_CITY = 2;//表示城市

    private static final int COLS = 8;
    private static final String TAG = "ProvCityDialogFragment";

    private Activity activity;
    private TableLayout provTable;
    private TableLayout cityTable;

    private ProvCityOnChangeListener listener;
    private String[] cityList;

    private int defaultColor;
    private int selectedColor;
    /**
     * 省份和城市选择
     * @param v
     */
    @Override
    public void onClick(View v) {
        //类型标识
        String sTag = v.getTag().toString();
        int type = Integer.parseInt(sTag.split(",")[0]);

        resetTextColor(type);

        TextView button = (TextView)v;
        selected(button);

        String text = button.getText().toString();
        listener.onChange(null, type, text);

        if(type == TYPE_PROV){
            initCityTable(button, null);
        }
    }

    private void selected(TextView textView){
        textView.setTextColor(selectedColor);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
    }
    private void resetTextColor(int type){
        TableLayout tableLayout ;
        if(type == TYPE_CITY){
            tableLayout = cityTable;
        }else {
            tableLayout = provTable;
        }
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView button = (TextView)row.getChildAt(j);
                TextPaint tp = button.getPaint();
                tp.setFakeBoldText(false);
                button.setTextColor(defaultColor);
            }
        }
    }
    /**
     * 按钮确定按钮
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public interface ProvCityOnChangeListener
    {
        /**
         *
         * @param type 1:TYPE_PROV,2:TYPE_CITY
         * @param text
         */
        void onChange(ProvCityDialogFragment dialog,int type, String text);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_prov_city, null);

        provTable = (TableLayout) view.findViewById(R.id.table_prov);
        cityTable = (TableLayout) view.findViewById(R.id.table_city);

        listener = (ProvCityOnChangeListener) activity;

        Resources res = view.getResources();
        String provStr = res.getString(R.string.province_list);
        String cityListStr = res.getString(R.string.city_list);
        cityList = cityListStr.split(",");

        defaultColor = res.getColor(R.color.gray);
        selectedColor = res.getColor(R.color.colorAccent);

        initProvTable(provStr);

        builder.setView(view)
                .setTitle(R.string.plate_no_prov_city_dialog_title)
                .setPositiveButton(R.string.ok, ProvCityDialogFragment.this)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private TextView initTable(TableLayout tableLayout, int itemType, String itemList,
                               String defaultText){
        tableLayout.removeAllViews();

        itemList = itemList.replaceAll(" ","");
        String item[] = itemList.split("");
        int itemSize = item.length;
        if(itemSize==0)return null;

        boolean findDefault = false;
        TextView defaultButton = null;

        //布局
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT,1);

        // 依次添加省份
        TableRow row = null;
        int index = 0;
        for (int i = 0; i < itemSize; i++) {
            String text = item[i];
            //判断空白字符（比如回车符之类的）
            if(text.getBytes().length == 0) continue;

            //之所以放在循环里面，是因为有空白字符
            if(defaultText == null && itemType == TYPE_CITY) {
                defaultText = item[i];
                listener.onChange(null, TYPE_CITY, defaultText);
            }

            //创建行
            if(index % COLS == 0){
                row = new TableRow(activity);
                tableLayout.addView(row);
            }

            //选项
            TextView button = new TextView(activity);
            button.setText(text);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(defaultColor);
            button.setTextSize(20);
            button.setLayoutParams(layoutParams);
            button.setTag(itemType + "," + index++);

            button.setOnClickListener(ProvCityDialogFragment.this);

            button.setPadding(0,16,0,0);
            row.addView(button);

            // set a default button_sel
            if(index == 0){
                defaultButton = button;
            }
            // find ex
            if(!findDefault && defaultText != null && defaultText.equals(text)){
                findDefault = true;
                defaultButton = button;
            }
        }
        //最后如果一列还有剩余，用TextView填充
        if(index % COLS >0){
            int span = COLS - index % COLS;
            for (int i = 0; i < span; i++) {
                TextView textView = new TextView(activity);
                textView.setLayoutParams(layoutParams);
                row.addView(textView);
            }
        }
        if(defaultButton != null){
            selected(defaultButton);
        }
        return defaultButton;
    }
    public TextView textProv;
    public TextView textCity;

    private void initProvTable(String provStr) {
        String defaultProvText = textProv.getText().toString();
        String defaultCityText = textCity.getText().toString();

        TextView provButton = initTable(provTable, TYPE_PROV, provStr, defaultProvText);

        if (provButton != null) {
            initCityTable(provButton,defaultCityText);
        }
    }

    private void initCityTable(TextView provButton, String defaultCity) {
        Log.d(TAG, "provButton=" + provButton + ",defaultCity=" + defaultCity);

        if(provButton == null) return;

        Object tag = provButton.getTag();
        Log.d(TAG, "tag=" + tag);
        if(tag == null) return;

        String sTag[] = tag.toString().split(",");

        int provIndex = Integer.parseInt(sTag[1]);
        String cities = cityList[provIndex];
        initTable(cityTable, TYPE_CITY, cities, defaultCity);
    }
}
