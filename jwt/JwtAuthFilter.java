package cgg.blogapp.blogapp.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cgg.blogapp.blogapp.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;
        String usernameFromToken = null;

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer")) {
            System.out.println("the header is good ");

            token = header.substring(7);

            usernameFromToken = jwtService.getUsernameFromToken(token);
            System.out.println(usernameFromToken);
        }

        if (usernameFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userByUsername = customUserDetailsService.loadUserByUsername(usernameFromToken);
            if (jwtService.validateToken(token, userByUsername)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userByUsername, null, userByUsername.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        filterChain.doFilter(request, response);
    }

}
