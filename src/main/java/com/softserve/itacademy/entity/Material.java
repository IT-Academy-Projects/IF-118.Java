package com.softserve.itacademy.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@Entity
public class Material extends BasicEntity {

    private String name;

    @Column(name = "owner_id")
    private Integer ownerId;

    private String fileReference;
    private String description;

    @Column(name = "due_date_time")
    private LocalDateTime dueDateTime;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_course_material"))
    private Course course;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private Set<Assignment> assignments;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
