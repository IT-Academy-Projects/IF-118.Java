package com.softserve.itacademy.controller;

import com.softserve.itacademy.projection.UserFullTinyProjection;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFullTinyProjection> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.findById(id), OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disableRequest) {
        userService.updateDisabled(id, disableRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<Void> updateProfileInfo(@PathVariable Integer id, @RequestParam String name,
                                                  @RequestParam String email) {
        userService.updateProfileInfo(id, name, email);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/current")
    public ResponseEntity<Boolean> checkIfTeacher(@AuthenticationPrincipal User currentUser) {
        if(currentUser.getRoles().stream().noneMatch(role -> role.getName().equals("TEACHER"))){
            return new ResponseEntity<>(false, OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
