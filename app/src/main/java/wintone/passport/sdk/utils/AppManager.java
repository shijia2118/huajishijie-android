package wintone.passport.sdk.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

public class AppManager {
    private static Stack<Activity> activityStack = new Stack<Activity>();
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
        print();
    }


    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
        print();
    }


    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
        print();
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
        print();
    }

    public void finishActivityBesides(Class<?> cls) {
        int index = 0;
        while (index < activityStack.size()) {
            Activity activity = activityStack.get(index);
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
                index = 0;
            } else index++;
        }


        print();
    }

    public void print() {
        for (Activity activity : activityStack) {
            Log.d("kkkkk", activity.getClass() + "");
        }
    }


    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void finishAllActivityBesidesCurrent() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.lastElement() != activityStack.get(i) && null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
//        activityStack.clear();
    }


    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
