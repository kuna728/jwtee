package pl.unak7.jwtee;

import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

public class JWTSessionConfigurationManagerBeanTest {

    JWTSessionConfigurationManagerBean configurationManager = new JWTSessionConfigurationManagerBean();

    @Test
    public void headerNameTest() {
        Assertions.assertEquals("token", configurationManager.getConfiguration().getHeaderName());

        configurationManager.configure(JWTSessionConfiguration.builder().headerName("auth").build());
        Assertions.assertEquals("auth", configurationManager.getConfiguration().getHeaderName());
    }

    @Test
    public void headerValuePatternTest() {
        Assertions.assertEquals(String.format("^%s$", JWTSessionConfiguration.JWT_PATTERN),
                configurationManager.getConfiguration().getHeaderValuePattern());

        String newHeaderPattern = String.format("^Bearer %s$", JWTSessionConfiguration.JWT_PATTERN);
        configurationManager.configure(JWTSessionConfiguration.builder().headerValuePattern(newHeaderPattern).build());
        Assertions.assertEquals(newHeaderPattern, configurationManager.getConfiguration().getHeaderValuePattern());
    }
    @Test
    public void maxInactiveIntervalTest() {
        Assertions.assertEquals(24 * 60 * 60, configurationManager.getConfiguration().getMaxInactiveInterval());

        configurationManager.configure(JWTSessionConfiguration.builder().maxInactiveInterval(60).build());
        Assertions.assertEquals(60, configurationManager.getConfiguration().getMaxInactiveInterval());
    }

    @Test
    public void algorithmTest() throws UnsupportedEncodingException {
        Assertions.assertEquals("HS256", configurationManager.getConfiguration().getAlgorithm().getName());

        configurationManager.configure(JWTSessionConfiguration.builder().algorithm(Algorithm.HMAC512("secr4t")).build());
        Assertions.assertEquals("HS512", configurationManager.getConfiguration().getAlgorithm().getName());
    }

    @Test
    public void attachTokenToResponseTest() {
        Assertions.assertTrue(configurationManager.getConfiguration().isAttachTokenToResponse());

        configurationManager.configure(JWTSessionConfiguration.builder().attachTokenToResponse(false).build());
        Assertions.assertFalse(configurationManager.getConfiguration().isAttachTokenToResponse());
    }

    @Test
    public void permitEmptySessionTest() {
        Assertions.assertTrue(configurationManager.getConfiguration().isPermitEmptySession());

        configurationManager.configure(JWTSessionConfiguration.builder().permitEmptySession(false).build());
        Assertions.assertFalse(configurationManager.getConfiguration().isPermitEmptySession());
    }
}
