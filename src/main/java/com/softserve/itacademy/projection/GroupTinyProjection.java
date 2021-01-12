package com.softserve.itacademy.projection;

public interface GroupTinyProjection extends IdNameTupleProjection {

    Integer getOwnerId();
    Boolean getDisabled();

}
