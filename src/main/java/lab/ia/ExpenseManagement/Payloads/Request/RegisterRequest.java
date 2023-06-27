package lab.ia.ExpenseManagement.Payloads.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String fullname;

    @NotBlank
    @Size(max = 60)
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    private Set<String> roles;
}
