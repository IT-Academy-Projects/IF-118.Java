package com.softserve.itacademy.security.oauth2;


import com.softserve.itacademy.entity.Invitation;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final InvitationRepository invitationRepository;
    private UserRepository userRepository;

    public OAuthSuccessHandler(UserRepository userRepository, InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user was not found"));
        Authentication token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);
        if (user.getInvitationCode() != null) {
            Optional<Invitation> byCode = invitationRepository.findByCode(user.getInvitationCode());
            getRedirectStrategy().sendRedirect(request, response, getLink(byCode.get()));
        }
        if (!user.getIsPickedRole()) {
            getRedirectStrategy().sendRedirect(request, response, "/role-pick");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/user");
        }

    }

    private String getLink(Invitation invitation) {
        String courseOrGroup = invitation.getGroup() == null ? "course" : "group";
        Integer id = courseOrGroup.equals("course") ? invitation.getCourse().getId() : invitation.getGroup().getId();
        return String.format("%s/%s", courseOrGroup, id);
    }

}
