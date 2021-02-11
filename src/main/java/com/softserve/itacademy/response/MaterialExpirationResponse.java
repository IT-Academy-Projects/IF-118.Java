package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialExpirationResponse {
    private Integer id;
    private LocalDateTime expirationDate;
    private LocalDateTime startDate;
    private Boolean opened;
    private Integer materialId;
    private Integer groupId;
}
