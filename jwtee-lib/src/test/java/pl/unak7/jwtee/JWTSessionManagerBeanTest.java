package pl.unak7.jwtee;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class JWTSessionManagerBeanTest {

    @Mock
    JWTSessionConfigurationManagerBean configurationManager;

    @InjectMocks
    JWTSessionManagerBean sessionManager;

    Utils utils = new Utils();

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        JWTSessionConfiguration configuration = JWTSessionConfiguration.builder().secret("secr4t")
                .algorithm(utils.getExampleAlgorithm()).build();
        Mockito.when(configurationManager.getConfiguration()).thenReturn(configuration);
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
        Assertions.assertEquals(inMap.get("session"), outMap.get("session"));
    }

    @Test
    public void getAttributeWithTypeReferenceTest() throws IOException {
        sessionManager.setToken(utils.generateExampleToken());

        Assertions.assertEquals("user", sessionManager.getAttribute("stringValue", new TypeReference<String>() {
        }));
        Assertions.assertEquals(true, sessionManager.getAttribute("booleanValue", new TypeReference<Boolean>() {
        }));
        Assertions.assertEquals(1.45f, sessionManager.getAttribute("floatValue", new TypeReference<Float>() {
        }));
        Assertions.assertEquals(1.45, sessionManager.getAttribute("floatValue", new TypeReference<Double>() {
        }));

        List<Utils.ExampleComplexType> complexValue = sessionManager.getAttribute("complexValue", new TypeReference<List<Utils.ExampleComplexType>>() {
        });
        Assertions.assertEquals("1", complexValue.get(0).getStringValue());
        Assertions.assertEquals(1, complexValue.get(0).getIntValue());
        Assertions.assertEquals("2", complexValue.get(1).getStringValue());
        Assertions.assertEquals(2, complexValue.get(1).getIntValue());

        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", new TypeReference<Integer>() {
        }));
        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", new TypeReference<List<Utils.ExampleComplexType>>() {
        }));
        Assertions.assertNull(sessionManager.getAttribute("notPresentValue", null));
        Assertions.assertNull(sessionManager.getAttribute("", null));
        Assertions.assertNull(sessionManager.getAttribute(null, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> sessionManager.getAttribute("stringValue", new TypeReference<Integer>() {
        }));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sessionManager.getAttribute("complexValue", new TypeReference<Utils.ExampleComplexType>() {
        }));
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
        Assertions.assertEquals(1672177452134L, sessionManager.getCreationTime());

        sessionManager.setToken(sessionManager.getToken());
        Assertions.assertEquals(1672177452134L, sessionManager.getCreationTime());

        sessionManager.setToken(null);
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(sessionManager.getCreationTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getCreationTime() > currentTime - 10);
    }

    @Test
    public void getLastAccessedTimeTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertEquals(1672177461043L, sessionManager.getLastAccessedTime());

        sessionManager.setToken(sessionManager.getToken());
        long currentTime = System.currentTimeMillis();
        Assertions.assertTrue(sessionManager.getLastAccessedTime() <= currentTime);
        Assertions.assertTrue(sessionManager.getLastAccessedTime() > currentTime - 10);

        sessionManager.setToken(null);
        Assertions.assertEquals(0, sessionManager.getLastAccessedTime());
    }

    @Test
    public void setMaxInactiveIntervalTest() {

    }

    @Test
    public void getMaxInactiveIntervalTest() {

    }

    @Test
    public void isNewTest() {
        sessionManager.setToken(utils.generateExampleToken());
        Assertions.assertFalse(sessionManager.isNew());

        sessionManager.setToken(null);
        Assertions.assertTrue(sessionManager.isNew());
    }

}
