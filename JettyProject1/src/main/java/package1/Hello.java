 package package1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Hello extends HttpServlet{
	
	private static final long serialVersionUID=1L;
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String content=null;
		ServletInputStream istr=request.getInputStream();
		
		if(istr!=null) {
			content=IOUtils.toString(istr, request.getCharacterEncoding());
			//content=istr.toString();
		}
		
		String postmanRequest=getRequestHeader(request).get("postman");
		String eventNname=(String) getRequestHeader(request).get("Event-Name");
		//String postManRequest=getRequestBody(request).get("postman");
		//Object Bname=getRequestBody(request).get("Bname");
		
		if(postmanRequest.equalsIgnoreCase("postman")) {
			ExamplePostDelegator postDelegator=ExamplePostDelegator.getInstance();
			postDelegator.setParam(getRequestHeader(request),getRequestBody(request),content,response);
		}
	}
	private HashMap<String, String> getRequestHeader(HttpServletRequest request) {
		HashMap<String, String> headerMap=new HashMap<String, String>();
		try {
			Enumeration<String> header=request.getHeaderNames();
			while(header.hasMoreElements()) {
				String key=header.nextElement();
				String value=request.getHeader(key);
				headerMap.put(key, value);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return headerMap;
	}
	
	private HashMap<String, String> getRequestBody(HttpServletRequest request) {
		HashMap<String, String> headerMap=new HashMap<String, String>();
		try {
			Enumeration<String> header=request.getParameterNames();
			while(header.hasMoreElements()) {
				String key=header.nextElement();
				String value=request.getParameter(key);
				headerMap.put(key, value);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return headerMap;
	}
}
