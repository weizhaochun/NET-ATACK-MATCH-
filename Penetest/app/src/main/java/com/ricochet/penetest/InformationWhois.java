package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016/8/4.
 */
public class InformationWhois extends IntentService {
    public InformationWhois() {
        super("InformationWhois");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String string = intent.getStringExtra("URI");
        StringBuilder response = new StringBuilder("");


        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://119.29.166.92/test/index.php").openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            String params = "command=whois&website=" + string;
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


        String response2 = response.toString();
        StringBuilder result2 = new StringBuilder("");
        String registrar = "", date1 = "", date2 = "", date3 = "", registrantName = "", registrantOrganization = "", registrantStreet = "", registrantCity = "",
                registrantState = "", registrantCountry = "", registrantPhone = "";
        if (response2.contains("ROID")) {
            try {
                JSONObject splitDetail = new JSONObject(response2);
                registrantName = splitDetail.getString("Registrant");
                registrantPhone = splitDetail.getString("Registrant Contact Email");
                registrantOrganization = splitDetail.getString("Sponsoring Registrar");
                date1 = splitDetail.getString("Registration Time");
                date2 = splitDetail.getString("Expiration Time");
                result2.append("   注册人：").append(registrantName).append("\n").append("   注册时间：").append(date1).append("\n").append("   到期时间：").append(date2).append("\n")
                        .append("   注册人邮箱：").append(registrantPhone).append("\n").append("   服务商：").append(registrantOrganization);

            } catch (Exception e) {
            }

        } else {
            try {
                JSONObject splitDetail = new JSONObject(response2);
                registrar = splitDetail.getString("Registrar");
                date1 = splitDetail.getString("Updated Date");
                date2 = splitDetail.getString("Creation Date");
                date3 = splitDetail.getString("Registrar Registration Expiration Date");
                registrantName = splitDetail.getString("Registrant Name");
                registrantOrganization = splitDetail.getString("Registrant Organization");
                registrantStreet = splitDetail.getString("Registrant Street");
                registrantCity = splitDetail.getString("Registrant City");
                registrantState = splitDetail.getString("Registrant State/Province");
                registrantCountry = splitDetail.getString("Registrant Country");
                registrantPhone = splitDetail.getString("Registrant Phone");
                result2.append("注册商：").append(registrar).append("\n").append("更新时间：").append(date1).append("\n").append("创建时间：").append(date2).append("\n")
                        .append("到期时间：").append(date3).append("\n").append("注册人姓名：").append(registrantName).append("\n").append("注册机构：").append(registrantOrganization)
                        .append("\n").append("注册人地址信息：").append(registrantStreet + "," + registrantCity + "," + registrantState + "," + registrantCountry).append("\n")
                        .append("注册人电话：").append(registrantPhone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intentRe = new Intent("com.android.penetest.INFORMATION_WHOIS");
        intentRe.putExtra("id", 2);
        intentRe.putExtra("URI", result2.toString());
        sendBroadcast(intentRe);

    }
}
