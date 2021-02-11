package com.softserve.itacademy.controller.view;

import com.softserve.itacademy.security.perms.roles.AdminRolePermission;
import com.softserve.itacademy.security.perms.roles.UserRolePermission;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String homeView() {
        return "home.html";
    }

    @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginView(HttpServletRequest request, Principal principal) {
        if (principal == null) {
            String referrer = request.getHeader("Referer");
            request.getSession().setAttribute("url_prior_login", referrer);
            return "login.html";
        } else {
            return "redirect:user";
        }
    }

    @GetMapping(path = "/registration", produces = MediaType.TEXT_HTML_VALUE)
    public String registrationView(Principal principal) {
        if (principal == null) {
            return "registration.html";
        } else {
            return "redirect:user";
        }
    }

    @GetMapping(path = "/role-pick", produces = MediaType.TEXT_HTML_VALUE)
    public String rolePickView() {
            return "role-pick.html";
    }

    @AdminRolePermission
    @GetMapping(path = "/admin", produces = MediaType.TEXT_HTML_VALUE)
    public String adminView() {
        return "admin-panel.html";
    }

    @UserRolePermission
    @GetMapping(path = "/user", produces = MediaType.TEXT_HTML_VALUE)
    public String userView() {
        return "user-panel.html";
    }

    @GetMapping(path = "/profile", produces = MediaType.TEXT_HTML_VALUE)
    public String profileView() {
        return "profile.html";
    }

    @GetMapping(path = "/group", produces = MediaType.TEXT_HTML_VALUE)
    public String groupView() {
        return "group.html";
    }

    @GetMapping(path = "/course", produces = MediaType.TEXT_HTML_VALUE)
    public String courseView() {
        return "course.html";
    }

    @GetMapping(path = "/material", produces = MediaType.TEXT_HTML_VALUE)
    public String materialView() {
        return "material.html";
    }

    @GetMapping(path = "/invitation", produces = MediaType.TEXT_HTML_VALUE)
    public String inviteView() {
        return "invitation.html";
    }

    @GetMapping(path = "/group-chat", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("@accessManager.isAllowedToChat(authentication.principal, #id)")
    public String chatView(@RequestParam int id) { return "group-chat.html"; }

    @GetMapping(path = "/password-reset", produces = MediaType.TEXT_HTML_VALUE)
    public String passwordResetView() { return "password-reset.html"; }

    @GetMapping(path = "/password-reset-new", produces = MediaType.TEXT_HTML_VALUE)
    public String newPasswordView(@RequestParam String token) { return "password-reset-new.html"; }

    @GetMapping(path = "/my-group-statistic", produces = MediaType.TEXT_HTML_VALUE)
    public String myGroupStatistic() { return "user-group-statistic.html"; }

    @GetMapping(path = "/group-statistic", produces = MediaType.TEXT_HTML_VALUE)
    public String groupStatistic() { return "group-statistic.html"; }
}
