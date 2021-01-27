package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Builder
@Table(name = "comments")
public class Comment extends BasicEntity{

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isPrivate;

    @Column(nullable = false)
    private Boolean disabled;

    @ManyToOne
    @JoinColumn(name = "material_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_material_comment"))
    private Material material;

    @ManyToOne
    @JoinColumn(name = "owner_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_comment"))
    private User owner;
}
