package com.brkc.traffic.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.brkc.traffic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-21.
 */
public class MultiChoicePickerDialog extends DialogFragment {
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
        listener = (CommonChoicePickerDialog) getActivity();

        if(nameList == null){
            nameList = getResources().getStringArray(name_string_array);
            codeList = getResources().getStringArray(code_string_array);
        }
        if(checkedItem==null) {
            checkedItem = new boolean[nameList.length];
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(title_string);
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(name_string_array, checkedItem,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
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

}
