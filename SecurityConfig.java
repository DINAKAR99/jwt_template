package cgg.blogapp.blogapp.config;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cgg.blogapp.blogapp.jwt.JwtAuthFilter;
import cgg.blogapp.blogapp.jwt.JwtEntryPoint;
import cgg.blogapp.blogapp.services.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "din api", description = "this is din api docs", version = "10.1", license = @License(name = "din license", url = "www.din.com")))
@SecurityScheme(name = "din_scheme", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    public CustomUserDetailsService service;
    @Autowired
    public JwtAuthFilter jwtAuthFilter;
    @Autowired
    public JwtEntryPoint jwtEntryPoint;

    public static final String[] PUBLIC_URLS = { "/api/v1/auth/**", "/api/v1/users/register", "/dologin", "/recharge" };

    @Bean
    SecurityFilterChain getSecurtyFilterChain(HttpSecurity hs) throws Exception {
        hs.csrf(s -> s.disable()).cors(
                Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers(PUBLIC_URLS)
                                .permitAll().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/api/v1/**")
                                .hasRole("USER").requestMatchers(HttpMethod.GET).permitAll().anyRequest()
                                .authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        jwtEntryPoint))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(
                        authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // .formLogin(login -> login.loginPage("/login").loginProcessingUrl("/dologin")
        // .defaultSuccessUrl("/api/v1/posts/"))
        // .logout(logout ->
        // logout.logoutUrl("/logoutt").logoutSuccessUrl("/login?logout"));

        return hs.build();
    }

    @Bean
    AuthenticationManager getAuthManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    UserDetailsService getuDetailsService() {

        return service;
    }

    @Bean
    BCryptPasswordEncoder getEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authProvider() {

        DaoAuthenticationProvider d1 = new DaoAuthenticationProvider();

        d1.setUserDetailsService(service);
        d1.setPasswordEncoder(getEncoder());

        return d1;
    }

}
