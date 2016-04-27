package com.brkc.traffic.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.brkc.traffic.R;

/**
 * Created by Administrator on 16-4-21.
 */
public class SingleChoicePickerDialog extends DialogFragment{
    private String[] listItems = null;

    public int res_title;
    public int res_name_array;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (CustomChoicePickerDialog) getActivity();
        if(listItems == null){
            listItems = getResources().getStringArray(res_name_array);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(res_title)
                .setItems(res_name_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String item = listItems[which];
                        listener.onChange(null, res_name_array, item, null);
                    }
                });
        return builder.create();
    }

    private CustomChoicePickerDialog listener;

}
