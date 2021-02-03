package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@Table(name = "assignment")
@Entity
public class Assignment extends BasicEntity {

    private String name;

    private String description;
    private String fileReference;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private Set<AssignmentAnswers> assignmentAnswers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_assignments",
            joinColumns = {@JoinColumn(name = "assignment_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__assignments__groups__assignment_id"))},
                    inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__assignments__group_id"))})
    private Set<Group> groups;
}
