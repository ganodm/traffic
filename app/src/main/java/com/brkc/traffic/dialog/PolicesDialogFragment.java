package com.brkc.traffic.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.brkc.traffic.R;
import com.brkc.traffic.ui.image.RecyclingImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-20.
 */
public class PolicesDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private static final int COLS = 8;
    private static final String TAG = "PolicesDialogFragment";

    private Activity activity;
    private CommonChoicePickerDialog listener;

    private List<Dict> dictList;
    private List<String> codeList = new ArrayList<String>();
    private List<String> nameList = new ArrayList<String>();
    private int selectedColor;
    private int defaultColor;

    public int res_code_array;
    public int res_name_array;
    public int res_title_string;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_police_picker, null);

        listener = (CommonChoicePickerDialog) activity;

        initList(view);

        GridView gridView = (GridView)view.findViewById(R.id.gridView);
        MyAdapter myAdapter = new MyAdapter(activity, dictList,this);
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(this);

        builder.setView(view)
                .setTitle(res_title_string)
                .setPositiveButton(R.string.ok, PolicesDialogFragment.this)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private void initList(View view) {
        Resources res = view.getResources();
        selectedColor = res.getColor(R.color.activeButton);
        defaultColor = res.getColor(R.color.gray);

        dictList = new ArrayList<Dict>();
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
            textView.setTextColor(0);
            codeList.remove(index);
            nameList.remove(index);
        }
        else{
            textView.setTextColor(selectedColor);
            codeList.add(textView.getTag().toString());
            nameList.add(textView.getText().toString());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        listener.onChange(null,R.array.police_code,nameList, codeList);
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
            textView.setLayoutParams(mImageViewLayoutParams);
            textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);

            if(mDialog.isChecked(item.code)>=0){
                textView.setTextColor(selectedColor);
            }
            else{
                textView.setTextColor(defaultColor);
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
