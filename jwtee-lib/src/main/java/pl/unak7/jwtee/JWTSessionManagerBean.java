package pl.unak7.jwtee;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
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
    }

    @Override
    public void initialize(HttpServletRequest request, HttpServletResponse response) {
        this.httpRequest = request;
        this.httpResponse = response;
    }


    @Override
    public Object get(String key) throws IOException, TokenNotFoundException {
        String token = getToken();
        DecodedJWT jwt = decodeJWT(token);
        String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        Map<String, Object> objectMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>(){});
        return objectMap.get(key);
    }

    @Override
    public Map<String, Object> getMap() throws IOException, TokenNotFoundException {
        String token = getToken();
        DecodedJWT jwt = decodeJWT(token);
        String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
        return objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>(){});
    }

    @Override
    public void put(String key, Object value) throws IOException, TokenNotFoundException {
        JWTCreator.Builder jwtBuilder = JWT.create();
        try {
            String inToken = getToken();
            DecodedJWT jwt = decodeJWT(inToken);
            String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
            Map<String, String> objectMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>() {
            });
            for (String objectKey : objectMap.keySet()) {
                jwtBuilder.withClaim(objectKey, objectMap.get(objectKey));
            }
        } catch(TokenNotFoundException exception) { }
        String outToken = jwtBuilder.withClaim(key, objectMapper.writeValueAsString(value)).sign(algorithm);
        httpResponse.setHeader("token", outToken);
    }

    private DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private String getToken() throws TokenNotFoundException{
        String token = httpRequest.getHeader("token");
        if(token == null || token.isEmpty() || token.equals("null"))
            throw new TokenNotFoundException("Token not found in request");
        return token;
    }
}
