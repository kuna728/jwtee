package pl.unak7.jwtee;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JWTSessionConfiguration {
    @Builder.Default
    private String headerName = "token";

    @Builder.Default
    private int maxInactiveInterval = 24 * 60 * 60;

    private String secret;

    private Algorithm algorithm;

    @Builder.Default
    private boolean encryptToken = false;

    @Builder.Default
    private String encryptionSecret = null;

    @Builder.Default
    private boolean attachTokenToResponse = true;
}
