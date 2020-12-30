package com.softserve.itacademy.config;

import com.softserve.itacademy.security.OwnAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfig(AuthenticationProvider ownAuthProvider) {
        this.authenticationProvider = ownAuthProvider;
    }

    public OwnAuthFilter ownAuthFilter(AuthenticationManager authenticationManager) {
        OwnAuthFilter filter = new OwnAuthFilter(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .mvcMatchers("/resources/**", "/static/**", "/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(ownAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers( "/registration", "/api/v1/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginProcessingUrl("/login")
                        .loginPage("/login").permitAll()
                        .successForwardUrl("/")
                        .defaultSuccessUrl("/" )
                        .failureUrl("/login-error"))
                .logout(logoutConfigurer -> {
                    logoutConfigurer
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/")
                            .permitAll();
                })
                .rememberMe()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable(); //TODO: Implement CSRF

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProvider);
    }
}