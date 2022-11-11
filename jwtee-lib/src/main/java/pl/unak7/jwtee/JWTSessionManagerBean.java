package pl.unak7.jwtee;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RequestScoped
@Stateful
public class JWTSessionManagerBean implements JWTSessionManager{

//    @Inject
    private HttpServletRequest httpRequest;

//    @Inject
    private HttpServletResponse httpResponse;

    private Algorithm algorithm;

    private ObjectMapper objectMapper;

    public JWTSessionManagerBean() { }

    @PostConstruct
    void init() {
        try {
            this.algorithm = Algorithm.HMAC256("secr3t");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.objectMapper = new ObjectMapper();
        System.out.println("\nBLABLA\n");
    }

    @Override
    public void initialize(HttpServletRequest request, HttpServletResponse response) {
        this.httpRequest = request;
        this.httpResponse = response;
    }


    @Override
    public String get(String key) throws IOException {
        String token = getToken();
        DecodedJWT jwt = decodeJWT(token);
        String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        Map<String, String> objectMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, String>>(){});
        return objectMap.get(key);
    }

    @Override
    public Map<String, String> getMap() throws IOException {
        String token = getToken();
        DecodedJWT jwt = decodeJWT(token);
        String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(decodedPayload, new TypeReference<Map<String, String>>(){});
    }

    @Override
    public void put(String key, String value) throws IOException {
        JWTCreator.Builder jwtBuilder = JWT.create();
        try {
            String inToken = getToken();
            DecodedJWT jwt = decodeJWT(inToken);
            String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
            Map<String, String> objectMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, String>>() {
            });
            for (String objectKey : objectMap.keySet()) {
                jwtBuilder.withClaim(objectKey, objectMap.get(objectKey));
            }
        } catch(RuntimeException exception) { }
        String outToken = jwtBuilder.withClaim(key, value).sign(algorithm);
        httpResponse.setHeader("token", outToken);
    }

    private DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private String getToken() {
        String token = httpRequest.getHeader("token");
        if(token == null || token.isEmpty())
            throw new RuntimeException("Token not found in request");
        return token;
    }
}
