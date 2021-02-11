package com.softserve.itacademy.projection;

import org.springframework.beans.factory.annotation.Value;

public interface CourseTinyProjection extends IdNameTupleProjection {

    Integer getOwnerId();
    Boolean getDisabled();
    String getDescription();
    @Value("#{target.avatar != null ? target.avatar.id : null}")
    Integer getImageId();
}
