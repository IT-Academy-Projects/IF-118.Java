package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "assignment_answers")
@Entity
public class AssignmentAnswers extends BasicEntity {

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "file_reference")
    private String fileReference;

    @ManyToOne
    @JoinColumn(name = "assignment_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_assignment_assignment_answers"))
    private Assignment assignment;
}
