package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.GroupDto;
import com.softserve.itacademy.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> findAll() {
        return new ResponseEntity<>(groupService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        groupService.delete(id);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestParam Boolean disabled) {
        groupService.updateDisabled(id, disabled);
        return new ResponseEntity<>(OK);
    }

}
