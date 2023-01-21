package pl.unak7.jwtee;

import javax.ejb.Local;

/**
 * Interface which allows JWTee configuration management.
 */
@Local
public interface JWTSessionConfigurationManager {

    /**
     * Specifies global JWTee configuration object.
     * @param configuration Configuration object.
     * @see JWTSessionConfiguration
     */
    void configure(JWTSessionConfiguration configuration);

    /**
     * Retrieves global JWTee configuration object.
     * @return Returns configuration object.
     * @see JWTSessionConfiguration
     */
    JWTSessionConfiguration getConfiguration();
}
