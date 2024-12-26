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
public class TokenResponse {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String scope;
}
