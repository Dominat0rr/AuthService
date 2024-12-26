package be.equals.authservice.controller;

import be.equals.authservice.config.SecurityConfig;
import be.equals.authservice.controller.request.UserLoginRequest;
import be.equals.authservice.domain.User;
import be.equals.authservice.repository.UserRepository;
import be.equals.authservice.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRestControllerTest.class)
@Import({SecurityConfig.class, AuthRestController.class})
@ContextConfiguration(classes = {AuthRestController.class})
public class AuthRestControllerTest {
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private static final String TOKEN = "TOKEN";

    @Test
    void whenGetToken_withValidUser_thenReturnTokenAndStatusOK() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(createUser()));
        when(passwordEncoder.matches(any(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(any())).thenReturn(TOKEN);
        when(jwtUtils.extractExpiration(any())).thenReturn(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));

        this.mockMvc.perform(post("/auth/token")
                        .contentType(APPLICATION_JSON)
                        .content(createUserLoginRequest()))
                .andDo(print())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.access_token").value(TOKEN))
                .andExpect(jsonPath("$.token_type").exists())
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.expires_in").exists())
                .andExpect(jsonPath("$.expires_in").value("299"))
                .andExpect(jsonPath("$.refresh_token").isEmpty())
                .andExpect(jsonPath("$.scope").isEmpty())
                .andExpect(status().isOk());
    }

    private static String createUserLoginRequest() throws JsonProcessingException {
         UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("john@email.be")
                .password("password")
                .build();

         return new ObjectMapper().writeValueAsString(userLoginRequest);
    }

    private static User createUser() {
        return User.builder()
                .email("john@email.be")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
    }
}
