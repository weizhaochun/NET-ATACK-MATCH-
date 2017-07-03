package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016/8/14.
 */
public class SQL extends IntentService {
    public SQL(){
        super("SQL");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StringBuilder response = new StringBuilder("");
        try{
            HttpURLConnection connection = (HttpURLConnection)new URL("http://119.29.166.92/test/index3.php").openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);

            String params = "command=sqlmapp&website="+intent.getStringExtra("URI");
            connection.getOutputStream().write(params.getBytes());
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        }catch (Exception e){

        }


        Intent intentRe = new Intent("com.android.penetest.ATTACK_SQL");
        intentRe.putExtra("re",response.toString());
        intentRe.putExtra("id",7);
        sendBroadcast(intentRe);

    }
}
