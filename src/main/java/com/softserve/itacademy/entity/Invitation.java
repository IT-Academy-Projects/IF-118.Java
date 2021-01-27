package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "invitation")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Invitation extends BasicEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean approved;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false, unique = true)
    private String code;

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
