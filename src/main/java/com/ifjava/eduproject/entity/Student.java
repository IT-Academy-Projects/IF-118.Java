package com.ifjava.eduproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("STUDENT")
@Entity
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_students_group_members_id"))
@Table(name = "students")
public class Student extends GroupMember {

    private Boolean isLikeHalyava = true;

}
