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
        assertThat(body.getError()).isNotNull();
        assertThat(body.getError()).isEqualTo("invalid_client");
        assertThat(body.getError_description()).isNotNull();
        assertThat(body.getError_description()).isEqualTo("Not authenticated");
        assertThat(body.getError_uri()).isNull();
    }

    @Test
    void whenHandleIllegalArgumentException_thenReturnErrorResponse() {
        ResponseEntity<ErrorResponse> result = exceptionControllerAdvice.handleIllegalArgumentException(new IllegalArgumentException("Error"));

        assertThat(result.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(result.getBody()).isNotNull();
        ErrorResponse body = result.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isNotNull();
        assertThat(body.getError()).isEqualTo("invalid_request");
        assertThat(body.getError_description()).isNotNull();
        assertThat(body.getError_description()).isEqualTo("Error");
        assertThat(body.getError_uri()).isNull();
    }
}
