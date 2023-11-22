package be.equals.authservice.controller;

import be.equals.authservice.controller.response.TokenResponse;
import be.equals.authservice.domain.User;
import be.equals.authservice.exception.UserNotFoundException;
import be.equals.authservice.repository.UserRepository;
import be.equals.authservice.controller.request.UserLoginRequest;
import be.equals.authservice.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/token")
public class AuthRestController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthRestController(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    public ResponseEntity<TokenResponse> getToken(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        final User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(userLoginRequest.getEmail()));

        if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(TokenResponse.builder()
                    .token(jwtUtils.generateToken(user))
                    .build(),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
