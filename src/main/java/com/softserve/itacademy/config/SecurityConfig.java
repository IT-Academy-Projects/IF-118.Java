package com.softserve.itacademy.config;

import com.softserve.itacademy.security.oauth2.OAuthSuccessHandler;
import com.softserve.itacademy.security.ownauth.OwnAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String LOGIN_PAGE = "/login";

    private final OidcUserService oidcUserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(OidcUserService oidcUserService, OAuthSuccessHandler oAuthSuccessHandler, AuthenticationProvider authenticationProvider) {
        this.oidcUserService = oidcUserService;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
        this.authenticationProvider = authenticationProvider;
    }

    public OwnAuthFilter ownAuthFilter(AuthenticationManager authenticationManager) {
        OwnAuthFilter filter = new OwnAuthFilter(new AntPathRequestMatcher(LOGIN_PAGE, HttpMethod.POST.name()));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
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
                .antMatchers("/api/v1/users/is-authenticated", "/navbar.html", "/img/*").permitAll()
                .antMatchers("/swagger-ui/", "/swagger-ui/**", "/v2/api-docs").hasAuthority("swagger")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2Login()
                    .loginPage(LOGIN_PAGE).permitAll()
                    .userInfoEndpoint()
                    .oidcUserService(oidcUserService)
                .and()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(customAuthorizationRequestRepository())
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
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
}
