package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Table(name = "student_groups")
public class Group extends BasicEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer ownerId;

    @Column(nullable = false)
    private Boolean disabled;

    private byte[] avatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_users",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__users__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__users__user_id"))}
    )
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_courses",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__courses__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__groups__course_id"))}
    )
    private Set<Course> courses = new HashSet<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;

}
