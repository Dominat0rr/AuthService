package be.equals.authservice.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/
 */

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String error;
    private String error_description;
    private String error_uri;
}
