package pl.unak7.jwtee;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Local
public interface JWTSessionManager {
    Object get(String key, TypeReference typeReference) throws IOException, TokenNotFoundException;
    String getJSON(String key) throws IOException, TokenNotFoundException;
    Map<String, String> getJSONMap() throws IOException, TokenNotFoundException;
    void put(String key, Object value) throws IOException, TokenNotFoundException;
    String getToken() throws TokenNotFoundException;
    void setToken(String token) throws IOException;
}
