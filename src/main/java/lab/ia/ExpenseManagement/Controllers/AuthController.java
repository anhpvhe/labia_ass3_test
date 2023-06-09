package lab.ia.ExpenseManagement.Controllers;

import jakarta.validation.Valid;
import lab.ia.ExpenseManagement.Payloads.Request.LoginRequest;
import lab.ia.ExpenseManagement.Payloads.Request.RegisterRequest;
import lab.ia.ExpenseManagement.Payloads.Response.MessageResponse;
import lab.ia.ExpenseManagement.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


}
