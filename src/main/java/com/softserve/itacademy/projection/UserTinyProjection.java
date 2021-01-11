package com.softserve.itacademy.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public interface UserTinyProjection extends IdNameTupleProjection {

    String getEmail();
    Set<RoleProjection> getRoles();

}
