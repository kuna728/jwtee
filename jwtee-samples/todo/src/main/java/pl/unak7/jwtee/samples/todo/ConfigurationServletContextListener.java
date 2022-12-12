package pl.unak7.jwtee.samples.todo;

import pl.unak7.jwtee.JWTSessionConfiguration;
import pl.unak7.jwtee.JWTSessionConfigurationManager;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConfigurationServletContextListener implements ServletContextListener {

    @Inject
    JWTSessionConfigurationManager configurationManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        configurationManager.configure(JWTSessionConfiguration.builder()
                .secret("secr3t")
                .build()
        );
    }
}
