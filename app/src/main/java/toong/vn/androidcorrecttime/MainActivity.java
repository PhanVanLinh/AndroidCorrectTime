package toong.vn.androidcorrecttime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import toong.vn.androidcorrecttime.api.SharedPreferenceApi;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    TextView tvTime;
    TextView tvServerTime;
    SharedPreferenceApi mSharedPreferenceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferenceApi = new SharedPreferenceApi(this);
        tvTime = findViewById(R.id.text_time);
        tvServerTime = findViewById(R.id.text_server_time);

        findViewById(R.id.button_fetch_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("http://phanvanlinh.ga/current_t.php");
            }
        });

        startService(new Intent(MainActivity.this, MyService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Long deltaTime = mSharedPreferenceApi.get(SharedPreferenceApi.PREFS_DELTA_TIME, Long.class);
        tvTime.setText(convertDate(System.currentTimeMillis() - (deltaTime == null ? 0 : deltaTime),
                "dd/MM/yyyy hh:mm:ssz"));
    }

    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("s");
            pd.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvServerTime.setText(result);
            long serverTime =
                    Long.parseLong(result.trim()) + (int) (7.5f * DateUtils.MINUTE_IN_MILLIS);
            Log.i(TAG, "server time " + serverTime);
            long deltaTime = System.currentTimeMillis() - serverTime;
            long deltaTimeWithReboot = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            mSharedPreferenceApi.put(SharedPreferenceApi.PREFS_DELTA_TIME, deltaTime);
            mSharedPreferenceApi.put(SharedPreferenceApi.PREFS_DELTA_TIME_FROM_REBOOT,
                    deltaTimeWithReboot);
            Log.i(TAG, "delta time " + deltaTime);
            Log.i(TAG, "delta time with reboot " + deltaTimeWithReboot);
            pd.dismiss();
            recreate();
        }
    }
}
