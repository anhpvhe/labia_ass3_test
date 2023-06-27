package lab.ia.ExpenseManagement.Payloads.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserIdentityAvailabilityResponse {
    private Boolean available;
}
