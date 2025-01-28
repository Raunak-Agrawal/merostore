package com.merostore.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merostore.backend.common.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
 import org.springframework.security.access.AccessDeniedException;
 import org.springframework.security.web.access.AccessDeniedHandler;
 
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.io.IOException;

 @Slf4j
 public class CustomAccessDeniedHandler implements AccessDeniedHandler {
     @Override
     public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
         log.error("Error occured in CustomAccessDeniedHandler {}", accessDeniedException.getMessage());
         MessageResponse errorResponse = new MessageResponse(accessDeniedException.getMessage()); //Or just Authentication Failed
         response.setStatus(HttpStatus.FORBIDDEN.value());
         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         new ObjectMapper().writeValue(response.getOutputStream(),errorResponse);
     }
 }