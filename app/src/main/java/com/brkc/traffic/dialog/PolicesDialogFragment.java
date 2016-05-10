package com.brkc.traffic.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.brkc.traffic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-20.
 */
public class PolicesDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "PolicesDialogFragment";

    private Activity activity;
    private CommonChoicePickerDialog listener;
    private GridView mGridView;

    private List<Dict> dictList;
    private List<String> codeList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private int selectedColor;
    private int defaultColor;
    private int whiteColor;

    public int res_code_array;
    public int res_name_array;
    public int res_title_string;

    private int selectAllFlag = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_police_picker, null);

        listener = (CommonChoicePickerDialog) activity;

        initList(view);

        mGridView = (GridView)view.findViewById(R.id.gridView);
        MyAdapter myAdapter = new MyAdapter(activity, dictList,this);
        mGridView.setAdapter(myAdapter);
        mGridView.setOnItemClickListener(this);

        String title = activity.getString(R.string.select_all) + "/" +
                activity.getString(R.string.unselect_all);
        builder.setView(view)
                .setTitle(res_title_string)
                .setNeutralButton(title,this)
                .setPositiveButton(R.string.ok, PolicesDialogFragment.this)
                .setNegativeButton(R.string.cancel, null);

        return builder.create();
    }

    private void initList(View view) {
        Resources res = view.getResources();
        selectedColor = res.getColor(R.color.activeButton);
        defaultColor = res.getColor(R.color.gray);
        whiteColor = res.getColor(R.color.white);

        dictList = new ArrayList<>();
        String[] codes = res.getStringArray(res_code_array);
        String[] names = res.getStringArray(res_name_array);
        for (int i = 0, len = codes.length; i < len; i++) {
            Dict dict = new Dict();
            dict.code = codes[i];
            dict.name = names[i];
            dictList.add(dict);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView)view;

        int index = isChecked(textView.getTag().toString());
        if(index>=0){
            setItemOutlook(textView, false);
            codeList.remove(index);
            nameList.remove(index);
        }
        else{
            setItemOutlook(textView, true);
            codeList.add(textView.getTag().toString());
            nameList.add(textView.getText().toString());
        }

    }

    private void setItemOutlook(TextView view, boolean selected){

        if(selected){
            view.setTextColor(whiteColor);
            view.setBackgroundColor(selectedColor);
        }else{
            view.setTextColor(defaultColor);
            view.setBackgroundColor(whiteColor);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            listener.onChange(null, R.array.police_code, nameList, codeList);
        }
        else if (which==DialogInterface.BUTTON_NEUTRAL){
            if(selectAllFlag==0){
                select(true);
                selectAllFlag = 1;
            }else{
                selectAllFlag = 0;
                select(false);
            }
            listener.onChange(null, R.array.police_code, nameList, codeList);
        }
    }

    private void select(boolean selectAll) {
        codeList = new ArrayList<>();
        nameList = new ArrayList<>();

        int len = mGridView.getChildCount();
        for (int i = 0; i < len; i++) {
            TextView textView = (TextView)mGridView.getChildAt(i);
            setItemOutlook(textView,selectAll);
            if(selectAll){
                codeList.add(textView.getTag().toString());
                nameList.add(textView.getText().toString());
            }
        }
    }



    class MyAdapter<T> extends CommonAdapter<T>
    {
        private PolicesDialogFragment mDialog;
        private GridView.LayoutParams mImageViewLayoutParams;

        public MyAdapter(Context context, List<T> mData, PolicesDialogFragment dialog)
        {
            super(context, mData);
            mDialog = dialog;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Dict item = (Dict)getItem(position);
            TextView textView = new TextView(mContext);
            textView.setText(item.name);
            textView.setTag(item.code);
            textView.setPadding(0, 10, 0, 10);
            textView.setLayoutParams(mImageViewLayoutParams);
            textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

            if(mDialog.isChecked(item.code)>=0){
                setItemOutlook(textView, true);
            }
            else{
                setItemOutlook(textView, false);
            }
            return textView;
        }
    }
    class Dict{
        public String code;
        public String name;
    }

    private int isChecked(String code){
        int index = -1;
        for (int i = 0; i < codeList.size(); i++) {
            String test = codeList.get(i);
            if(test.equals(code)){
                index = i;
                break;
            }
        }
        return index;
    }
}
