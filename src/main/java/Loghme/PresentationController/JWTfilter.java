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

@ WebFilter(asyncSupported = true)
public class JWTfilter implements Filter {
    public void destroy() {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) servletRequest).getMethod().equals("OPTIONS")) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI().split("/")[2];
        if (path.equals("authentication")) {
            chain.doFilter(servletRequest, servletResponse);
        }
        else {
            String jwtStr = request.getHeader("Authorization");
            if (jwtStr == null) {
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            int userId = JWTmanager.getInstance().validateJWT(jwtStr);
            if (userId == -1) {
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            request.setAttribute("userId", userId);
        }
        chain.doFilter(request, servletResponse);
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }
}