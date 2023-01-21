package pl.unak7.jwtee;


import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.RandomStringUtils;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;;
import java.io.UnsupportedEncodingException;

/**
 * Bean which stores JWTee configuration in global application scoped state and allows user to manage the configuration.
 */
@Singleton
public class JWTSessionConfigurationManagerBean implements JWTSessionConfigurationManager{
    private JWTSessionConfiguration configuration = JWTSessionConfiguration.builder().build();
    private final String randomSecret = RandomStringUtils.random(300);

    @Override
    public void configure(JWTSessionConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JWTSessionConfiguration getConfiguration() {
        if(configuration.getAlgorithm() == null) {
            try {
                configuration.setAlgorithm(Algorithm.HMAC256(randomSecret));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return configuration;
    }
}
