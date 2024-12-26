package be.equals.authservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    BEARER("Bearer");

    private final String name;
}