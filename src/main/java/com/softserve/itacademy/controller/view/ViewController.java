package com.softserve.itacademy.controller.view;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String loginView() {
        return "login/login.html";
    }

    @GetMapping(path = "/admin", produces = MediaType.TEXT_HTML_VALUE)
    public String adminView() {
        return "admin-panel/admin-panel.html";
    }

}
