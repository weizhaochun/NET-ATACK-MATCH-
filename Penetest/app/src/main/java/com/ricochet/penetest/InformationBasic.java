package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by user on 2016/8/4.
 */
public class InformationBasic extends IntentService {

    public InformationBasic(){
        super("InformationBasic");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String string = intent.getStringExtra("URI");
        StringBuilder response=new StringBuilder("");

        HttpURLConnection connection = null;
        try {
            InetAddress address = InetAddress.getByName(string);
            String[] str = address.toString().split("[/]");
            connection = (HttpURLConnection) new URL("http://apis.baidu.com/showapi_open_bus/ip/ip?ip=" + str[1]).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", "b10ff7896898934639725d19f6a63494");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
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


        StringBuilder result1 = new StringBuilder("");

        String region = "", ip = "获取失败", isp = "获取失败", country = "获取失败", city = "";
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            String detail = jsonObject.getString("showapi_res_body");
            JSONObject splitDetail = new JSONObject(detail);
            region = splitDetail.getString("region");
            isp = splitDetail.getString("isp");
            ip = splitDetail.getString("ip");
            city = splitDetail.getString("city");
            country = splitDetail.getString("country");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result1.append("ip地址：").append(ip).append("\n").append("网络服务提供商：").append(isp).append("\n").append("所在位置：").append(country + " " + region + " " + city);




        Intent intentRe = new Intent("com.android.penetest.INFORMATION_BASIC");
        intentRe.putExtra("id",1);
        intentRe.putExtra("URI",result1.toString());
        sendBroadcast(intentRe);

    }

}
