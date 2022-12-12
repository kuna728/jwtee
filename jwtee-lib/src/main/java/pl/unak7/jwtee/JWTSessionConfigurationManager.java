package pl.unak7.jwtee;

import javax.ejb.Local;

@Local
public interface JWTSessionConfigurationManager {
    void configure(JWTSessionConfiguration configuration);

    JWTSessionConfiguration getConfiguration();
}
