package pl.unak7.jwtee;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.Local;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * Interface enabling client-side session management.
 */
@Local
public interface JWTSessionManager extends HttpSession {

    /**
     * Returns deserialized object bound with the specified name in this session. Object is deserialized into type specified by typeReference param.
     * Returns null if no object is bound under the name.
     * @param name             a string specifying the name of the object
     * @param typeReference    a {@link TypeReference} indicating the type to which the object should be deserialized
     * @return                 the object with the specified name deserialized into type T
     * @param <T>              target deserialization type
     * @throws                 IllegalStateException if this method is called on an invalidated session
     * @throws                 IllegalArgumentException if the object cannot be deserialized to the specified type
     * @see TypeReference
     */
    <T> T getAttribute(String name, TypeReference<T> typeReference);

    /**
     * Prepares and returns a token containing session data and metadata.
     * Session data are attributes explicitly placed in the session.
     * Session metadata are attributes defined by the {@link HttpSession} interface and automatically placed in the session i.e. creation time, last accessed time and max inactive interval.
     * Method is mainly used by the {@link JWTSessionFilter}, however, library clients can use this method to, for example, implement a bearer scheme.
     * Returns null if this method is called on an invalidated session.
     * @return a session token string
     */
    String getToken();

    /**
     * Initializes manager with session data and metadata retrieved from the token.
     * Initializes manager with defaults if token is null, empty, invalid or expired.
     * Method is mainly used by the {@link JWTSessionFilter}, however, library clients can use this method to, for example, start a new session after invalidating the previous one.
     * @param token a session token string
     */
    void setToken(String token);

    /**
     * Method is not implemented. An attempt to call it will end up throwing a NotImplementedException.
     * @throws NotImplementedException each time it is called
     */
    @Override
    default String getId() {
        throw new NotImplementedException();
    }

    /**
     * Method is not implemented. An attempt to call it will end up throwing a NotImplementedException.
     * @throws NotImplementedException each time it is called
     */
    @Override
    default ServletContext getServletContext() {
        throw new NotImplementedException();
    }

    /**
     * Method is not implemented. An attempt to call it will end up throwing a NotImplementedException.
     * @throws NotImplementedException each time it is called
     */
    @Override
    default HttpSessionContext getSessionContext() {
        throw new NotImplementedException();
    }
}
