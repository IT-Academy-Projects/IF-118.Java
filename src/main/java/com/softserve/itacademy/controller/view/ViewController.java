package com.softserve.itacademy.controller.view;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ViewController {
    
    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String homeView() {
        return "home.html";
    }

    @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginView() {
        return "login.html";
    }

    @GetMapping(path = "/registration", produces = MediaType.TEXT_HTML_VALUE)
    public String registrationView() {
        return "registration.html";
    }

    @GetMapping(path = "/admin", produces = MediaType.TEXT_HTML_VALUE)
    public String adminView() {
        return "admin-panel.html";
    }

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

    @GetMapping(path = "/invite", produces = MediaType.TEXT_HTML_VALUE)
    public String inviteView() {
        return "invitation.html";
    }

}
