package lab.ia.ExpenseManagement.Services.Impl;

import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Repositories.UserRepository;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
        return UserPrincipal.build(user);
    }
}
