package pl.unak7.jwtee;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Local
public interface JWTSessionManager {
    public String get(String key) throws IOException;
    public Map<String, String> getMap() throws IOException;
    public void put(String key, String value) throws IOException;
    public void initialize(HttpServletRequest request, HttpServletResponse response);
}
