package package1;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class Jettyclass {
	private String ipAddress="localhost";
	private int portNumber=8080;
	
	//private FilterHolder holder=new FilterHolder((Class<? extends Filter>) CorsFilter.class);
	private ContextHandlerCollection contextHandlerCollection=new ContextHandlerCollection();
	private QueuedThreadPool queuedThreadPool=new QueuedThreadPool(20, 10);
	private Server server;
	
	public Jettyclass setIpPortAndThread(String ipAddress,int portNumber,
			int minThreads,int maxThreads) {
		this.ipAddress=ipAddress;
		this.portNumber=portNumber;
		this.queuedThreadPool.setMinThreads(minThreads);
		this.queuedThreadPool.setMaxThreads(maxThreads);
		return this;
	}
	public Jettyclass handler( String contextPath,Class<? extends HttpServlet> handlerClass) {
		ServletContextHandler servletContextHandler=new ServletContextHandler();
		servletContextHandler.setContextPath(contextPath);
		servletContextHandler.addServlet(handlerClass, "/*");
		
		/*servletContextHandler.addFilter(holder, "/", 
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC));
		*/
		
		servletContextHandler.setAllowNullPathInfo(true);
		contextHandlerCollection.addHandler(servletContextHandler);
		return this;
	}
	
	public Server start() throws Exception{
		this.server=new Server(queuedThreadPool);
		ServerConnector connector=new ServerConnector(server);
		connector.setHost(this.ipAddress);
		connector.setPort(portNumber);
		
		ServerConnector[] connectors={connector};
		
		this.server.setConnectors(connectors);
		
		HandlerCollection handler=new HandlerCollection();
		
		handler.setHandlers(new Handler[] {contextHandlerCollection,new DefaultHandler()});
		this.server.setHandler(handler);
		this.server.start();
		return this.server;
	}
}
