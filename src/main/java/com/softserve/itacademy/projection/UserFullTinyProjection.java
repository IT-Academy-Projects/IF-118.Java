package com.softserve.itacademy.projection;

import java.util.Set;

public interface UserFullTinyProjection extends UserTinyProjection {

    Set<CourseTinyProjection> getCourses();
    Set<GroupTinyProjection> getGroups();
    Set<CommentTinyProjection> getComments();

}
