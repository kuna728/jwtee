package pl.unak7.jwtee.samples.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String firstName;
    private Integer counter;
}
