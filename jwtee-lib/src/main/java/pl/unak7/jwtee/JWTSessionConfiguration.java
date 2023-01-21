package pl.unak7.jwtee;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpSession;

/**
 * JWTee configuration object.
 */
public class JWTSessionConfiguration {

    /**
     * JWT pattern constant.
     * Can be used for extracting token from header.
     * @see  #getHeaderValuePattern
     */
    public final static String JWT_PATTERN = "(?<token>([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*))";

    private String headerName;
    private String headerValuePattern;
    private int maxInactiveInterval;
    private Algorithm algorithm;
    private boolean attachTokenToResponse;
    private boolean permitEmptySession;

    /**
     * Name of the header with token.
     * Default value is {@code "token"}.
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Pattern for extracting jwt from request header.
     * Default value is JWT_PATTERN i.e. by default header must contain only token.
     * JWT_PATTERN may be used to modify this value so for example to use bearer scheme set this field to {@code "^Bearer " + JWT_TOKEN + "$"}.
     * @see  #JWT_PATTERN
     */
    public String getHeaderValuePattern() {
        return headerValuePattern;
    }

    /**
     * Default global value for maxInactiveInterval property.
     * This property can be also set for individual session via {@link JWTSessionManager}.
     * Value of zero or less indicates that the session will never time out.
     * Default value is 24 hours.
     * @see HttpSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /**
     * Algorithm which is used for token signing and token verification.
     * Default value is HMAC256 with random generated 300 chars secret.
     * New secret is generated each time the application starts, so it is strongly recommended to change this default behaviour.
     * With default randomized value all sessions will be invalidated after application restart.
     * @see Algorithm
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Indicates if token should be attached to response header.
     * If true token will be everytime attached to response header.
     * Default value is true.
     */
    public boolean isAttachTokenToResponse() {
        return attachTokenToResponse;
    }

    /**
     * Indicates if empty session is allowed.
     * If true method {@code getToken()} from {@link JWTSessionManager} returns token even if there is no object bind to session.
     * If false method {@code getToken()} from {@link JWTSessionManager} returns null if there is no object bind to session.
     * Default value is false.
     */
    public boolean isPermitEmptySession() {
        return permitEmptySession;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setHeaderValuePattern(String headerValuePattern) {
        this.headerValuePattern = headerValuePattern;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setAttachTokenToResponse(boolean attachTokenToResponse) {
        this.attachTokenToResponse = attachTokenToResponse;
    }

    public void setPermitEmptySession(boolean permitEmptySession) {
        this.permitEmptySession = permitEmptySession;
    }

    private JWTSessionConfiguration(JWTSessionConfigurationBuilder builder) {
        this.algorithm = builder.algorithm;
        this.headerName = builder.headerName;
        this.headerValuePattern = builder.headerValuePattern;
        this.maxInactiveInterval = builder.maxInactiveInterval;
        this.attachTokenToResponse = builder.attachTokenToResponse;
        this.permitEmptySession = builder.permitEmptySession;
    }

    /**
     * Returns builder object.
     */
    public static JWTSessionConfigurationBuilder builder() {
        return new JWTSessionConfigurationBuilder();
    }

    /**
     * Builder for JWTSessionConfiguration.
     * @see JWTSessionConfiguration
     */
    public static class JWTSessionConfigurationBuilder {

        private Algorithm algorithm;
        private String headerName = "token";
        private String headerValuePattern = "^" + JWT_PATTERN + "$";
        private int maxInactiveInterval = 24 * 60 * 60;
        private boolean attachTokenToResponse = true;
        private boolean permitEmptySession = true;

        public JWTSessionConfigurationBuilder algorithm(Algorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public JWTSessionConfigurationBuilder headerName(String headerName) {
            this.headerName = headerName;
            return this;
        }

        public JWTSessionConfigurationBuilder headerValuePattern(String headerValuePattern) {
            this.headerValuePattern = headerValuePattern;
            return this;
        }

        public JWTSessionConfigurationBuilder maxInactiveInterval(int maxInactiveInterval) {
            this.maxInactiveInterval = maxInactiveInterval;
            return this;
        }

        public JWTSessionConfigurationBuilder attachTokenToResponse(boolean attachTokenToResponse) {
            this.attachTokenToResponse = attachTokenToResponse;
            return this;
        }

        public JWTSessionConfigurationBuilder permitEmptySession(boolean permitEmptySession) {
            this.permitEmptySession = permitEmptySession;
            return this;
        }

        public JWTSessionConfiguration build() {
            return new JWTSessionConfiguration(this);
        }

    }
}
