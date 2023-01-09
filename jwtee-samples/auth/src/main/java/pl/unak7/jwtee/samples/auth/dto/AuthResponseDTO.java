package pl.unak7.jwtee.samples.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private boolean success;
    private String token;
    private String firstName;
    private Integer counter;
}
