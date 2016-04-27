package com.brkc.traffic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.brkc.common.logger.Log;
import com.brkc.traffic.R;
import com.brkc.common.net.HttpUtil;

/**
 * Created by Administrator on 16-4-25.
 */
public class NetWorkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetWorkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean test = HttpUtil.isNetworkAvailable();
        String toastText;
        if(test) {
            toastText = context.getResources().getString( R.string.network_connection_aviable_toast);
        }
        else{
            toastText = context.getResources().getString( R.string.no_network_connection_toast);
        }
        HttpUtil.setNetworkAvailable(test);
        Log.d(TAG,toastText);
    }
}
