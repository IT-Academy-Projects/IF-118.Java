package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.security.perms.GroupCreatePermission;
import com.softserve.itacademy.security.perms.GroupDeletePermission;
import com.softserve.itacademy.security.perms.GroupReadPermission;
import com.softserve.itacademy.security.perms.GroupUpdatePermission;
import com.softserve.itacademy.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GroupCreatePermission
    @PostMapping
    public ResponseEntity<GroupResponse> create(@AuthenticationPrincipal User user, @RequestBody GroupRequest groupRequest) {
        groupRequest.setOwnerId(user.getId());
        return new ResponseEntity<>(groupService.create(groupRequest), HttpStatus.CREATED);
    }

    @GroupReadPermission
    @GetMapping
    public ResponseEntity<List<GroupResponse>> findAll() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    @GroupReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(groupService.findById(id), HttpStatus.OK);
    }

    @GroupReadPermission
    @GetMapping("/owner/{id}")
    public ResponseEntity<List<GroupResponse>> findByOwner(@PathVariable Integer id) {
        return new ResponseEntity<>(groupService.findByOwner(id), HttpStatus.OK);
    }

    @GroupUpdatePermission
    @PatchMapping("/{groupId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable Integer groupId, @RequestBody GroupRequest groupRequest) {
        groupRequest.setOwnerId(user.getId());
        groupService.updateGroup(groupId, groupRequest);
        return new ResponseEntity<>(OK);
    }

    @GroupDeletePermission
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disableRequest) {
        groupService.updateDisabled(id, disableRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }
}
