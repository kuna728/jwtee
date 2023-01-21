package pl.unak7.jwtee;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@RequestScoped
public class JWTSessionManagerBean implements JWTSessionManager{

    private final static String INVALIDATED_SESSION_EXCEPTION_MESSAGE = "Session was invalidated";

    private final static TypeReference mapTypeReference = new TypeReference<Map<String, Object>>(){};

    @Inject
    JWTSessionConfigurationManager configurationManager;

    private Map<String, Object> sessionData;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private long creationTime;

    private long lastAccessedTime;

    private int maxInactiveInterval;

    private boolean isInvalidated = false;

    public JWTSessionManagerBean() { }

    @PostConstruct
    public void init() {
        setDefaults();
    }

    @Override
    public String getToken() {
        if(isInvalidated || (!configurationManager.getConfiguration().isPermitEmptySession() && sessionData.isEmpty()))
            return null;

        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withClaim("ct", creationTime);
        jwtBuilder.withClaim("lat", Instant.now().toEpochMilli());
        jwtBuilder.withClaim("mii", maxInactiveInterval);
        try {
            jwtBuilder.withClaim("session", objectMapper.writeValueAsString(sessionData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jwtBuilder.sign(configurationManager.getConfiguration().getAlgorithm());
    }

    @Override
    public void setToken(String token) {
        this.isInvalidated = false;
        if(token == null || token.isEmpty() || token.equals("null")) {
            setDefaults();
            return;
        }
        JWTVerifier verifier = JWT.require(configurationManager.getConfiguration().getAlgorithm()).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            String payload = new String(Base64.getDecoder().decode(jwt.getPayload()));
            Map<String, Object> payloadMap = objectMapper.readValue(payload, mapTypeReference);
            this.creationTime = (long) payloadMap.get("ct");
            this.lastAccessedTime = (long) payloadMap.get("lat");
            this.maxInactiveInterval = payloadMap.get("mii") != null ? (int) payloadMap.get("mii")
                    : configurationManager.getConfiguration().getMaxInactiveInterval();
            if(maxInactiveInterval > 0 && Instant.now().toEpochMilli() > lastAccessedTime + maxInactiveInterval * 1000L)
                throw new TokenExpiredException("");
            this.sessionData = payloadMap.get("session") == null ? new HashMap<>()
                    : objectMapper.readValue((String) payloadMap.get("session"), mapTypeReference);
        } catch (JWTVerificationException | IOException e) {
            setDefaults();
        }

    }

    @Override
    public <T> T getAttribute(String name, TypeReference<T> typeReference) {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        if(!sessionData.containsKey(name))
            return null;
        return objectMapper.convertValue(sessionData.get(name), typeReference);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getAttribute(String name) {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        return sessionData.get(name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }


    @Override
    public Enumeration<String> getAttributeNames() {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        return Collections.enumeration(sessionData.keySet());
    }

    @Override
    public String[] getValueNames() {
        return Collections.list(getAttributeNames()).toArray(new String[0]);
    }

    @Override
    public void setAttribute(String name, Object value) {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        sessionData.put(name, value);
        reloadData();
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        sessionData.remove(name);
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public long getCreationTime() {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        return creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public boolean isNew() {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        return lastAccessedTime == 0;
    }

    @Override
    public void invalidate() {
        if(isInvalidated)
            throw new IllegalStateException(INVALIDATED_SESSION_EXCEPTION_MESSAGE);
        isInvalidated = true;
    }

    private void setDefaults() {
        this.sessionData = new HashMap<>();
        this.creationTime = Date.from(Instant.now()).getTime();
        this.lastAccessedTime = 0;
        this.maxInactiveInterval = configurationManager.getConfiguration().getMaxInactiveInterval();
    }

    private void reloadData() {
        sessionData = objectMapper.convertValue(sessionData, mapTypeReference);
    }

}
