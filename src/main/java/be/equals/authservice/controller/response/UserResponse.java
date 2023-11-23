package be.equals.authservice.controller.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
        @Email
        @NotNull
        @NotEmpty
        private String email;
        @NotNull
        @NotEmpty
        private String username;
        @NotNull
        @NotEmpty
        private String firstName;
        @NotNull
        @NotEmpty
        private String lastName;
}
