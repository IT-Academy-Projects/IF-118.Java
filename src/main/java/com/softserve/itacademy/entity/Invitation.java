package com.softserve.itacademy.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "invitation")
public class Invitation extends BasicEntity{

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean approved;

    @Column(nullable = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_invitation"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_course_invitation"))
    private Course course;

    @ManyToOne
    @JoinColumn(name = "group_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_group_invitation"))
    private Group group;
}
