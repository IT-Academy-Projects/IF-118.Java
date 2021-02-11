package com.softserve.itacademy.entity;

import com.softserve.itacademy.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@Table(name = "users")
public class User extends BasicEntity {

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private Boolean disabled = false;

    @Column
    @Builder.Default
    private Boolean activated = false;

    @Column
    private Boolean isPickedRole;

    @Column
    private String activationCode;

    @Column
    private String invitationCode;

    @Column
    private byte[] avatar;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_users_roles_user_id"))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_roles_users_role_id"))}
    )
    private Set<Role> roles = new HashSet<>();

    @Transient
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

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_courses",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__users__courses__user_id"))},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__users__course_id"))}
    )
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
