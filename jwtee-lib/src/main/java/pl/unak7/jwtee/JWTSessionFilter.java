package pl.unak7.jwtee;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter required for JWTee to work properly.
 * In pre-processing phase filter extracts token from request header and initializes JWTSessionManager with token value via setToken() method.
 * In post-processing phase, if attachTokenToResponse configuration property is set to true, filter attaches token to response header. Token is retrieved via getToken() method in JWTSessionManager.
 * Filter is applicable for all application endpoints i.e. url pattern equals {@code "/*"}.
 */
@WebFilter(filterName = "jwtSessionFilter", urlPatterns = {"/*"})
public class JWTSessionFilter implements Filter {

    @Inject
    private JWTSessionManager sessionManager;

    @Inject
    private JWTSessionConfigurationManager configurationManager;

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
