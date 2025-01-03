package be.equals.authservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    INVALID_REQUEST("invalid_request"),
    INVALID_CLIENT("invalid_client"),
    INVALID_GRANT("invalid_grant"),
    INVALID_SCOPE("invalid_scope"),
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type");

    private final String name;
}
