package com.mtm.mtm.config;

import com.mtm.mtm.security.CustomUserDetailsService;
import com.mtm.mtm.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String path = req.getRequestURI();

        if (path.startsWith("/login") ||
                path.startsWith("/registro") ||
                path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/libs")) {

            chain.doFilter(request, response);
            return;
        }

        String token = null;

        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token)) {

            String username = jwtUtil.extractUsername(token);

            var userDetails = userDetailsService.loadUserByUsername(username);

            var auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}