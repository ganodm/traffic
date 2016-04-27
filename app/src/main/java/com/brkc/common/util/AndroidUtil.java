package com.brkc.common.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 16-4-25.
 */
public class AndroidUtil {

    public static int getAndroidOSVersion()
    {
        int osVersion;
        try
        {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        }
        catch (NumberFormatException e)
        {
            osVersion = 0;
        }

        return osVersion;
    }

    public static Intent createIntent(Context context,Class clazz){
        Intent intent = new Intent(context,clazz);
        return intent;
    }
}
