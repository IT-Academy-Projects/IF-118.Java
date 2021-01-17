package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "assignment")
@Entity
public class Assignment extends BasicEntity {

    @Column(name = "name")
    private String name;

//    TODO you don't need this explicit mapping even Column annotation
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "material_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_material_assignment"))
    private Material material;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private Set<AssignmentAnswers> assignmentAnswers;
}
