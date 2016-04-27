package com.brkc.traffic.dialog;

import java.util.List;

/**
 * Created by Administrator on 16-4-22.
 */
public interface CustomChoicePickerDialog {
    /**
     *
     * @param dialog
     * @param res_code_array 区分是哪种代码
     * @param names     名称List或者是名称字符串
     * @param codes
     */
    public void onChange(CustomChoicePickerDialog dialog, int res_code_array, Object names, Object codes);
}
