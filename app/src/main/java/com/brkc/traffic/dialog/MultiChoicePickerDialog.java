package com.brkc.traffic.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.brkc.traffic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-21.
 */
public class MultiChoicePickerDialog extends DialogFragment {
    private static final String TAG = "MultiChoicePickerDialog";
    private String[] codeList = null;  //全部选项
    private String[] nameList = null;
    private List mSelectedNames = new ArrayList();  // Where we track the selected items
    private List mSelectedCodes = new ArrayList();  // Where we track the selected items
    private boolean[] checkedItem = null;   //每个选项的选择状态

    public int name_string_array;
    public int code_string_array;
    public int title_string;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        getResValues(getResources());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(title_string);

        Log.d(TAG,"checkedItem=" + checkedItem[2]);

        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(name_string_array, checkedItem,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        Log.d(TAG, "checkedItem[which]=" + checkedItem[which]);
                        Log.d(TAG, "isChecked=" + isChecked);
                        checkedItem[which] = isChecked;

                        String name = nameList[which];
                        String code = codeList[which];

                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedNames.add(name);
                            mSelectedCodes.add(code);
                        } else if (mSelectedNames.contains(name)) {
                            // Else, if the item is already in the array, remove it
                            mSelectedNames.remove(name);
                            mSelectedCodes.remove(code);
                        }
                    }
                });
        // Set the action buttons

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog

                listener.onChange(null,code_string_array,mSelectedNames,mSelectedCodes);
            }
        });


        return builder.create();
    }

    private CommonChoicePickerDialog listener;

    private void getResValues(Resources res){
        listener = (CommonChoicePickerDialog) getActivity();

        if(nameList == null){
            nameList = res.getStringArray(name_string_array);
            codeList = res.getStringArray(code_string_array);
        }
        if(checkedItem==null) {
            checkedItem = new boolean[nameList.length];
        }
    }
    public List selectAll(Resources res){
        unSelectAll(res);

        List list = new ArrayList();
        for (int i = 0, len = nameList.length; i < len; i++) {
            mSelectedNames.add(nameList[i]);
            mSelectedCodes.add(codeList[i]);
            checkedItem[i] = true;
        }
        list.add(mSelectedCodes);
        list.add(mSelectedNames);
        return list;
    }
    public void unSelectAll(Resources res){
        getResValues(res);
        mSelectedNames = new ArrayList();
        mSelectedCodes = new ArrayList();
        checkedItem = new boolean[codeList.length];
    }
}
