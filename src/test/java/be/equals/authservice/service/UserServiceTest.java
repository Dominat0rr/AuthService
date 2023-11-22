package be.equals.authservice.service;

import be.equals.authservice.domain.User;
import be.equals.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final Long EXISTING_USER_ID = 100L;
    private static final Long NON_EXISTING_USER_ID = 400L;

    @Test
    void whenExists_withExistingId_thenReturnTrue() {
        when(userRepository.existsById(EXISTING_USER_ID)).thenReturn(true);

        assertThat(userService.exists(EXISTING_USER_ID)).isTrue();

        verify(userRepository, times(1)).existsById(EXISTING_USER_ID);
    }

    @Test
    void whenExists_withNonExistingId_thenReturnFalse() {
        when(userRepository.existsById(NON_EXISTING_USER_ID)).thenReturn(false);

        assertThat(userService.exists(NON_EXISTING_USER_ID)).isFalse();

        verify(userRepository, times(1)).existsById(NON_EXISTING_USER_ID);
    }

    @Test
    void whenCreate_withCorrectUser_thenVerifyMethodCalls() {
        userService.create(createUser());

        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void whenGetPrincipalUser_thenReturnSpringSecurityPrincipal() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(createUser());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getPrincipalUser();

        assertThat(user).isNotNull();
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
