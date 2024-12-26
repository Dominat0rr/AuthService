package be.equals.authservice.controller.exceptionhandler;

import be.equals.authservice.controller.response.ErrorEnum;
import be.equals.authservice.controller.response.ErrorResponse;
import be.equals.authservice.exception.NotAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {NotAuthenticatedException.class})
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .error(ErrorEnum.INVALID_CLIENT.getName())
                        .error_description(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponse.builder()
                        .error(ErrorEnum.INVALID_REQUEST.getName())
                        .error_description(ex.getMessage())
                        .build()
                );
    }
}
