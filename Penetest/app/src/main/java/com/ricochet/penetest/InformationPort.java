package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by user on 2016/8/4.
 */
public class InformationPort extends IntentService {
    public InformationPort(){
        super("InformationPort");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        String string = intent.getStringExtra("URI");
        StringBuilder response=new StringBuilder("");


        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://119.29.166.92/test/index.php").openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            String params = "command=nmap&website=" + string;
            connection.getOutputStream().write(params.getBytes());
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String response3 = response.toString();
        response3 = response3.replace("[", "");
        response3 = response3.replace("]", "");
        response3 = response3.replace("\"", "");
        response3 = response3.replace(",", "\n");



        Intent intentRe = new Intent("com.android.penetest.INFORMATION_PORT");
        intentRe.putExtra("id",3);
        intentRe.putExtra("URI",response3.toString());
        sendBroadcast(intentRe);
    }

}
