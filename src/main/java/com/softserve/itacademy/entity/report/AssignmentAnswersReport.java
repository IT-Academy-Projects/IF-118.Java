package com.softserve.itacademy.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
@Entity
public class AssignmentAnswersReport extends Report {

    private int grade;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_answer"))
    private UserReport user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_group_answer"))
    private GroupReport group;
}
