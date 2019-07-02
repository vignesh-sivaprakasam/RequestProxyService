package com.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class RequestInfo {
	private String url;
	private String method;
	private JSONObject  params;
	private JSONObject  headers;
	private String requestEncoding = "UTF-8";
	private String responseEncoding = "UTF-8";
	
	public String getRequestEncoding() {
		return requestEncoding;
	}

	public void setRequestEncoding(String requestEncoding) {
		this.requestEncoding = requestEncoding;
	}

	public String getResponseEncoding() {
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}

	public RequestInfo(String url, String method) {
		this.url 		= url;
		this.method 	= method;
	}
	
	public RequestInfo(String url, String method, JSONObject params) {
		this.url 		= url;
		this.method 	= method;
		this.params 	= params;
	}
	
	public RequestInfo(String url, String method, JSONObject params, JSONObject headers) {
		this.url 		= url;
		this.method 	= method;
		this.params 	= params;
		this.headers 	= headers;
	}
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public JSONObject getHeaders() {
		return headers;
	}
	public void setHeaders(JSONObject headers) {
		this.headers = headers;
	}
	
	public String getAsQueryParam() {
		StringBuffer str = new StringBuffer();
		if(this.params != null) {
			try {
				System.out.println(" params not null");
				boolean delimiter = false;
				for(Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
				    String key = (String) iterator.next();
				    if (delimiter) {
						str.append('&');
					}		
					str.append(URLEncoder.encode(key, this.getRequestEncoding()));
					str.append('=').append(URLEncoder.encode(params.getString(key), this.getRequestEncoding()));
					if (!delimiter) {
						delimiter = true;
					}
				}
				System.out.println(" string "+str.toString());
				return str.toString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
