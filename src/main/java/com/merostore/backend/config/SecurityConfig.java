package com.merostore.backend.config;

import com.merostore.backend.security.filter.CustomAuthenticationFilter;
import com.merostore.backend.security.filter.CustomAuthorizationFilter;
import com.merostore.backend.security.service.UserService;
import com.merostore.backend.utils.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // ignored homepage, account area from authentication. and h2 database console
        http.csrf().
                disable().
                headers().
                frameOptions().
                disable().and().exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/**/login", "/h2-console/**", "/**/register", "/users/**").permitAll()
                .antMatchers("/seller/**", "/stores/**", "/products/**", "/categories/**").hasAnyAuthority(SecurityConstants.ROLE_SELLER)
                .antMatchers("/buyer/**").hasAnyAuthority(SecurityConstants.ROLE_BUYER)
                .antMatchers("/admin/**").hasAuthority(SecurityConstants.ROLE_ADMIN)
                .anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(
                new CustomAuthorizationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

}