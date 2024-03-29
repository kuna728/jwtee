package pl.unak7.jwtee.samples.todo;

import com.auth0.jwt.algorithms.Algorithm;
import pl.unak7.jwtee.JWTSessionConfiguration;
import pl.unak7.jwtee.JWTSessionConfigurationManager;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.UnsupportedEncodingException;

@WebListener
public class ConfigurationServletContextListener implements ServletContextListener {

    @Inject
    JWTSessionConfigurationManager configurationManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            configurationManager.configure(JWTSessionConfiguration.builder()
                    .algorithm(Algorithm.HMAC256("secr3t"))
                    .build()
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
