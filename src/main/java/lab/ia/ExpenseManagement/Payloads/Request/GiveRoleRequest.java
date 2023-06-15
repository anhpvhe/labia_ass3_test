package lab.ia.ExpenseManagement.Payloads.Request;

import jakarta.validation.constraints.Size;
import lab.ia.ExpenseManagement.Models.Enums.ERole;
import lombok.Data;

@Data
public class GiveRoleRequest {
    @Size(max = 255)
    private String username;

    @Size(max = 60)
    private ERole role;
}
