package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.Exceptions.BadRequestException;
import lab.ia.ExpenseManagement.Models.Enums.ERole;
import lab.ia.ExpenseManagement.Models.Role;
import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Payloads.Request.UserInfoRequest;
import lab.ia.ExpenseManagement.Payloads.Response.ApiResponse;
import lab.ia.ExpenseManagement.Payloads.Response.UserIdentityAvailabilityResponse;
import lab.ia.ExpenseManagement.Payloads.Response.UserResponse;
import lab.ia.ExpenseManagement.Repositories.RoleRepository;
import lab.ia.ExpenseManagement.Repositories.UserRepository;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import lab.ia.ExpenseManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServerImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getCurrentUserInfo(UserPrincipal currentUser) {
        List<String> roles = currentUser.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return new UserResponse(currentUser.getUsername(), currentUser.getFullName(), currentUser.getEmail(), roles);
    }

    @Override
    public UserIdentityAvailabilityResponse checkUsernameAvailable(String username) {
        boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailabilityResponse(isAvailable);
    }

    @Override
    public UserIdentityAvailabilityResponse checkEmailAvailable(String email) {
        boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailabilityResponse(isAvailable);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @PreAuthorize("#username == principal.username || hasRole('ADMIN')")
    @Override
    public ApiResponse updateUser(String username, UserInfoRequest newUserInfo) {
        User user = userRepository.getUserByUsername(username);
        user.setFullname(newUserInfo.getFullname());
        if (!user.getEmail().equals(newUserInfo.getEmail()) && userRepository.existsByEmail(newUserInfo.getEmail()))
            throw new BadRequestException(new ApiResponse(false, "Email is already taken!"));
        user.setEmail(newUserInfo.getEmail());
        userRepository.save(user);
        return new ApiResponse(true, String.format("User %s updated successfully!", username));
    }

    @PreAuthorize("#username == principal.username || hasRole('ADMIN')")
    @Override
    public ApiResponse deleteUser(String username) {
        User user = userRepository.getUserByUsername(username);
        userRepository.deleteById(user.getId());
        return new ApiResponse(true, String.format("User %s deleted successfully!", username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ApiResponse giveRoleByUsername(String username, ERole role) {
        User user = userRepository.getUserByUsername(username);
        Set<Role> roles = user.getRoles();
        Role newRole = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("User role is not found!"));
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);
        return new ApiResponse(true, String.format("Added role %s to user %s", role.toString(), username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ApiResponse takeRoleByUsername(String username, ERole role) {
        User user = userRepository.getUserByUsername(username);
        Set<Role> roles = user.getRoles();
        Role newRole = roleRepository.findByName(role).orElseThrow(() -> new RuntimeException("User role is not found!"));
        roles.remove(newRole);
        user.setRoles(roles);
        userRepository.save(user);
        return new ApiResponse(true, String.format("Taken role %s from user %s", role.toString(), username));
    }
}
