package be.equals.authservice.dto;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName
) { }
