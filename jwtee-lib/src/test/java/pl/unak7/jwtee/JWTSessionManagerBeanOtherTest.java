package pl.unak7.jwtee;

import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class JWTSessionManagerBeanOtherTest {

    @Mock
    JWTSessionConfigurationManagerBean configurationManager;

    @InjectMocks
    JWTSessionManagerBean sessionManager;

    Utils utils = new Utils();

    @BeforeEach
    public void setup() {
        Mockito.lenient().when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).build());
        sessionManager.init();
    }

    @Test
    public void notInitializedWithTokenTest() {
        long currentTime = Instant.now().toEpochMilli();

        Assertions.assertNotNull(sessionManager.getToken());
        Assertions.assertNull(sessionManager.getAttribute("test"));
        Assertions.assertTrue(sessionManager.getCreationTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getCreationTime() > currentTime - 10);
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
        Assertions.assertEquals(configurationManager.getConfiguration().getMaxInactiveInterval(), sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void getTokenPermitEmptySessionTrueTest() {
        Assertions.assertNotNull(sessionManager.getToken());

        sessionManager.setToken(utils.generateToken(utils.getExampleAlgorithm(), Utils.exampleCreationTime, Utils.exampleLastAccessedTime,
                Utils.exampleMaxInactiveInterval, new HashMap<>()));
        Assertions.assertNotNull(sessionManager.getToken());
    }

    @Test
    public void getTokenPermitEmptySessionFalseTest() {
        Mockito.lenient().when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).permitEmptySession(false).build());

        Assertions.assertNull(sessionManager.getToken());

        sessionManager.setToken(utils.generateToken(utils.getExampleAlgorithm(), Utils.exampleCreationTime, Utils.exampleLastAccessedTime,
                Utils.exampleMaxInactiveInterval, new HashMap<>()));
        Assertions.assertNull(sessionManager.getToken());
    }

    @Test
    public void setGetTokenTest() throws IOException {
        String inToken = utils.generateExampleToken();
        Map<String, Object> inMap = utils.getPayloadFromToken(inToken);
        sessionManager.setToken(inToken);
        String outToken = sessionManager.getToken();
        Map<String, Object> outMap = utils.getPayloadFromToken(outToken);

        Assertions.assertNotEquals(inToken, outToken);
        Assertions.assertEquals(inMap.get("ct"), outMap.get("ct"));
        Assertions.assertNotEquals(inMap.get("lat"), outMap.get("lat"));
        Assertions.assertEquals(inMap.get("mii"), outMap.get("mii"));
        Assertions.assertEquals(inMap.get("session"), outMap.get("session"));
    }

    @Test
    public void expiredTest() {
        sessionManager.setToken(utils.generateToken(utils.getExampleAlgorithm(), Utils.exampleCreationTime,
                Utils.exampleLastAccessedTime, 60, utils.getExampleSession()));
        long currentTime = Instant.now().toEpochMilli();
        Assertions.assertTrue( sessionManager.getCreationTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getCreationTime() > currentTime - 10);
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
        Assertions.assertEquals(configurationManager.getConfiguration().getMaxInactiveInterval(), sessionManager.getMaxInactiveInterval());
        Assertions.assertEquals(0, sessionManager.getValueNames().length);
    }

    @Test
    public void customMaxInactiveIntervalTest() {
        Mockito.lenient().when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).maxInactiveInterval(20).build());
        sessionManager.init();

        Assertions.assertEquals(20, sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void customAlgorithmTest() throws UnsupportedEncodingException {
        Algorithm customAlgorithm = Algorithm.HMAC512("secret");
        String token = utils.generateToken(customAlgorithm, Utils.exampleCreationTime, Utils.exampleLastAccessedTime,
                Utils.exampleMaxInactiveInterval, new HashMap<>());

        Mockito.lenient().when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(Algorithm.HMAC512("secret")).build());
        sessionManager.setToken(token);
        Assertions.assertEquals(Utils.exampleLastAccessedTime, sessionManager.getLastAccessedTime());

        Mockito.lenient().when(configurationManager.getConfiguration()).thenReturn(
                JWTSessionConfiguration.builder().algorithm(utils.getExampleAlgorithm()).build());
        sessionManager.setToken(token);
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
    }

    @Test
    public void tokenAbuseTest() { //TODO
        String token = utils.generateExampleToken();
        String[] parts = token.split("\\.");
        String decodedPayload = new String(Base64.getDecoder().decode(parts[1]));
        String abusedPayload = decodedPayload.replace("\\\"stringValue\\\":\\\"user\\\"", "\\\"stringValue\\\":\\\"admin\\\"");
        String abusedToken = String.join(".", parts[0], Base64.getEncoder().encodeToString(abusedPayload.getBytes()), parts[2]);
        sessionManager.setToken(abusedToken);

        Assertions.assertNull(sessionManager.getAttribute("stringValue"));
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
    }
}
