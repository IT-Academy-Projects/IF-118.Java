package com.softserve.itacademy.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
@IdClass(UserReportId.class)
public class UserReport {

    @Id
    private Integer userId;
    @Id
    private Integer groupId;

    private String assignments;

    private boolean updatable;

    private String avg;
}
