package dev.matheushnt.url_shortener.controller;

import dev.matheushnt.url_shortener.documentation.AuthControllerApi;
import dev.matheushnt.url_shortener.dto.AccessToken;
import dev.matheushnt.url_shortener.dto.SignInRequest;
import dev.matheushnt.url_shortener.dto.SignUpRequest;
import dev.matheushnt.url_shortener.model.User;
import dev.matheushnt.url_shortener.provider.TokenProvider;
import dev.matheushnt.url_shortener.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerApi {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/sign-up")
    @Override
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        this.authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    @Override
    public ResponseEntity<AccessToken> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password());
        Authentication authUser = this.authenticationManager.authenticate(auth);
        String accessToken = this.tokenProvider.generateAccessToken((User) authUser.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new AccessToken(accessToken));
    }

}
