package com.softserve.itacademy.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@Entity
public class Material extends BasicEntity {

    private String name;

    @Column(name = "owner_id")
    private Integer ownerId;

    private String fileReference;
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_course_material"))
    private Course course;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private List<MaterialExpiration> expirations;

    @ManyToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    private List<Group> groups;
}
