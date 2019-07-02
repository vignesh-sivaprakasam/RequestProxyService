package com.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

public class HttpClientRequestor {
	private RequestInfo requestInfo;
	
	public HttpClientRequestor(RequestInfo requestInfo) {
		this.requestInfo 	= requestInfo;
	}
	
	public JSONObject send() {
		HttpMethod method = null;
		String responseText = null;
		int responseCode = 0;
		try {
			System.out.println("checking url");
			URL u = new URL(this.requestInfo.getUrl());
			method = getMethod();
			setRequestHeaders(method);
			if(u.getProtocol().equals("http")) {
				System.out.println(" reject : request");
				responseText = "HTTP request rejected";
				return getResponseJson(responseCode, responseText);
			}
		}catch(MalformedURLException mu) {
			System.out.println(" Malformed URL Exception");
			responseCode = 400;
			responseText = "Malformed URL";
			return getResponseJson(responseCode, responseText);
		}
		
		try {
			
			HttpClient http = new HttpClient();
			http.getParams().setParameter("timeout", 1 * 100);
			System.out.println(" sending request");
			responseCode = http.executeMethod(method);
			responseText = getResponse(method, this.requestInfo.getResponseEncoding());
			
		}  catch(URIException u) {
			responseCode = 400;
		} catch (ConnectTimeoutException e) {
			System.out.println(" connection timed out");
			responseCode = 504;
			responseText = "Connection Timed Out";
		} catch (IOException e) {
			responseCode = method.getStatusCode();
			System.out.println(" exception");
			e.printStackTrace();
		}
		System.out.println(" response code :"+responseCode);
		System.out.println(" response text :"+responseText);
		return getResponseJson(responseCode, responseText);
	}
	
	private JSONObject getResponseJson(int statusCode, String responseText) {
		JSONObject json = new JSONObject();
		json.put("status", statusCode);
		json.put("responseText", responseText);
		return json;
	}
	
	private HttpMethod getMethod() {
		HttpMethod method;
		String url 			= this.requestInfo.getUrl();
		String requestType 	= this.requestInfo.getMethod();
		switch(requestType) {
			case "post":
				method = new PostMethod(url);
				setRequestParams(method);
				break;
			case "put":
				method = new PutMethod(url);
				setRequestParams(method);
				break;
			case "delete":
				method = new DeleteMethod(url);
				setRequestParams(method);
				break;
			case "get":
			default:
				String queryParams = this.requestInfo.getAsQueryParam();
				queryParams = queryParams != null ? "?" + queryParams : "";
				url += queryParams;
				method = new GetMethod(url);				
		}
		System.out.println(" url : "+url);
		return method; 
	}
	
	private void setRequestHeaders(HttpMethod method) {
		
		method.setRequestHeader("User-Agent", "Mozilla");
    	method.setRequestHeader("Cache-Control", "no-cache");
    	method.setRequestHeader("Connection", "keep-alive");
    	method.setRequestHeader("Accept", "*/*");
		
		if(this.requestInfo.getHeaders() != null) {
			try {
				JSONObject headers = this.requestInfo.getHeaders();
				for(Iterator iterator = headers.keySet().iterator(); iterator.hasNext();) {
					String key = (String)iterator.next();
					method.setRequestHeader(key, headers.getString(key));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setRequestParams(HttpMethod method) {
		if(this.requestInfo.getParams() != null) {
			try {				
				JSONObject params = this.requestInfo.getParams();
				HttpMethodParams p = new HttpMethodParams();
				for(Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
					String key = (String)iterator.next();
					p.setParameter(key, params.get(key));
				}
				method.setParams(p);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getResponse(HttpMethod method, String responseEncoding) {
		
		InputStream responseStream = null;
		try {
			responseStream = method.getResponseBodyAsStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        if (responseStream != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader 	= null;
				reader 			= new BufferedReader(new InputStreamReader(responseStream, responseEncoding));
                int length;
				while ((length = reader.read(buffer)) != -1) {
				    writer.write(buffer, 0, length);
				}
            } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
                try {
                	responseStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            return writer.toString();
        }
		return null;
	}
}
