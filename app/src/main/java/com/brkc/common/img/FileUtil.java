package com.brkc.common.img;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final String TAG = "BRKC_FileUtil";
	  
    public static File getCacheFile(String imageUri){  
        File cacheFile = null;  
        try {
            Log.d(TAG, "Environment.getExternalStorageState()="  + Environment.getExternalStorageState());
            if (Environment.getExternalStorageState().equals(  
                    Environment.MEDIA_MOUNTED)) {  
                File sdCardDir = Environment.getExternalStorageDirectory();

                String fileName = getFileName(imageUri);
                File dir = new File(sdCardDir.getCanonicalPath()  
                        + AsynImageLoader.CACHE_DIR);  
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }
                Log.d(TAG,"dir.getAbsolutePath=" + dir.getAbsolutePath());
                Log.d(TAG,"dir.getCanonicalPath=" + dir.getCanonicalPath());
                Log.d(TAG,"fileName=" + fileName);
                cacheFile = new File(dir, fileName);  
                Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);  
            }    
        } catch (IOException e) {  
            e.printStackTrace();  
            Log.e(TAG, "getCacheFileError:" + e.getMessage());  
        }  
          
        return cacheFile;  
    }  
      
    public static String getFileName(String path) {  
        int index = path.lastIndexOf("/");  
        return path.substring(index + 1);  
    }
}
