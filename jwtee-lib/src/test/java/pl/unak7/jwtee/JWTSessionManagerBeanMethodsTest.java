package pl.unak7.jwtee;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class JWTSessionManagerBeanMethodsTest {

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
    public void getTokenTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        sessionManager.setToken(utils.generateExampleToken());
        String token = sessionManager.getToken();
        long currentTime = Instant.now().toEpochMilli();

        JWTVerifier verifier = JWT.require(utils.getExampleAlgorithm()).build();
        DecodedJWT jwt = verifier.verify(token);
        String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>(){});
        Assertions.assertEquals(Utils.exampleCreationTime, payloadMap.get("ct"));
        Assertions.assertTrue((long) payloadMap.get("lat") <= currentTime);
        Assertions.assertTrue((long) payloadMap.get("lat") > currentTime - 10);
        Assertions.assertEquals(Utils.exampleMaxInactiveInterval, payloadMap.get("mii"));
        Assertions.assertNotNull(payloadMap.get("session"));
    }

    @Test
    public void setTokenTest() {
        sessionManager.setToken(null);
        Assertions.assertEquals(0, sessionManager.getValueNames().length);

        sessionManager.setToken("");
        Assertions.assertEquals(0, sessionManager.getValueNames().length);

        sessionManager.setToken("null");
        Assertions.assertEquals(0, sessionManager.getValueNames().length);

        sessionManager.setToken(utils.generateToken(utils.getExampleAlgorithm(), Utils.exampleCreationTime,
                Utils.exampleLastAccessedTime, Utils.exampleMaxInactiveInterval, new HashMap<>()));
        Assertions.assertEquals(0, sessionManager.getValueNames().length);
    }

    @Test
    public void getAttributeWithTypeReferenceTest() throws IOException {
        sessionManager.setToken(utils.generateExampleToken());

        Assertions.assertEquals("user", sessionManager.getAttribute("stringValue", new TypeReference<String>() {}));
        Assertions.assertEquals(true, sessionManager.getAttribute("booleanValue", new TypeReference<Boolean>() {}));
        Assertions.assertEquals(1.45f, sessionManager.getAttribute("floatValue", new TypeReference<Float>() {}));
        Assertions.assertEquals(1.45, sessionManager.getAttribute("floatValue", new TypeReference<Double>() {}));

        List<Utils.ExampleComplexType> complexValue = sessionManager.getAttribute("complexValue", new TypeReference<List<Utils.ExampleComplexType>>() {});
        Assertions.assertEquals("1", complexValue.get(0).getStringValue());
        Assertions.assertEquals(1, complexValue.get(0).getIntValue());
        Assertions.assertEquals("2", complexValue.get(1).getStringValue());
        Assertions.assertEquals(2, complexValue.get(1).getIntValue());

        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", new TypeReference<Integer>() {}));
        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", new TypeReference<List<Utils.ExampleComplexType>>() {}));
        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", null));
        Assertions.assertNull(sessionManager.getAttribute("", null));
        Assertions.assertNull(sessionManager.getAttribute(null, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> sessionManager.getAttribute("stringValue", new TypeReference<Integer>() {}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sessionManager.getAttribute("complexValue", new TypeReference<Utils.ExampleComplexType>() {}));
    }

    @Test
    public void getAttributeTest() {
        sessionManager.setToken(utils.generateExampleToken());

        Assertions.assertEquals("user", sessionManager.getAttribute("stringValue"));
        Assertions.assertEquals(true, sessionManager.getAttribute("booleanValue"));
        Assertions.assertEquals(1.45, sessionManager.getAttribute("floatValue"));

        Assertions.assertTrue(List.class.isAssignableFrom(sessionManager.getAttribute("complexValue").getClass()));
        List<Map<String, Object>> complexValue = (List<Map<String, Object>>) sessionManager.getAttribute("complexValue");
        Assertions.assertEquals("1", complexValue.get(0).get("stringValue"));
        Assertions.assertEquals(1, complexValue.get(0).get("intValue"));
        Assertions.assertEquals("2", complexValue.get(1).get("stringValue"));
        Assertions.assertEquals(2, complexValue.get(1).get("intValue"));

        Assertions.assertNull(sessionManager.getAttribute("notPresentValue"));
        Assertions.assertNull(sessionManager.getAttribute(""));
        Assertions.assertNull(sessionManager.getAttribute(null));
    }

    @Test
    public void getValueTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertEquals("user", sessionManager.getValue("stringValue"));
    }

    @Test
    public void getAttributeNamesTest() {
        sessionManager.setToken(utils.generateExampleToken());
        testKeys(Collections.list(sessionManager.getAttributeNames()).toArray(new String[0]));
    }

    @Test
    public void getValueNamesTest() {
        sessionManager.setToken(utils.generateExampleToken());
        testKeys(sessionManager.getValueNames());
    }

    private void testKeys(String[] attributesNames) {
        Arrays.sort(attributesNames);
        Assertions.assertEquals(4, attributesNames.length);
        Assertions.assertArrayEquals(new String[]{"booleanValue", "complexValue", "floatValue", "stringValue"}, attributesNames);
    }

    @Test
    public void setAttributeTest() {
        sessionManager.setToken(utils.generateExampleToken());

        sessionManager.setAttribute("attribute1", 12);
        Assertions.assertEquals(12, sessionManager.getAttribute("attribute1"));

        sessionManager.setAttribute("attribute2", new Utils.ExampleComplexType("str", 11));
        Map<String, Object> item = (Map<String, Object>) sessionManager.getAttribute("attribute2");
        Assertions.assertEquals(11, item.get("intValue"));
        Assertions.assertEquals("str", item.get("stringValue"));

        sessionManager.setAttribute("attribute3", Arrays.asList(new Utils.ExampleComplexType("str", 11)));
        List<Map<String, Object>> items = (List<Map<String, Object>>) sessionManager.getAttribute("attribute3");
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(11, items.get(0).get("intValue"));
        Assertions.assertEquals("str", items.get(0).get("stringValue"));

        Assertions.assertEquals(7, sessionManager.getValueNames().length);
    }

    @Test
    public void putValueTest() {
        sessionManager.setToken(utils.generateExampleToken());

        sessionManager.putValue("attribute1", 12);
        Assertions.assertEquals(12, sessionManager.getAttribute("attribute1"));
    }

    @Test
    public void removeAttributeTest() {
        sessionManager.setToken(utils.generateExampleToken());

        sessionManager.removeAttribute("booleanValue");
        Assertions.assertEquals(3, sessionManager.getValueNames().length);
        Assertions.assertNull(sessionManager.getAttribute("booleanValue"));
    }

    @Test
    public void getCreationTimeTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertEquals(Utils.exampleCreationTime, sessionManager.getCreationTime());

        sessionManager.setToken(sessionManager.getToken());
        Assertions.assertEquals(Utils.exampleCreationTime, sessionManager.getCreationTime());

        sessionManager.setToken(null);
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(sessionManager.getCreationTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getCreationTime() > currentTime - 10);
    }

    @Test
    public void getLastAccessedTimeTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertEquals(Utils.exampleLastAccessedTime, sessionManager.getLastAccessedTime());

        sessionManager.setToken(sessionManager.getToken());
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(sessionManager.getLastAccessedTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getLastAccessedTime() > currentTime - 10);

        sessionManager.setToken(null);
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
    }

    @Test
    public void setMaxInactiveIntervalTest() {
        final int MAX_INACTIVE_INTERVAL = 6 * 60 * 60;

        sessionManager.setToken(utils.generateExampleToken());
        sessionManager.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
        Assertions.assertEquals(MAX_INACTIVE_INTERVAL, sessionManager.getMaxInactiveInterval());

        sessionManager.setToken(sessionManager.getToken());
        Assertions.assertEquals(MAX_INACTIVE_INTERVAL, sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void getMaxInactiveIntervalTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertEquals(Utils.exampleMaxInactiveInterval, sessionManager.getMaxInactiveInterval());

        sessionManager.setToken(null);
        Assertions.assertEquals(configurationManager.getConfiguration().getMaxInactiveInterval(), sessionManager.getMaxInactiveInterval());
    }

    @Test
    public void isNewTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertFalse(sessionManager.isNew());

        sessionManager.setToken(null);
        Assertions.assertTrue(sessionManager.isNew());
    }

    @Test
    public void invalidateTest() {
        sessionManager.setToken(utils.generateExampleToken());
        sessionManager.invalidate();
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getAttribute("stringValue", new TypeReference<String>() {}));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getAttribute("stringValue"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getValue("stringValue"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getAttributeNames());
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getValueNames());
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.setAttribute("stringValue", "2"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.putValue("stringValue", "2"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.removeAttribute("stringValue"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.removeValue("stringValue"));
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getCreationTime());
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.getLastAccessedTime());
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.isNew());
        Assertions.assertThrows(IllegalStateException.class, () -> sessionManager.invalidate());
    }

}
