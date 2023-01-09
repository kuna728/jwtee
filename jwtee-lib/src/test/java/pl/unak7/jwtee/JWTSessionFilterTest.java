package pl.unak7.jwtee;

import com.sun.javafx.binding.StringFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
public class JWTSessionFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JWTSessionConfigurationManagerBean configurationManager;

    @Mock
    private JWTSessionManagerBean sessionManager;

    @InjectMocks
    private JWTSessionFilter sessionFilter;

    private final Utils utils = new Utils();


    @BeforeEach
    public void setup() throws ServletException, IOException {
        Mockito.doNothing().when(filterChain).doFilter(Mockito.eq(request), Mockito.eq(response));
        Mockito.when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).build());
        Mockito.lenient().when(sessionManager.getToken()).thenReturn("testToken");
    }

    @Test
    public void baseTest() throws ServletException, IOException {
        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(request).getHeader(configurationManager.getConfiguration().getHeaderName());
        Mockito.verify(sessionManager).setToken(null);
        Mockito.verify(sessionManager).getToken();
        Mockito.verify(response).setHeader(configurationManager.getConfiguration().getHeaderName(), "testToken");
    }

    @Test
    public void headerNameTest1() throws ServletException, IOException {
        Mockito.when(request.getHeader(configurationManager.getConfiguration().getHeaderName())).thenReturn("A7.H2.P9");
        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(request).getHeader(configurationManager.getConfiguration().getHeaderName());
        Mockito.verify(sessionManager).setToken("A7.H2.P9");
        Mockito.verify(sessionManager).getToken();
        Mockito.verify(response).setHeader(configurationManager.getConfiguration().getHeaderName(), "testToken");
    }

    @Test
    public void headerNameTest2() throws ServletException, IOException {
        Mockito.when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).headerName("auth").build());

        Mockito.when(request.getHeader("auth")).thenReturn("C3.H2.P9");
        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(request).getHeader(configurationManager.getConfiguration().getHeaderName());
        Mockito.verify(sessionManager).setToken("C3.H2.P9");
        Mockito.verify(sessionManager).getToken();
        Mockito.verify(response).setHeader(configurationManager.getConfiguration().getHeaderName(), "testToken");
    }

    @Test
    public void headerValuePatternTest1() throws ServletException, IOException {
        String pattern = String.format("^Bearer %s$", JWTSessionConfiguration.JWT_PATTERN);
        Mockito.when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).headerValuePattern(pattern).build());

        Mockito.when(request.getHeader(configurationManager.getConfiguration().getHeaderName())).thenReturn("Bearer K8.L6.V0");
        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(sessionManager).setToken("K8.L6.V0");
    }

    @Test
    public void headerValuePatternTest2() throws ServletException, IOException {
        String pattern = String.format("^Bearer %s$", JWTSessionConfiguration.JWT_PATTERN);
        Mockito.when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).headerValuePattern(pattern).build());

        Mockito.when(request.getHeader(configurationManager.getConfiguration().getHeaderName())).thenReturn("Bearer A5.notValidToken");
        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(sessionManager).setToken(null);
    }

    @Test
    public void attachTokenTest() throws ServletException, IOException {
        Mockito.when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).attachTokenToResponse(false).build());

        sessionFilter.doFilter(request, response, filterChain);

        Mockito.verify(request).getHeader(configurationManager.getConfiguration().getHeaderName());
        Mockito.verify(sessionManager).setToken(null);
        Mockito.verify(sessionManager, Mockito.never()).getToken();
        Mockito.verify(response, Mockito.never()).setHeader(configurationManager.getConfiguration().getHeaderName(), "testToken");
    }
}
