package toong.vn.androidcorrecttime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MyTimeSetReceiver extends BroadcastReceiver {
    public static String ACTION = "time-set";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyTimeSetReceiver", "time set: " + context.getApplicationContext());
        Toast.makeText(context, "time set detected", Toast.LENGTH_LONG).show();
        sendBroadcast(context);
    }

    private void sendBroadcast(Context context) {
        Intent intent = new Intent(ACTION);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}