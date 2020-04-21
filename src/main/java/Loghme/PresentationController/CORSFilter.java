package Loghme.PresentationController;

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


@ WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class CORSFilter implements Filter {
        public void destroy() {

        }

        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

            // Server delay
            try {
                Thread.sleep(3000);

                // Authorize (allow) all domains to consume the content
                ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Origin","*");
                ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Methods","OPTIONS, GET, POST, PUT, DELETE, HEAD");
                ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, X-Device-User-Agent, Content-Type, Accept, Authentication");

                // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
                if (((HttpServletRequest) servletRequest).getMethod().equals("OPTIONS")) {
                    ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_ACCEPTED);
                    return;
                }

                // pass the request along the filter chain
                chain.doFilter(servletRequest, servletResponse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void init(FilterConfig fConfig) throws ServletException {

        }
}