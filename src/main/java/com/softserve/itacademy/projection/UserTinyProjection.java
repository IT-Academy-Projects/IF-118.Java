package com.softserve.itacademy.projection;

import java.util.Set;

public interface UserTinyProjection extends IdNameTupleProjection {

    String getEmail();
    Set<RoleProjection> getRoles();
}
