package habsida.spring.boot_security.demo.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/accessDenied").permitAll()

                        .requestMatchers("/api/csrf", "/api/auth/me").authenticated()
                        .requestMatchers("/api/roles").hasRole("ADMIN")
                        .requestMatchers("/api/users","/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/users-api.html").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successUserHandler)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        // Для API: 401, если пользователь не авторизован
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) ->
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED),
                                PathPatternRequestMatcher.withDefaults().matcher("/api/**")
                        )

                        // Для API: 403, если доступ запрещён
                        .defaultAccessDeniedHandlerFor(
                                (request, response, accessDeniedException) ->
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN),
                                PathPatternRequestMatcher.withDefaults().matcher("/api/**")
                        )

                        // Для обычных страниц
                        .accessDeniedHandler((request, response, ex) ->
                                response.sendRedirect("/accessDenied")
                        )
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}