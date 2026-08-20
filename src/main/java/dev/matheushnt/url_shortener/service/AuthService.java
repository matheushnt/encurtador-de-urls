package dev.matheushnt.url_shortener.service;

import dev.matheushnt.url_shortener.dto.SignUpRequest;
import dev.matheushnt.url_shortener.exception.ResourceFoundException;
import dev.matheushnt.url_shortener.model.User;
import dev.matheushnt.url_shortener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    public User signUp(SignUpRequest signUpRequest) {
        Optional<User> existingUser = this.userRepository.findByEmail(signUpRequest.email());

        if (existingUser.isPresent()) {
            throw new ResourceFoundException("E-mail já cadastrado");
        }

        String encryptedPassword = this.passwordEncoder.encode(signUpRequest.password());
        User newUser = User.create(signUpRequest.fullName(), signUpRequest.email(), encryptedPassword, signUpRequest.role());

        return this.userRepository.save(newUser);
    }
}
