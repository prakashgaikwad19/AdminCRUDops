package package1;

import java.io.IOException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class StartJettyServer {
	public static void main(String[] args) {
		try {
			Server server=BuildJetty.jetty()
					.setIpPortAndThread("localhost", 8080, 10, 20)
					.handler("/hello/",Hello.class)
					.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}