package com.softserve.itacademy.entity.security;

import com.softserve.itacademy.entity.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;


@Getter
@EqualsAndHashCode(callSuper = true)
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
