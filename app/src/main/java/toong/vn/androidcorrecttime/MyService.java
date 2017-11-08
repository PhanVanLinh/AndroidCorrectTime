package toong.vn.androidcorrecttime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import toong.vn.androidcorrecttime.api.SharedPreferenceApi;

public class MyService extends Service {
    String TAG = getClass().getSimpleName();
    SharedPreferenceApi mSharedPreferenceApi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        mSharedPreferenceApi = new SharedPreferenceApi(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyTimeSetReceiver.ACTION);
        intentFilter.addAction(MyRebootReceiver.ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTimeDelta();
            }
        }, intentFilter);
    }


    

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private void updateTimeDelta() {
        long newDelta = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        long oldDelta = getDeltaOfDeviceTimeAndTimeSinceBoot();
        long curDeltaTime = getTimeDelta();
        long newDeltaTime = curDeltaTime + (newDelta - oldDelta);

        mSharedPreferenceApi.put(SharedPreferenceApi.PREFS_DELTA_TIME, newDeltaTime);
        mSharedPreferenceApi.put(SharedPreferenceApi.PREFS_DELTA_TIME_FROM_REBOOT, newDelta);

        Log.i(TAG, "delta time " + newDeltaTime);
        Log.i(TAG, "delta time with reboot " + newDelta);
    }

    private long getDeltaOfDeviceTimeAndTimeSinceBoot() {
        Long delta = mSharedPreferenceApi.get(SharedPreferenceApi.PREFS_DELTA_TIME_FROM_REBOOT,
                Long.class);
        if (delta == null) {
            return 0;
        }
        return delta;
    }

    private long getTimeDelta() {
        Long delta = mSharedPreferenceApi.get(SharedPreferenceApi.PREFS_DELTA_TIME, Long.class);
        if (delta == null) {
            return 0;
        }
        return delta;
    }
}