import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.request.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class ApiServlet
 */

/**
 * Servlet implementation class ApiServlet
 */
@WebServlet("/ApiServlet")
public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("server hitting");
        String clientID      = request.getParameter("clientID");
        String requestMethod = request.getParameter("requestMethod");
        String url           = request.getParameter("url");
        String headers       = request.getParameter("headers");

        String requestBody = request.getParameter("requestBody") != null ? request.getParameter("requestBody") : null;

        response.setContentType("application/json");
        
        System.out.println("clientID    : " + clientID);
        System.out.println("requestType : " + requestMethod);
        System.out.println("url         : " + url);
        System.out.println("Headers     : " + headers);
        System.out.println("Body        : " + requestBody);
//        url = "http://ip.jsontest.com/";

        JSONObject json = null;
        JSONObject params = null;
        try {
        	if(headers != null) {        		
        		json = new JSONObject(headers);
        	}
        	if(requestBody != null) {        		
        		params = new JSONObject(requestBody);
        	}
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        JSONObject resp = executeRequest(clientID, url, json, params, requestMethod.toLowerCase());
        String respString = resp != null ? resp.toString() : "";
        System.out.println(" response to client: "+respString);
        System.out.println(" content -type :"+response.getContentType());
        response.getWriter().write(respString);
//        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }

    
    /**
     * 
     * @param clientID
     * @param requestUrl
     * @param headers
     * @param requestBody
     * @return
     * @throws IOException
     */
    private JSONObject executeRequest(String clientID, String requestUrl, JSONObject headers, JSONObject requestBody, String methodType)
            throws IOException {
        System.out.println(" getRequest : ");
        
        try {
        	System.out.println(" url :  "+requestUrl);
        	RequestInfo requestInfo 	= new RequestInfo(requestUrl, methodType, requestBody, headers);
        	HttpClientRequestor http 	= new HttpClientRequestor(requestInfo);
        	JSONObject resp 			= http.send();
        	return resp;
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(" exception: ");
        }

        return null;
    }
    
}


