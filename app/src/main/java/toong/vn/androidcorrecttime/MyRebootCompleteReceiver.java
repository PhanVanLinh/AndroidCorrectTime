package toong.vn.androidcorrecttime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import toong.vn.androidcorrecttime.api.SharedPreferenceApi;

/**
 * Created by PhanVanLinh on 06/11/2017.
 * phanvanlinh.94vn@gmail.com
 */

public class MyRebootCompleteReceiver extends BroadcastReceiver {
    public static String ACTION = "reboot";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "reboot complete", Toast.LENGTH_SHORT).show();
        Log.i("TAG", "reboot complete sssss");
        SharedPreferenceApi mSharedPreferenceApi = new SharedPreferenceApi(context);
        mSharedPreferenceApi.put(SharedPreferenceApi.PREFS_DELTA_TIME_FROM_REBOOT,
                System.currentTimeMillis() - SystemClock.elapsedRealtime());
    }
}
