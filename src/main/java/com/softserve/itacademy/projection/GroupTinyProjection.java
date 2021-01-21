package com.softserve.itacademy.projection;

import org.springframework.beans.factory.annotation.Value;

public interface GroupTinyProjection extends IdNameTupleProjection {

    Integer getOwnerId();
    Boolean getDisabled();
    @Value("#{target.avatar != null}")
    Boolean getHasAvatar();

}
