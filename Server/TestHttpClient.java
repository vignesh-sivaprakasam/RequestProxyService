


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.request.HttpClientRequestor;
import com.request.RequestInfo;

public class TestHttpClient {
	
	public static void getMethod() {
		HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod("http://dummy.restapiexample.com/api/v1/employees");

        try {
        	method.setRequestHeader("User-Agent", "Mozilla");
        	method.setRequestHeader("Cache-Control", "no-cache");
        	method.setRequestHeader("Connection", "keep-alive");
        	method.setRequestHeader("Accept", "*/*");
            client.executeMethod(method);

            if (method.getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = method.getResponseBodyAsStream();

                if (is != null) {
                    Writer writer = new StringWriter();
                    char[] buffer = new char[1024];
                    try {
                        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        int length;
                        while ((length = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, length);
                        }
                    } finally {
                        is.close();
                    }
                    System.out.println("Response = " + writer.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
	}
	
	public  static void main(String []a) {
//		getMethod();
//		postMethod();
//		newGet();
		newerGet();
	}

	public static void newerGet() {
		String url 		= "http://dummy.restapiexample.com/api/v1/employees";
		String method	= "get";
		RequestInfo requestInfo  = new RequestInfo(url, method);
		HttpClientRequestor http = new HttpClientRequestor(requestInfo);
		http.send();
	}
}