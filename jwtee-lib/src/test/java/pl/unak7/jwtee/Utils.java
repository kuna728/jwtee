package pl.unak7.jwtee;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static final String exampleSecret = "secr4t";
    public static final long exampleCreationTime = Instant.now().minusSeconds(4*60*60).toEpochMilli();
    public static final long exampleLastAccessedTime = Instant.now().minusSeconds(60*60).toEpochMilli();
    public static final int exampleMaxInactiveInterval = 12 * 60 * 60;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(Algorithm algorithm, long creationTime, long lastAccessedTime, int maxInactiveInterval, Map<String, Object> payload) {
        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withClaim("ct", creationTime);
        jwtBuilder.withClaim("lat", lastAccessedTime);
        jwtBuilder.withClaim("mii", maxInactiveInterval);
        try {
            String pl = objectMapper.writeValueAsString(payload);
            jwtBuilder.withClaim("session", objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jwtBuilder.sign(algorithm);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExampleComplexType {
        private String stringValue;
        private int intValue;
    }

    public Map<String, Object> getExampleSession() {
        Map<String, Object> session = new HashMap<>();
        session.put("stringValue", "user");
        session.put("booleanValue", true);
        session.put("floatValue", 1.45f);
        session.put("complexValue", Arrays.asList(new ExampleComplexType("1", 1), new ExampleComplexType("2", 2)));
        return session;
    }

    public Algorithm getExampleAlgorithm() {
        try {
            return Algorithm.HMAC256(exampleSecret);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateExampleToken() {
        return generateToken(getExampleAlgorithm(), exampleCreationTime, exampleLastAccessedTime, exampleMaxInactiveInterval, getExampleSession());
    }

    public Map<String, Object> getPayloadFromToken(String token, Algorithm algorithm) throws IOException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(payload, new TypeReference<Map<String, Object>>(){});
    }

    public Map<String, Object> getPayloadFromToken(String token) throws IOException {
        return getPayloadFromToken(token, getExampleAlgorithm());
    }
}
