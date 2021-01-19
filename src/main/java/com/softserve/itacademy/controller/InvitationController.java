package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponse> sendInvitation(@RequestBody InvitationRequest invitation) {
        return new ResponseEntity<>(invitationService.sendInvitation(invitation), HttpStatus.OK);
    }

    @PatchMapping("/approve/{email}/{code}")
    public ResponseEntity<InvitationResponse> approveInvitation(@PathVariable String email, @PathVariable("code") String code) {
        return new ResponseEntity<>(invitationService.approveByLink(email, code), HttpStatus.OK);
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Void> approveById(@PathVariable Integer id) {
        invitationService.approveById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InvitationResponse>> findAllByEmail(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(invitationService.findAllByEmail(user.getEmail()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        invitationService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
