package com.softserve.itacademy.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class GroupReport extends Report {

    private BigDecimal avg;

    @ManyToMany
    private Set<UserReport> users;
}
