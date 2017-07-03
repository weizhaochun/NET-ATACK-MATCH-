package com.ricochet.penetest;

import java.util.regex.Pattern;

/**
 * Created by user on 2016/8/13.
 */
public class URLValidate {
    private String input;
    public URLValidate(String url){
        input = url;
    }
    public Boolean getResult(){
        Pattern pattern =Pattern.compile("^([0-9a-zA-Z\\.\\-])+\\.([a-zA-Z]){2,6}$");
        return pattern.matcher(input).matches();
    }
}
