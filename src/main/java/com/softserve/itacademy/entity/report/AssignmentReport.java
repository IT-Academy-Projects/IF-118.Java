package com.softserve.itacademy.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Table
@Entity
@Builder
public class AssignmentReport extends Report {

}
