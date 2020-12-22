package com.kakuom.finaltipping.security;

import com.kakuom.finaltipping.dto.AuthDTO;
import com.kakuom.finaltipping.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthDTO user = userRepository.loadByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("username not found"));

        return new UserPrincipal(user);
    }
}
