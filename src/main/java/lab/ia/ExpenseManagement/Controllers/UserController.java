package lab.ia.ExpenseManagement.Controllers;

import jakarta.validation.Valid;
import lab.ia.ExpenseManagement.Payloads.Request.UserInfoRequest;
import lab.ia.ExpenseManagement.Security.CurrentUser;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(userService.getCurrentUserInfo(currentUser));
    }

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.checkUsernameAvailable(username));
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.checkEmailAvailable(email));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username,
                                        @Valid @RequestBody UserInfoRequest newUserInfo) {
        return ResponseEntity.ok(userService.updateUser(username, newUserInfo));
    }

}
