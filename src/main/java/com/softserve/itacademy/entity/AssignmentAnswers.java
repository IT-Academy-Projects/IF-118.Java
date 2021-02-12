package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@Table(name = "assignment_answers")
@Entity
public class AssignmentAnswers extends BasicEntity {

    private Integer ownerId;

    private String fileReference;

    private Integer grade;

    @ManyToOne
    @JoinColumn(name = "assignment_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_assignment_assignment_answers"))
    private Assignment assignment;

    @Enumerated(EnumType.STRING)
    private AnswersStatus status;

    public enum AnswersStatus {
        NEW,
        SUBMITTED,
        REJECTED,
        GRADED
    }
}
