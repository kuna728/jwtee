package pl.unak7.jwtee;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.ejb.Local;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Local
public interface JWTSessionManager extends HttpSession {
    Object get(String key, TypeReference typeReference) throws IOException, TokenNotFoundException;
    String getJSON(String key) throws IOException, TokenNotFoundException;
    Map<String, String> getJSONMap() throws IOException, TokenNotFoundException;
    void put(String key, Object value) throws IOException, TokenNotFoundException;
    default String remove(String key) {
        return null;
    }

    String getToken() throws TokenNotFoundException;
    void setToken(String token) throws IOException;

    default long getCreationTime() {
        return 0;
    }
    default long getLastAccessedTime() {
        return 0;
    }
    default int getMaxInactiveInterval() {
        return 0;
    }
    default void setMaxInactiveInterval(long maxInactiveInterval) {}
    default void invalidate() {}
    default boolean isNew() {
        return true;
    }
}
