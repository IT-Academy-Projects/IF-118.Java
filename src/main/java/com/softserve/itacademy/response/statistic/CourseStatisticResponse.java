package com.softserve.itacademy.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatisticResponse {
    private Set<MaterialStatisticResponse> materials;
}
