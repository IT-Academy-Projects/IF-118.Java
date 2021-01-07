package com.softserve.itacademy.projection;

import java.util.Set;

public interface CourseTinyProjection extends IdNameTupleProjection {

    Integer getOwnerId();
    Boolean getDisabled();

}
