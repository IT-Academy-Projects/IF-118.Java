package com.softserve.itacademy.security.filters;

import com.softserve.itacademy.security.ownauth.OwnAuthFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OwnAuthFilterTest {

    @InjectMocks
    private OwnAuthFilter ownAuthFilter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RequestMatcher requestMatcher;

    @Test
    void authenticate() throws IOException, ServletException {
        ownAuthFilter.setAuthenticationManager(authenticationManager);
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password1");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("Principal", null));

        ownAuthFilter.attemptAuthentication(request, response);

        verify(authenticationManager, times(1)).authenticate(any());
    }
}

