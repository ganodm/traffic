package com.brkc.common.util;

import java.util.List;

/**
 * Created by Administrator on 16-4-18.
 */
public class StringUtil {
    public static int parseInteger(String s) {
        int i = 0;
        if(!StringUtil.isEmpty(s)) {
            try {
                i = Integer.parseInt(s);
            }catch (Exception e){

            }
        }
        return i;
    }

    public static boolean isEmpty(String s) {
        return s==null || s.trim().length()==0;
    }

    public static String parseString(Object tag) {
        return tag == null ? "" : tag.toString();
    }

    public static String join(String[] array,String sep){
        StringBuffer buf = new StringBuffer();
        int len = array.length;
        for (int i = 0; i < len; i++) {
            if(i>0){
                buf.append(sep);
            }
            buf.append(array[i]);
        }

        return buf.toString();
    }
    public static String join(List list,String sep){
        StringBuffer buf = new StringBuffer();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            if(i>0){
                buf.append(sep);
            }
            buf.append(list.get(i));
        }

        return buf.toString();
    }
}
