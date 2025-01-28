package com.merostore.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merostore.backend.common.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
 import org.springframework.security.core.AuthenticationException;
 import org.springframework.security.web.AuthenticationEntryPoint;
 
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.io.IOException;

 @Slf4j
 public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
     @Override
     public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
         log.error("Error occured in CustomAuthenticationEntryPoint {}", authException.getMessage());
         MessageResponse errorResponse = new MessageResponse("Bad credentials"); //Or just Authentication Failed
         response.setStatus(HttpStatus.BAD_REQUEST.value());
         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         new ObjectMapper().writeValue(response.getOutputStream(),errorResponse);
     }
 }