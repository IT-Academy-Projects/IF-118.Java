package com.softserve.itacademy.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public interface UserFullTinyProjection extends UserTinyProjection {

    @Value("#{@courseRepository.findAllByUserId(target.id)}")
    Set<CourseTinyProjection> getCourses();

    @Value("#{@groupRepository.findAllByUserId(target.id)}")
    Set<GroupTinyProjection> getGroups();
    Set<CommentTinyProjection> getComments();

}
