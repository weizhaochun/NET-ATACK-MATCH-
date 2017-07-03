package com.ricochet.penetest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupParseURL {
	int number=1;
	public JsoupParseURL (String target){
		if(target.substring(target.length()-1,target.length()).equals("/"))
			url_ = target.substring(0,target.length()-1);
		else
			url_ = target;
		if(url_.contains("www."))
			url_ = url_.substring(4,url_.length());
		url = "http://www."+url_;
		//url_ssl = "https://www."+url;
	}
	private String url;   //not support "https://"
	private String url_;  
	//private String url_ssl;   
	private Map<String, Boolean> urls= new HashMap<String , Boolean>();
//	private Map<String, Boolean> urls_second= new HashMap<String , Boolean>();
	
	public void handleWork(){
		try{
			urls.put(url,true);
			Document doc = Jsoup.connect(url).get();
	        Elements links = doc.select("a[href]");
			for (Element link : links) {
			  String linkHref = link.attr("href");
			  if(!linkHref.substring(0,1).equals(".")&&linkHref.contains(".")&&!linkHref.contains("#")&&!linkHref.contains("javascript")){
				  if(!linkHref.contains("http"))
					  linkHref = url +"/"+ linkHref;
				  else;
				  if(linkHref.substring(linkHref.length()-1,linkHref.length()).equals("/"))
					  linkHref =linkHref.substring(0,linkHref.length()-1);
				  else;
				  if(linkHref.contains(url_)){
					  if(!urls.containsKey(linkHref))
						  number++;
						  urls.put(linkHref , false);
				  }
			  }
			}
		}catch(Exception e){
			
		}
		
//		for(Map.Entry<String, Boolean>  a : urls.entrySet()){
//			parseAgain(a);
//			urls_second.put(a.getKey(), a.getValue());
//		}
//
//	}
//	private void parseAgain(Map.Entry<String, Boolean> single){
//		if(single.getValue()==false){
//			try{
//				Document doc = Jsoup.connect(single.getKey()).get();
//				single.setValue(true);
//				Elements links = doc.select("a[href]");
//				for (Element link : links){
//					String linkHref = link.attr("href");
//					if(!linkHref.substring(0,1).equals(".")&&linkHref.contains(".")&&!linkHref.contains("#")&&!linkHref.contains("javascript")){
//					  if(!linkHref.contains("http"))
//						  linkHref = single.getKey() +"/"+ linkHref;
//					  else;
//					  if(linkHref.substring(linkHref.length()-1,linkHref.length()).equals("/"))
//						  linkHref =linkHref.substring(0,linkHref.length()-1);
//					  else;
//					  if(linkHref.contains(url_))
//						  if(!urls.containsKey(linkHref))
//							  urls_second.put(linkHref , false);
//					  }
//				}
//			}
//			catch(Exception e){
//
//			}
			
//		}
	}
	public ArrayList<String> getURLs(){
		ArrayList<String> array = new ArrayList<String>();
		for(Map.Entry<String, Boolean> a : urls.entrySet()){
			array.add(a.getKey());
		}
		return array;
	}
	
}
