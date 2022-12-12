package pl.unak7.jwtee;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.*;
import javax.annotation.PostConstruct;


@RequestScoped
@Stateful
public class JWTSessionManagerBean implements JWTSessionManager{

    @Inject
    JWTSessionConfigurationManager configurationManager;

    private Map<String, String> jsonMap;

    private ObjectMapper objectMapper;

    public JWTSessionManagerBean() { }

    @PostConstruct
    void init() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Object get(String key, TypeReference typeReference) throws IOException, TokenNotFoundException {
        if(!jsonMap.containsKey(key))
            return null;
        return objectMapper.readValue(jsonMap.get(key), typeReference);
    }

    @Override
    public String getJSON(String key) throws IOException, TokenNotFoundException {
        return jsonMap.get(key);
    }

    @Override
    public Map<String, String> getJSONMap() {
        return jsonMap;
    }

    @Override
    public void put(String key, Object value) throws IOException {
        jsonMap.put(key, objectMapper.writeValueAsString(value));
    }

    @Override
    public String getToken() {
        JWTCreator.Builder jwtBuilder = JWT.create();
        for (String objectKey : jsonMap.keySet()) {
            jwtBuilder.withClaim(objectKey, jsonMap.get(objectKey));
        }
        return jwtBuilder.sign(configurationManager.getConfiguration().getAlgorithm());
    }

    @Override
    public void setToken(String token) throws IOException {
        if(token == null || token.isEmpty() || token.equals("null")) {
            this.jsonMap = new HashMap<>();
            return;
        }
        JWTVerifier verifier = JWT.require(configurationManager.getConfiguration().getAlgorithm()).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
            this.jsonMap = objectMapper.readValue(payload, new TypeReference<Map<String, String>>(){});
        } catch (JWTVerificationException e) {
            this.jsonMap = new HashMap<>();
        }

    }

}
