package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.Exceptions.AccessDeniedException;
import lab.ia.ExpenseManagement.Exceptions.BadRequestException;
import lab.ia.ExpenseManagement.Models.Enums.ERole;
import lab.ia.ExpenseManagement.Models.Role;
import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Payloads.Request.LoginRequest;
import lab.ia.ExpenseManagement.Payloads.Request.RegisterRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.JwtResponse;
import lab.ia.ExpenseManagement.Repositories.RoleRepository;
import lab.ia.ExpenseManagement.Repositories.UserRepository;
import lab.ia.ExpenseManagement.Security.JWT.JwtUtils;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest loginRequest){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception exception) {
            throw new AccessDeniedException(new ApiResponse(false, "Wrong username or password!"));
        }
//        if (!authentication.isAuthenticated())
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return new JwtResponse(
                jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public void register(RegisterRequest registerRequest){
        //Check if user already existed by username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException(new ApiResponse(false, "Username is already taken!"));
        }

        //Check if user already existed by email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException(new ApiResponse(false, "Email is already taken!"));
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getFullname(),registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()));
        Set<String> requestRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                      .orElseThrow(() -> new RuntimeException("User role is not found!"));
        if (requestRoles == null) {
            roles.add(userRole);
        } else {
            requestRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Admin role is not found!"));
                    roles.add(adminRole);
                } else {
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
