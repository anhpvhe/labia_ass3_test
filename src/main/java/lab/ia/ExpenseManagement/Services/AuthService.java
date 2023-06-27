package lab.ia.ExpenseManagement.Services;

import lab.ia.ExpenseManagement.Payloads.Request.LoginRequest;
import lab.ia.ExpenseManagement.Payloads.Request.RegisterRequest;
import lab.ia.ExpenseManagement.Payloads.Response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
}
