package pl.unak7.jwtee;

import com.auth0.jwt.exceptions.JWTDecodeException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Local
public interface JWTSessionManager {
    public Object get(String key) throws IOException, TokenNotFoundException;
    public Map<String, Object> getMap() throws IOException, TokenNotFoundException;
    public void put(String key, Object value) throws IOException, TokenNotFoundException;
    public void initialize(HttpServletRequest request, HttpServletResponse response);
}
