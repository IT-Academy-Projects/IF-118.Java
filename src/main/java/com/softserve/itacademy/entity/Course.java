package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "courses")
public class Course extends BasicEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer ownerId;

    @Column(nullable = false)
    private Boolean disabled;

    @Column(nullable = false)
    private String description;

    private byte[] avatar;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Material> materials;

}
