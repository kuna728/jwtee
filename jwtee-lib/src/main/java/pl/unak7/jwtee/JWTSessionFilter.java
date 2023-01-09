package pl.unak7.jwtee;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "jwtSessionFilter", urlPatterns = {"/api/*"})
public class JWTSessionFilter implements Filter {

    @Inject
    JWTSessionManager sessionManager;

    @Inject
    JWTSessionConfigurationManager configurationManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String headerName = configurationManager.getConfiguration().getHeaderName();
        String headerValue = ((HttpServletRequest) servletRequest).getHeader(headerName);
        sessionManager.setToken(extractTokenFromHeader(headerValue));

        filterChain.doFilter(servletRequest, servletResponse);

        if(configurationManager.getConfiguration().isAttachTokenToResponse()){
            ((HttpServletResponse) servletResponse).setHeader(headerName, sessionManager.getToken());
        }
    }

    private String extractTokenFromHeader(String headerValue) {
        if(headerValue == null)
            return null;
        Pattern p = Pattern.compile(configurationManager.getConfiguration().getHeaderValuePattern());
        Matcher m = p.matcher(headerValue);
        if(m.find())
            return m.group("token");
        return null;
    }
}
