package package1;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/")
public class CorsFilter implements Filter{

	public CorsFilter() {
		
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse=(HttpServletResponse) response;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Credential", "true");
		httpServletResponse.setHeader("Access-Control-Allow-Header", 
				((HttpServletRequest) request).getHeader("Access-control-Request-Header"));
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "GET, POST, PUT, OPTIONS, DELETE");
		httpServletResponse.setHeader("Access-Control-Expose-Headers", "userToken");
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
