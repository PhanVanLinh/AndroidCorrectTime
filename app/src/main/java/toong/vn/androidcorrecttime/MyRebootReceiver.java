package toong.vn.androidcorrecttime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PhanVanLinh on 06/11/2017.
 * phanvanlinh.94vn@gmail.com
 */

public class MyRebootReceiver extends BroadcastReceiver {
    public static String ACTION = "reboot";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "reboot sssss");
        System.nanoTime();
        Toast.makeText(context, "reboot", Toast.LENGTH_SHORT).show();
    }
}
