package com.softserve.itacademy.projection;

import java.util.Set;

public interface GroupFullProjection extends GroupTinyProjection {

    Set<CourseTinyProjection> getCourses();
    Set<UserTinyProjection> getUsers();

}
