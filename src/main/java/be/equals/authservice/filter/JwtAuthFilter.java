package be.equals.authservice.filter;

import be.equals.authservice.exception.UserNotFoundException;
import be.equals.authservice.repository.UserRepository;
import be.equals.authservice.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private static final RequestMatcher REQUEST_MATCHER_API = new AntPathRequestMatcher("/api/**");
    private static final RequestMatcher REQUEST_MATCHER_API_CREATE_USER = new AntPathRequestMatcher("/api/user", HttpMethod.POST.name());

    public JwtAuthFilter(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String userEmail;
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null && (SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(ANONYMOUS_USER))) {
            UserDetails userDetails = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException(userEmail));

            final boolean isValidToken = jwtUtils.isValidToken(jwtToken, userDetails);

            if (isValidToken) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher matcherApi = new NegatedRequestMatcher(REQUEST_MATCHER_API);
        RequestMatcher matcherApiCreateUser = new AndRequestMatcher(REQUEST_MATCHER_API_CREATE_USER);

        return matcherApi.matches(request) || matcherApiCreateUser.matches(request);
    }
}
