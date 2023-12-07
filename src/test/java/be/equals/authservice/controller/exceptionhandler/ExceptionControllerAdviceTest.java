package be.equals.authservice.controller.exceptionhandler;

import be.equals.authservice.controller.response.ErrorResponse;
import be.equals.authservice.exception.NotAuthenticatedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerAdviceTest {
    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    void whenHandleSecurityException_thenReturnErrorResponse() {
        ResponseEntity<ErrorResponse> result = exceptionControllerAdvice.handleSecurityException(new NotAuthenticatedException());

        assertThat(result.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(result.getBody()).isNotNull();
        ErrorResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isNotNull();
        assertThat(body.getStatus()).isEqualTo(FORBIDDEN.value());
        assertThat(body.getMessage()).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Not authenticated");
    }

    @Test
    void whenHandleIllegalArgumentException_thenReturnErrorResponse() {
        ResponseEntity<ErrorResponse> result = exceptionControllerAdvice.handleIllegalArgumentException(new IllegalArgumentException("Error"));

        assertThat(result.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(result.getBody()).isNotNull();
        ErrorResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isNotNull();
        assertThat(body.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        assertThat(body.getMessage()).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Error");
    }
}
