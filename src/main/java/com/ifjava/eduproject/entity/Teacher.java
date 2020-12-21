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
@DiscriminatorValue("TEACHER")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_teachers_group_members_id"))
@Entity
@Table(name = "teachers")
public class Teacher extends GroupMember {

    private String type = "SIMPLE";

}
