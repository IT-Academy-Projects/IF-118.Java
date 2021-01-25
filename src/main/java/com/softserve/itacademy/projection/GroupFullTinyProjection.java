package com.softserve.itacademy.projection;

import java.util.Set;

public interface GroupFullTinyProjection extends GroupTinyProjection {

    Set<UserTinyProjection> getUsers();
}
