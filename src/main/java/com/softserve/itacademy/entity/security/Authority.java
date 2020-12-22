package com.softserve.itacademy.entity.security;

import com.softserve.itacademy.entity.BasicEntity;
import lombok.*;


import javax.persistence.Entity;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Entity
public class Authority extends BasicEntity {

    private String name;

//    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
//    private Set<Role> roles = new HashSet<>();

}
