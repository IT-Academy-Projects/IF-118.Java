package com.softserve.itacademy.service;

import com.softserve.itacademy.projection.CourseTinyProjection;
import com.softserve.itacademy.projection.GroupTinyProjection;

import java.util.List;

public interface SearchService {

    List<GroupTinyProjection> searchGroup(String name);

    List<CourseTinyProjection> searchCourse(String name);
}
