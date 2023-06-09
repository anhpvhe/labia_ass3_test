package lab.ia.ExpenseManagement.Payloads.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private String username;

    private String fullname;

    private String email;

    private List<String> roles;
}
