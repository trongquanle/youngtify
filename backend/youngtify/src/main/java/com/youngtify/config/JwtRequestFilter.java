package com.youngtify.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngtify.model.CustomUser;
import com.youngtify.model.UserCache;
import com.youngtify.service.impl.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private StringRedisTemplate template;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String tokenHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                username = tokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            } catch (MalformedJwtException e) {
                System.out.println("JWT Token invalid");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            UserCache userCache = null;
            String userStr = template.opsForValue().get(username);
            if (userStr != null) {
                try {
                    userCache = new ObjectMapper().readValue(userStr, UserCache.class);
                } catch (Exception e) {
                    userCache = null;
                }
            }
            if (userCache != null) {
                userDetails = new CustomUser(userCache.getUsername(), userCache.getPassword(),
                        new ArrayList<>(), userCache.getUserId());
            } else {
                CustomUser customUser = this.userDetailsService.loadUserByUsername(username);
                userCache = new UserCache(customUser.getUsername(), customUser.getPassword(), customUser.getUserId());
                userStr = new ObjectMapper().writeValueAsString(userCache);
                template.opsForValue().set(username, userStr);
                userDetails = customUser;
            }
            // if token is valid configure Spring Security to manually set
            // authentication
            if (tokenUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
