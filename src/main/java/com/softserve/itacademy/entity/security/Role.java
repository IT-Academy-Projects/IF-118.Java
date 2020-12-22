package com.softserve.itacademy.entity.security;

import com.softserve.itacademy.entity.BasicEntity;
import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Role extends BasicEntity {

    private String name;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();

    @Singular
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_authorities",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_roles_authorities_role_id"))},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_authorities_roles_authority_id"))}
    )
    private Set<Authority> authorities = new HashSet<>();

}
