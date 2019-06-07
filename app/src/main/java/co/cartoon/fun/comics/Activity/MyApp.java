package co.cartoon.fun.comics.Activity;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * Created by TedPark on 2018. 2. 3..
 */

public class MyApp extends Application {
    public static MyApp INSTANCE = null;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            co.cartoon.fun.comics.Util.NotificationManager.createChannel(MyApp.INSTANCE);
        }
    }
}
