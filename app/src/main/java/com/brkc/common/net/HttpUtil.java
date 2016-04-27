package com.brkc.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.brkc.traffic.R;
import com.brkc.common.base.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Administrator on 16-4-18.
 */
public class HttpUtil {
    private static String TAG = "HttpUtil";

    private static String sessionID;

    public static void setNetworkAvailable(boolean networkAvailable) {
        HttpUtil.networkAvailable = networkAvailable;
    }

    private static boolean networkAvailable = false;

    public static void get(final String address, final HttpCallbackListener listener) {

        if (!isNetworkAvailable()) {
            Toast.makeText(MyApplication.getContext(), "network is unavailable",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static String post(String url,JSONObject params) {
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        // BufferedReader bufferedReader = null;
        StringBuffer responseResult = new StringBuffer();
        StringBuffer queryString = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        // 组织请求参数
        Iterator<String> it = params.keys();
        while (it.hasNext()) {
            String key = it.next();
            try {
                Object value = params.get(key);
                queryString.append(key);
                queryString.append("=");
                queryString.append(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queryString.append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();

            // 设置通用的请求属性
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            String contentLength = "" + queryString.toString().getBytes().length;
            httpURLConnection.setRequestProperty("Content-Length", contentLength);
            if(sessionID != null) {
                httpURLConnection.setRequestProperty("Cookie", sessionID);
            }

            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(queryString.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 根据ResponseCode判断连接是否成功
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                Log.d(TAG, " Error===" + responseCode);
            } else {
                Log.d(TAG,"Post Success!");
            }

            String cookie = httpURLConnection.getHeaderField("set-cookie");
            Log.d(TAG,"cookie :" + cookie);
            if(cookie != null) {
                sessionID = cookie.substring(0, cookie.indexOf(";"));
            }

            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();

            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String responseText = responseResult.toString();
        int logLen = 500;
        String logStr = (responseText!=null && responseText.length()>logLen) ?
                responseText.substring(0, logLen) : responseText;
        Log.i(TAG, "返回结果:" + logStr);

        return responseText;
    }

    public static boolean isNetworkAvailable() {

        Context context = MyApplication.getContext();

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            networkAvailable = true;
        } else {
            // display error
            networkAvailable = false;
        }
        Log.d(TAG,"networkAvailable>>" + networkAvailable);
        return networkAvailable;

    }

    public static void toastNetworkInfo(Context context){
        String toastText;
        if(networkAvailable) {
            toastText = context.getResources().getString( R.string.network_connection_aviable_toast);
        }
        else{
            toastText = context.getResources().getString( R.string.no_network_connection_toast);
        }
        Toast.makeText(context,toastText,Toast.LENGTH_SHORT).show();
    }

    public static Date getServerDate() {
        return null;
    }

    public interface HttpCallbackListener {
        void onFinish(String response);
        void onError(Exception e);
    }
}
