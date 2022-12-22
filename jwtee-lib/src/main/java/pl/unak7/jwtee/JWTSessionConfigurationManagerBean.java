package pl.unak7.jwtee;


import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import java.io.UnsupportedEncodingException;

@ApplicationScoped
@Stateful
public class JWTSessionConfigurationManagerBean implements JWTSessionConfigurationManager{
    private JWTSessionConfiguration configuration = null;

    @PostConstruct
    void init() {
        configuration = JWTSessionConfiguration.builder().build();
    }

    @Override
    public void configure(JWTSessionConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JWTSessionConfiguration getConfiguration() {
        return buildConfigurationWithDefaults();
    }

    private JWTSessionConfiguration buildConfigurationWithDefaults() {
        JWTSessionConfiguration.JWTSessionConfigurationBuilder builder = JWTSessionConfiguration.builder();
        String secret = configuration.getSecret() == null || configuration.getSecret().isEmpty() ?
                RandomStringUtils.random(300) : configuration.getSecret();

        builder.headerName(configuration.getHeaderName())
                .encryptToken(configuration.isEncryptToken())
                .encryptionSecret(configuration.getEncryptionSecret())
                .attachTokenToResponse(configuration.isAttachTokenToResponse())
                .secret(secret);
        try {
            builder.algorithm(configuration.getAlgorithm() == null ?
                    Algorithm.HMAC256(secret) : configuration.getAlgorithm());
        } catch (UnsupportedEncodingException e) { }
        return builder.build();
    }

}
