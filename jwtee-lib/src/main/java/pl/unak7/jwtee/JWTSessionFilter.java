package pl.unak7.jwtee;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "jwtSessionFilter", urlPatterns = {"/api/*"})
public class JWTSessionFilter implements Filter {

    @Inject
    JWTSessionManager sessionManager;

    @Inject
    JWTSessionConfigurationManager configurationManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String headerName = configurationManager.getConfiguration().getHeaderName();
        String token = ((HttpServletRequest) servletRequest).getHeader(headerName);
        sessionManager.setToken(token);

        filterChain.doFilter(servletRequest, servletResponse);

        TokenAttachStrategyEnum attachStrategy = configurationManager.getConfiguration().getTokenAttachStrategy();
        try {
            if(attachStrategy == TokenAttachStrategyEnum.ALWAYS || (
                    attachStrategy == TokenAttachStrategyEnum.UPDATE
                    && !token.equals(sessionManager.getToken()))){
                ((HttpServletResponse) servletResponse).setHeader(headerName, sessionManager.getToken());
            }
        } catch (TokenNotFoundException e) { }
    }
}
