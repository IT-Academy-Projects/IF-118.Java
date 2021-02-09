package com.softserve.itacademy.config;

import com.softserve.itacademy.security.oauth2.OAuthSuccessHandler;
import com.softserve.itacademy.security.ownauth.OwnAuthFilter;
import com.softserve.itacademy.security.ownauth.OwnAuthProvider;
import com.softserve.itacademy.security.service.UserPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_PAGE = "/login";

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final OidcUserService oidcUserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final UserPrincipalService userPrincipalService;

    public SecurityConfig(OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
            OidcUserService oidcUserService, OAuthSuccessHandler oAuthSuccessHandler,
            UserPrincipalService userPrincipalService) {
        this.oAuth2UserService = oAuth2UserService;
        this.oidcUserService = oidcUserService;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
        this.userPrincipalService = userPrincipalService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
          http.addFilterBefore(ownAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
                .mvcMatchers("/","/api/v1/invitation/invite", "/registration", "/api/v1/invitation/approve/**", "/api/v1/registration", "/api/v1/activation/*", "/activation", "oauth2/**").permitAll()
                .mvcMatchers("/api/v1/users/is-authenticated", "/password-reset", "/password-reset-new", "/api/v1/password-reset", "/api/v1/password-reset/new", "/navbar.html", "/img/*").permitAll()
                .antMatchers("/swagger-ui/", "/swagger-ui/**", "/v2/api-docs").hasAuthority("swagger")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2Login()
                    .loginPage(LOGIN_PAGE).permitAll()
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .oidcUserService(oidcUserService)
                .and()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                .and()
                    .successHandler(oAuthSuccessHandler)
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.name()))
                        .logoutSuccessUrl(LOGIN_PAGE)
                        .permitAll()
                .and()
                    .rememberMe();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ownAuthProvider(userPrincipalService, getPasswordEncoder()));
    }

    public OwnAuthFilter ownAuthFilter(AuthenticationManager authenticationManager) {
        OwnAuthFilter filter = new OwnAuthFilter(new AntPathRequestMatcher(LOGIN_PAGE, HttpMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public OwnAuthProvider ownAuthProvider(UserPrincipalService userPrincipalService, PasswordEncoder passwordEncoder) {
        return new OwnAuthProvider(userPrincipalService, passwordEncoder);
    }
}
