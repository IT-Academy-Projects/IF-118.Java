package com.softserve.itacademy.security.principal;

import com.softserve.itacademy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal implements PrincipalDetails {

    private Integer id;

    private String email;

    private String password;

    private Boolean disabled;

    private Boolean activated;

    private Boolean pickedRole;

    private Set<GrantedAuthority> authorities;

    public static UserPrincipal of(User user) {
        return UserPrincipal.builder()
                .activated(user.getActivated())
                .disabled(user.getDisabled())
                .email(user.getEmail())
                .id(user.getId())
                .password(user.getPassword())
                .pickedRole(user.getPickedRole())
                .authorities(user.getAuthorities())
                .build();
    }

}
