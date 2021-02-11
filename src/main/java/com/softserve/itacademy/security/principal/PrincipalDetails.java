package com.softserve.itacademy.security.principal;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface PrincipalDetails {

    Collection<GrantedAuthority> getAuthorities();

    Integer getId();

    String getEmail();

    String getPassword();

    Boolean getDisabled();

    Boolean getActivated();

    Boolean getPickedRole();

}
