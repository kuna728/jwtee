package pl.unak7.jwtee;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
