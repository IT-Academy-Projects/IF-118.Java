package com.softserve.itacademy.projection;

public interface CourseTinyProjection extends IdNameTupleProjection {

    Integer getOwnerId();
    Boolean getDisabled();

}
