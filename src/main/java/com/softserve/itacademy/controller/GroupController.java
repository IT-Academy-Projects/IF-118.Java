package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.security.principal.UserPrincipal;
import com.softserve.itacademy.security.perms.GroupCreatePermission;
import com.softserve.itacademy.security.perms.GroupDeletePermission;
import com.softserve.itacademy.security.perms.GroupReadPermission;
import com.softserve.itacademy.security.perms.GroupUpdatePermission;
import com.softserve.itacademy.service.GroupService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.softserve.itacademy.config.Constance.API_V1;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(API_V1 + "groups")
public class GroupController {

    private final GroupService groupService;
    private final ObjectMapper objectMapper;

    public GroupController(GroupService groupService, ObjectMapper objectMapper) {
        this.groupService = groupService;
        this.objectMapper = objectMapper;
    }

    @GroupCreatePermission
    @PostMapping
    public ResponseEntity<GroupResponse> create(@RequestPart(value = "group") String group,
                                                @RequestPart(value = "file", required = false) MultipartFile file,
                                                @AuthenticationPrincipal UserPrincipal principal) throws JsonProcessingException {
        GroupRequest groupRequest = objectMapper.readValue(group, GroupRequest.class);
        groupRequest.setOwnerId(principal.getId());
        return new ResponseEntity<>(groupService.create(groupRequest, file), HttpStatus.CREATED);
    }

    @GroupReadPermission
    @GetMapping(path = "/{id}/avatar", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> downloadAvatarById(@PathVariable Integer id) {
        HttpHeaders headers = new HttpHeaders();
        byte[] avatar = groupService.getAvatarById(id);
//        TODO its not good practice to set such caching here. I'll explain why. Just remind me
        headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).getHeaderValue());
        return new ResponseEntity<>(new ByteArrayResource(avatar), headers, HttpStatus.OK);
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
//    TODO use automatic line wrapping to avoid such a long lines
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Integer groupId, @RequestBody GroupRequest groupRequest) {
        groupRequest.setOwnerId(principal.getId());
        groupService.updateGroup(groupId, groupRequest);
        return new ResponseEntity<>(OK);
    }

    @GroupDeletePermission
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disableRequest) {
        groupService.updateDisabled(id, disableRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/open/{materialId}")
    public ResponseEntity<List<GroupResponse>> findGroupsWithClosedMaterial(@PathVariable Integer materialId) {
        return new ResponseEntity<>(groupService.findGroupsWithClosedMaterial(materialId), OK);
    }
}
