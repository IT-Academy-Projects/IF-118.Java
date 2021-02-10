package com.softserve.itacademy.controller;

import static com.softserve.itacademy.config.Constance.API_V1;
import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.security.principal.UserPrincipal;
import com.softserve.itacademy.service.InvitationService;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(API_V1 + "invitation")
public class InvitationController {
    private final InvitationService invitationService;
    @Value("${application.address}")
    private String address;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponse> sendInvitation(@AuthenticationPrincipal UserPrincipal principal,
                                                             @RequestBody InvitationRequest invitation) {
        invitation.setOwnerId(principal.getId());

        return new ResponseEntity<>(invitationService.sendInvitation(invitation), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<InvitationResponse> getByCode(@PathVariable String code) {
        return new ResponseEntity<>(invitationService.findByCode(code), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<InvitationResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(invitationService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/approve/{email}/{code}")
    public ModelAndView approveInvitation(@PathVariable String email, @PathVariable("code") String code) {

        invitationService.approveByLink(email, code);
        return new ModelAndView("redirect:" + address + "/login");
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Void> approveById(@PathVariable Integer id) {
        invitationService.approveById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InvitationResponse>> findAllByEmail(@AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(invitationService.findAllByEmail(principal.getEmail()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        invitationService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
