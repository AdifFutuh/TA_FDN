package com.fdn.course.monitoring.security;

import com.fdn.course.monitoring.config.JwtConfig;
import com.fdn.course.monitoring.core.MyHttpServletRequestWrapper;
import com.fdn.course.monitoring.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        authorization = authorization==null ? "" : authorization;
        String token = null;
        String username = null;

        try{
            if(!"".equals(authorization) && authorization.startsWith("Bearer ") && authorization.length()>7){
                token = authorization.substring(7);

                if (JwtConfig.getTokenEncryptEnable().equals("y")){
                    token = Crypto.performDecrypt(token);
                }
                username = jwtUtility.getUsernameFromToken(token);
                System.out.println(username);

                String contentType = request.getContentType()==null?"":request.getContentType();
                if(!contentType.startsWith("multipart/form-data") || contentType.isBlank()){
                    request = new MyHttpServletRequestWrapper(request);
                }
                boolean cek  = SecurityContextHolder.getContext().getAuthentication() == null;
                System.out.println(cek);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println(userDetails.getAuthorities());
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    if (jwtUtility.validateToken(token)){
                        userDetails = userDetailsService.loadUserByUsername(username);
                        System.out.println(userDetails.getAuthorities());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            //handling error
        }
        filterChain.doFilter(request,response);
    }
}
