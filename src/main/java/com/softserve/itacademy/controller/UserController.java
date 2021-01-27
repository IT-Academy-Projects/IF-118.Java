package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.projection.IdNameTupleProjection;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.request.UserPasswordRequest;
import com.softserve.itacademy.response.IsAuthenticatedResponse;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.security.perms.UserDeletePermission;
import com.softserve.itacademy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<IsAuthenticatedResponse> isAuthenticated(@AuthenticationPrincipal User user) {
        if (user == null) {
            return new ResponseEntity<>(IsAuthenticatedResponse.builder().exists(false).build(), OK);
        }

        return new ResponseEntity<>(IsAuthenticatedResponse.builder().exists(true).userId(user.getId()).build(), OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userService.getUserById(user.getId()), OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserFullTinyProjection> findCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new NotFoundException("user was not found");
        }
        return new ResponseEntity<>(userService.findById(user.getId()), OK);
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<Void> changePhoto(@RequestPart(value = "avatar") MultipartFile file,
                                            @PathVariable Integer id) {
        userService.setAvatar(file, id);
        return new ResponseEntity<>(OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdNameTupleProjection> findUserNameAndIdByUserId(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.findUserNameById(id), HttpStatus.OK);
    }

    @UserDeletePermission
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

    @PutMapping("/{id}/updatePass")
    public ResponseEntity<Void> updatePass(@PathVariable Integer id, @RequestBody UserPasswordRequest request) {
        userService.changePass(id, request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UserResponse>> findByGroupId(@PathVariable Integer groupId) {
        return new ResponseEntity<>(userService.findByGroupId(groupId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/delete/{invitationId}")
    public ResponseEntity<Void> deleteInvite(@PathVariable Integer userId, @PathVariable Integer invitationId) {
        userService.deleteInvitation(userId, invitationId);
        return new ResponseEntity<>(OK);
    }
}
