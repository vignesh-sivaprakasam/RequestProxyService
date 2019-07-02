/**
 * 
 */
package com.request;

/**
 * @author vignesh-4121
 *
 */
public interface Request {
	public void setRequestMethod(String method);
	public void setRequestHeader(String key, String value);
	public String send();
	
	public int getStatusCode();
}
