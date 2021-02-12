package com.softserve.itacademy.controller;

import com.softserve.itacademy.projection.CourseTinyProjection;
import com.softserve.itacademy.projection.GroupTinyProjection;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.GroupReadPermission;
import com.softserve.itacademy.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GroupReadPermission
    @GetMapping("/group")
    public ResponseEntity<List<GroupTinyProjection>> searchGroups(@RequestParam String searchQuery) {
        return new ResponseEntity<>(searchService.searchGroup(searchQuery), HttpStatus.OK);
    }

    @CourseReadPermission
    @GetMapping("/course")
    public ResponseEntity<List<CourseTinyProjection>> searchCourses(@RequestParam String searchQuery) {
        return new ResponseEntity<>(searchService.searchCourse(searchQuery), HttpStatus.OK);
    }

}
