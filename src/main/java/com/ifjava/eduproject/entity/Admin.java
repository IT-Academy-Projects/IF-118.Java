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
@DiscriminatorValue("ADMIN")
@Entity
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_admins_users_id"))
@Table(name = "admins")
public class Admin extends User {

    private Boolean isSuper = false;

}
