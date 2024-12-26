package be.equals.authservice.controller;

import be.equals.authservice.controller.request.TokenRequest;
import be.equals.authservice.controller.request.UserLoginRequest;
import be.equals.authservice.controller.response.TokenResponse;
import be.equals.authservice.controller.response.UserResponse;
import be.equals.authservice.domain.User;
import be.equals.authservice.exception.UserNotFoundException;
import be.equals.authservice.repository.UserRepository;
import be.equals.authservice.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static be.equals.authservice.controller.response.TokenType.BEARER;

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
            String token = jwtUtils.generateToken(user);
            Date expiration = jwtUtils.extractExpiration(token);

            return new ResponseEntity<>(TokenResponse.builder()
                    .access_token(token)
                    .token_type(BEARER.getName())
                    .expires_in(String.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000))
                    .build(),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/valid")
    public ResponseEntity<?> isValidToken(@Valid @RequestBody TokenRequest tokenRequest) {
        if (jwtUtils.isValidToken(tokenRequest.getToken()))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@Valid @RequestBody TokenRequest tokenRequest) {
        if (jwtUtils.isValidToken(tokenRequest.getToken())) {
            final String username = jwtUtils.extractUsername(tokenRequest.getToken());
            final User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException(username));

            final UserResponse userResponse = UserResponse.builder()
                    .email(user.getEmail())
                    .username(user.getRealUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
