package pl.unak7.jwtee;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Builder;
import lombok.Getter;

public class JWTSessionConfiguration {

    /**
     * JWT pattern constant.
     * Can be used to extracting token from header.
     */
    public final static String JWT_PATTERN = "(?<token>([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*))";

    private String headerName;
    private String headerValuePattern;
    private int maxInactiveInterval;
    private Algorithm algorithm;
    private boolean attachTokenToResponse;
    private boolean permitEmptySession;

    /**
     * Name of the header with token, default value is 'token'.
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Pattern for extracting jwt from request header, by default library expects header containing only token.
     * Default value is {@code '^JWT_TOKEN$'}
     * To use bearer scheme set this field to {@code '^Bearer '+JWT_TOKEN+'$'}.
     * @see  #JWT_PATTERN
     */
    public String getHeaderValuePattern() {
        return headerValuePattern;
    }

    /**
     *
     * @return
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public boolean isAttachTokenToResponse() {
        return attachTokenToResponse;
    }

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

    public static JWTSessionConfigurationBuilder builder() {
        return new JWTSessionConfigurationBuilder();
    }

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
