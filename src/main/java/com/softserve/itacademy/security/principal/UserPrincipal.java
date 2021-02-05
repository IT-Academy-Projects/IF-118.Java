package com.softserve.itacademy.security.principal;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements PrincipalDetails {

    private static final ModelMapper modelMapper = new ModelMapper();

    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    private String email;

    private String password;

    private Boolean disabled;

    private Boolean activated;

    private Boolean pickedRole;

    private Set<Role> roles;

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toSet());

        authorities.addAll(this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toSet()));

        return authorities;
    }

    public static UserPrincipal of(User user) {
         return modelMapper.map(user, UserPrincipal.class);
    }

}
