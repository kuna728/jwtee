package pl.unak7.jwtee;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.Local;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@Local
public interface JWTSessionManager extends HttpSession {

    /**
     *
     * @param key              key from session map
     * @param typeReference    jackson type reference
     * @return                 value from session map with correct type
     * @param <T>              type which value should be deserialized
     */
    <T> T getAttribute(String key, TypeReference<T> typeReference);

    String getToken();

    void setToken(String token);

    @Override
    default String getId() {
        throw new NotImplementedException();
    }

    @Override
    default ServletContext getServletContext() {
        throw new NotImplementedException();
    }

    @Override
    default HttpSessionContext getSessionContext() {
        throw new NotImplementedException();
    }
}
