package com.brkc.common.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16-4-25.
 */
public class ActivityController {
    private static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        if(!activities.contains(activity))
            activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    public static int size(){
        return activities.size();
    }
}
